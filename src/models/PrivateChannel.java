package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.AppConstants;

public class PrivateChannel {
	public String NickName;
	public String ID;
	public ArrayList<Messages> Messages;
	public int Unread;
	public int Replies;
	
	 
	
	
	
	public PrivateChannel(String nickName, String id, ArrayList<models.Messages> messages, int unread, int replies) {
		super();
		NickName = nickName;
		ID = id;
		Messages = messages;
		Unread = unread;
		Replies = replies;
	}

	public PrivateChannel(String NickName,String Id){
		this.NickName=NickName;
		this.ID=Id;
	}
	
	public PrivateChannel(String NickName,String Id,ArrayList<Messages> Messages){
		this.NickName=NickName;
		this.Messages=Messages;
		this.ID=Id;
	}

	
	

	public int getUnread() {
		return Unread;
	}

	public void setUnread(int unread) {
		Unread = unread;
	}

	public int getReplies() {
		return Replies;
	}

	public void setReplies(int replies) {
		Replies = replies;
	}

	public String getId() {
		return ID;
	}

	public void setId(String id) {
		ID = id;
	}

	public String getNickName() {
		return NickName;
	}


	public void setNickName(String nickName) {
		NickName = nickName;
	}


	public ArrayList<Messages> getMessages() {
		return Messages;
	}


	public void setMessages(ArrayList<Messages> messages) {
		Messages = messages;
	}
	

	
	/**
	 * 
	 * @param  NickName
	 * @param  c
	 * @return ArrayList<PrivateChannel>
	 * return ArrayList<PrivateChannel> for the user that logs in, containing all the data for private channels needed for log in UI
	 */
	public static ArrayList<PrivateChannel> getAllChanels(String NickName,ServletContext c){
		
		ArrayList<PrivateChannel> PrivateChannelsList =new ArrayList<PrivateChannel>();
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			try {
				
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? OR USER2=? ");
				pstmt.setString(1,NickName);
				pstmt.setString(2,NickName);
				
				java.sql.ResultSet rs = pstmt.executeQuery();
				
				// loop over ResultSet 
				while(rs.next()){
					String nickname1=rs.getString("USER1");
					String nickname2=rs.getString("USER2");
					String Id=Integer.toString(rs.getInt("ID"));
					if(NickName.equals(nickname1)){
						PrivateChannel x=new PrivateChannel(nickname2,Id);
						PrivateChannelsList.add(x);

					}
					else if(NickName.equals(nickname2)){
						PrivateChannel x=new PrivateChannel(nickname1,Id);
						PrivateChannelsList.add(x);
					}
				}
				//release rs object close select statement and close connection with DB
				rs.close();
				pstmt.close();
				conn.close();
				
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
		}
		catch (SQLException e) {
			System.err.println(e.getMessage()); // print error to error stream
    	}
    	catch(NamingException e)
    	{
    		System.err.println(e.getMessage()); // print error to error stream
    	}
			

		PrivateChannelsList=getMessages(NickName,PrivateChannelsList,c);
		PrivateChannelsList=getUnreadAndReplies(PrivateChannelsList,NickName,c);
		
		return PrivateChannelsList;
		
	}
	
	
	/**
	 * 
	 * @param  PrivateChannelList
	 * @param  NickName
	 * @param  c
	 * @return ArrayList<PrivateChannel> 
	 *  ArrayList<PrivateChannel> containing the notifications for the private channels from db
	 */
	private static ArrayList<PrivateChannel> getUnreadAndReplies(ArrayList<PrivateChannel> PrivateChannelList,String NickName, ServletContext c) {
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			for(int i=0;i<PrivateChannelList.size();i++){
				
				try {
					
					pstmt = conn.prepareStatement("SELECT * FROM PRIVATE_INFO "+ "WHERE NICKNAME1=? AND NICKNAME2=? ");
					pstmt.setString(1,NickName);
					pstmt.setString(2,PrivateChannelList.get(i).getNickName());
					
					
					java.sql.ResultSet rs = pstmt.executeQuery();
					
					// loop over ResultSet 
					while(rs.next()){
						PrivateChannel temp=PrivateChannelList.get(i);
						temp.setUnread(rs.getInt("UNREAD"));
						temp.setReplies(rs.getInt("REPLIES"));
						PrivateChannelList.set(i, temp);
						
					}
					
					//release rs object close select statement and close connection with DB
					rs.close();
					pstmt.close();
					
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
				
				
				
				
			}
			conn.close();

			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
		
		
		return PrivateChannelList;
	}

	
	
	
	
	
	/**
	 * 
	 * @param  PrivateChannelsList
	 * @param  NickName1
	 * @param  c
	 * @return ArrayList<PrivateChannel> 
	 * ArrayList<PrivateChannel> containing the messages from db
	 */
	public static ArrayList<PrivateChannel> getMessages(String NickName1,ArrayList<PrivateChannel> PrivateChannelsList,ServletContext c){
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt,pstmt1;
			
			for(int i=0;i<PrivateChannelsList.size();i++){
				try {
					String NickName2=PrivateChannelsList.get(i).getNickName();
					int Id=Integer.parseInt(PrivateChannelsList.get(i).getId());
					ArrayList<Messages> msgtemp=new ArrayList<Messages>();
					pstmt = conn.prepareStatement("SELECT * FROM PRIVATEMESSAGES "+ "WHERE SENDER=? AND PRIVATECHAT=? ");
					pstmt.setString(1,NickName1);
					pstmt.setInt(2,Id);
					ResultSet rs1 = pstmt.executeQuery();
					while(rs1.next()){
						Messages msg=new Messages(rs1.getString("ID"),rs1.getString("SENDER"),rs1.getString("PRIVATECHAT"),rs1.getString("MESSAGE"),rs1.getTimestamp("DATE_TIME") ,rs1.getString("PARENT"));
						msgtemp.add(msg);
					}
					
					//release rs object close select statement and close connection with DB
					rs1.close();
					pstmt.close();

					
					pstmt1 = conn.prepareStatement("SELECT * FROM PRIVATEMESSAGES "+ "WHERE SENDER=? AND PRIVATECHAT=? ");
					pstmt1.setString(1,NickName2);
					pstmt1.setInt(2,Id);
					ResultSet rs2 = pstmt1.executeQuery();
					while(rs2.next()){
						Messages msg=new Messages(rs2.getString("ID"),rs2.getString("SENDER"),rs2.getString("PRIVATECHAT"),rs2.getString("MESSAGE"),rs2.getTimestamp("DATE_TIME") ,rs2.getString("PARENT"));
						msgtemp.add(msg);
					}
					
					//release rs object close select statement and close connection with DB
					rs2.close();
					pstmt1.close();
					
					int counter2=1;
					int length2=msgtemp.size();
					PreparedStatement pstmt2;
					for(int j=0;j<length2;j++){
						try {
							String UserName=msgtemp.get(j).getSender();
							pstmt2 = conn.prepareStatement("SELECT * FROM USERS "+ "WHERE NICKNAME=? ");
							pstmt2.setString(1,UserName);
							ResultSet rs3 = pstmt2.executeQuery();
							while(rs3.next()){
								String photo=rs3.getString("PHOTO");
								msgtemp.get(j).setPhoto(photo);
							}
							//release rs1 object close select statement and close connection with DB
							if(counter2==length2){
								rs2.close();
								pstmt2.close();
								conn.close();
							}
							counter2++;
							
							
						}
						catch(SQLException e)
						{
							System.err.println("hello" + e.getMessage()); // print error to error stream
						}
						
						
					}
					
					PrivateChannel channeltemp=PrivateChannelsList.get(i);
					channeltemp.setMessages(msgtemp);
					PrivateChannelsList.set(i, channeltemp);
					
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
				
			}
			conn.close();
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}

		
		return PrivateChannelsList;
				
	}

	
	/**
	 * 
	 * @param  c
	 * @return  Created 
	 * Created true if created
	 * creates a private channel in db
	 */
	public boolean CreatChannel(ServletContext c){
		
		boolean Created=false;
		boolean temp=true,temp1=true;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,stmt2,stmt3;
			stmt2 = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? AND USER2=? ");
			stmt2.setString(1,getNickName());
			stmt2.setString(2,getId());
			ResultSet rs = stmt2.executeQuery();
			temp=rs.next();
			
			
			stmt3 = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? AND USER2=? ");
			stmt3.setString(1,getId());
			stmt3.setString(2,getNickName());
			ResultSet rs1 = stmt3.executeQuery();
			temp1=rs1.next();

			if((temp==false)&&(temp1==false)){
				try {
					
					stmt = conn.prepareStatement("INSERT INTO PRIVATECHAT(USER1, USER2)"
							+ "VALUES(?,?)");
					stmt.setString(1,getNickName());
					stmt.setString(2, getId());

					//execute query commit changes and close insert statement
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					Created = true;
					
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
				
			}

			conn.close();
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
		
		if (Created){
			AddToChatInfo(c,getNickName(),getId());
			AddToChatInfo(c,getId(),getNickName());
		}
		return Created;
		
		
	}
	
	
	/**
	 * 
	 * @param  c
	 * @param  nickName1
	 * @param  nickName2
	 * after creating a channel adds the users to chat info in db
	 */
	private void AddToChatInfo(ServletContext c, String nickName1, String nickName2) {
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				
				stmt = conn.prepareStatement("INSERT INTO PRIVATE_INFO(NICKNAME1, NICKNAME2, UNREAD, REPLIES)"
						+ "VALUES(?,?,?,?)");
				stmt.setString(1,nickName1);
				stmt.setString(2, nickName2);
				stmt.setInt(3, 0);
				stmt.setInt(4, 0);

				//execute query commit changes and close insert statement
				stmt.executeUpdate();
				conn.commit();
				stmt.close();
				
				
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
		
	}


	/**
	 * 
	 * @param  c
	 * @return  id
	 * id of private channel
	 */
	public String GetIdFromDB(ServletContext c) {
		String ID="";
		boolean found=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt3,stmt2;
			stmt2 = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? AND USER2=? ");
			stmt2.setString(1,getNickName());
			stmt2.setString(2,getId());
			ResultSet rs = stmt2.executeQuery();
			while(rs.next()){
				try {
					int x=rs.getInt("ID");
					ID=Integer.toString(x);
					found=true;
					
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
			}
			if(!found){
				stmt3 = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? AND USER2=? ");
				stmt3.setString(1,getId());
				stmt3.setString(2,getNickName());
				ResultSet rs1 = stmt3.executeQuery();
				while(rs1.next()){
					try {
						int x=rs.getInt("ID");
						ID=Integer.toString(x);
						found=true;
					}
					catch(SQLException e)
					{
						System.err.println(e.getMessage()); // print error to error stream
					}
				}
			}

			conn.close();
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
		return ID;	
		
	
	}


}
