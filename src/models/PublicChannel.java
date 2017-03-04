package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.AppConstants;

public class PublicChannel {
	public String Name;
	private String Creator;
	private String Description;
	public ArrayList<String> Users;
	private ArrayList<Messages> Messages;
	int Unread;
	int Replies;
	
	
	public PublicChannel(String Name,String Description,String Creator){
		this.Name=Name;
		this.Description=Description;
		this.Creator=Creator; new ArrayList<PublicChannel>();
		this.Users=new ArrayList<String>();
		this.Messages=new ArrayList<Messages>();
	}
	
	public PublicChannel(String Name,String Description,String Creator,ArrayList<String> Users,ArrayList<Messages> Messages ){
		this.Name=Name;
		this.Description=Description;
		this.Creator=Creator;
		this.Users=new ArrayList<String>();
		this.Messages=new ArrayList<Messages>();
		this.Users=Users;
		this.Messages=Messages;
	}

	
	
	
	
	public PublicChannel(String name, String creator, String description, ArrayList<String> users,
			ArrayList<models.Messages> messages, int unread, int replies) {
		this.Name = name;
		this.Creator = creator;
		this.Description = description;
		this.Users = users;
		this.Messages = messages;
		this.Unread = unread;
		this.Replies = replies;
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

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public ArrayList<String> getUsers() {
		return Users;
	}

	public void setUsers(ArrayList<String> users) {
		Users = users;
	}

	public ArrayList<Messages> getMessages() {
		return Messages;
	}
	
	public void setMessages(ArrayList<Messages> messages) {
		Messages=messages;
	}

	public void AddMessages(Messages message) {
		this.Messages.add(message);
	}
	
	
	/**
	 * 
	 * @param  UserName
	 * @param  c
	 * @return getChannelNames
	 * gets all the channels that the UserName is subscribed to
	 */
	public static ArrayList<String> getChannelNames(String UserName,ServletContext c){
		ArrayList<String> ChannelNames = new ArrayList<String>();
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			try {
				
				pstmt = conn.prepareStatement("SELECT * FROM SUBSCRIPTIONS "+ "WHERE USERNAME=? ");
				pstmt.setString(1,UserName);
				
				
				ResultSet rs = pstmt.executeQuery();
				// loop over ResultSet 
				while(rs.next()){
					ChannelNames.add(rs.getString("CHANNEL"));		
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
		return ChannelNames;
		
	}
	
	
	/**
	 * 
	 * @param PublicChannelsList
	 * @param  date_time
	 * @param  c
	 * @return ArrayList<PublicChannel>
	 * get all users and messages for each channel in PublicChannelsList from db
	 */
	public static ArrayList<PublicChannel> getUsersAndMessages(ArrayList<PublicChannel> PublicChannelsList,HashMap<String, Timestamp> date_time, ServletContext c){
			
			try{
				//obtain DB data source from Tomcat's context
				Context context = new InitialContext();
				BasicDataSource ds = (BasicDataSource)context.lookup(
						c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
				Connection conn = ds.getConnection();
				
				PreparedStatement pstmt,pstmt1,pstmt2;
				
				for(int i=0;i<PublicChannelsList.size();i++){
					try {
						String Name=PublicChannelsList.get(i).getName();
						pstmt = conn.prepareStatement("SELECT * FROM SUBSCRIPTIONS "+ "WHERE CHANNEL=? ");
						pstmt.setString(1,Name);
						ResultSet rs = pstmt.executeQuery();
						ArrayList<String> UsersTemp=new ArrayList<String>();
						while(rs.next()){
							UsersTemp.add(rs.getString("USERNAME"));
						}
						PublicChannel temp=PublicChannelsList.get(i);
						temp.setUsers(UsersTemp);
						PublicChannelsList.set(i, temp);
						
						
						//release rs object close select statement and close connection with DB
						rs.close();
						pstmt.close();
						
					}
					catch(SQLException e)
					{
						System.err.println(e.getMessage()); // print error to error stream
					}
					
				}
				int length= PublicChannelsList.size();
				int counter=1;
				for(int j=0;j<length;j++){
					try {
						String Name=PublicChannelsList.get(j).getName();
						pstmt1 = conn.prepareStatement("SELECT * FROM MESSAGES "+ "WHERE CHANNEL=? ");
						pstmt1.setString(1,Name);
						ResultSet rs1 = pstmt1.executeQuery();
						ArrayList<Messages> MessageTemp=new ArrayList<Messages>();
						Timestamp time1=date_time.get(Name);
						while(rs1.next()){
							Timestamp time2=rs1.getTimestamp("DATE_TIME");
							if(time2.after(time1)){
								MessageTemp.add(new Messages(rs1.getString("ID"),rs1.getString("SENDER"),rs1.getString("CHANNEL"),rs1.getString("MESSAGE"),rs1.getTimestamp("DATE_TIME") ,rs1.getString("PARENT") ));
							}
						}
						PublicChannel temp=PublicChannelsList.get(j);
						temp.setMessages(MessageTemp);
						PublicChannelsList.set(j, temp);
						
						
						//release rs1 object close select statement and close connection with DB
						if(counter==length){
							rs1.close();
							pstmt1.close();
							
						}
						counter++;
						
						
					}
					catch(SQLException e)
					{
						System.err.println("hii" + e.getMessage()); // print error to error stream
					}
					
				}
				for(int j=0;j<length;j++){
					int length2= PublicChannelsList.get(j).getMessages().size();
					ArrayList<Messages> MessageTemp=new ArrayList<Messages>();
					int counter2=1;
					for(int i=0;i<length2;i++){
						try {
							Messages Message=PublicChannelsList.get(j).getMessages().get(i);
							String UserName=Message.getSender();
							pstmt2 = conn.prepareStatement("SELECT * FROM USERS "+ "WHERE NICKNAME=? ");
							pstmt2.setString(1,UserName);
							ResultSet rs2 = pstmt2.executeQuery();
							while(rs2.next()){
								String photo=rs2.getString("PHOTO");
								Message.setPhoto(photo);
							}
							MessageTemp.add(Message);
							//release rs1 object close select statement and close connection with DB
							if(counter2==length2){
								rs2.close();
								pstmt2.close();
							}
							counter2++;
							
							
						}
						catch(SQLException e)
						{
							System.err.println("hello" + e.getMessage()); // print error to error stream
						}
						
						
					}
					
					PublicChannel temp=PublicChannelsList.get(j);
					temp.setMessages(MessageTemp);
					PublicChannelsList.set(j, temp);
					
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
			return PublicChannelsList;
			
		}

	
	/**
	 * 
	 * @param  UserName
	 * @param  channelNames
	 * @param  c
	 * @return HashMap<String, Timestamp>
	 *  get for the user for each channel the time and date that he subscribed to the channel
	 */
	public static HashMap<String, Timestamp> GetTimeStamp(String UserName,ArrayList<String> channelNames, ServletContext c){
		HashMap<String, Timestamp> ret =new HashMap<String, Timestamp>();
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			for(int i=0; i<channelNames.size();i++){
				String Channel=channelNames.get(i);
				try {
					
					pstmt = conn.prepareStatement("SELECT * FROM SUBSCRIPTIONS "+ "WHERE USERNAME=? AND CHANNEL=? ");
					pstmt.setString(1,UserName);
					pstmt.setString(2,Channel);
					
					java.sql.ResultSet rs = pstmt.executeQuery();
					
					// loop over ResultSet 
					while(rs.next()){
						Timestamp time = rs.getTimestamp("DATE_TIME");
						ret.put(Channel, time);
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
		
		return ret;
		
	}
	
	
	/**
	 * 
	 * @param  UserName
	 * @param  c
	 * @return ArrayList<PublicChannel> for the user that logs in, containing all the data of public channels needed for log in UI
	 */
	public static ArrayList<PublicChannel> getAllChanels(String UserName,ServletContext c){
		ArrayList<String> ChannelNames=getChannelNames(UserName, c);
		if(ChannelNames==null){
			return null;
		}
		HashMap<String, Timestamp> date_time=GetTimeStamp(UserName,ChannelNames,c);
		ArrayList<PublicChannel> PublicChannelsList =new ArrayList<PublicChannel>();
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			int length = ChannelNames.size();
			int counter=1;
			for(int i=0;i<length;i++){
				
				try {
					String name=ChannelNames.get(i);
					pstmt = conn.prepareStatement("SELECT * FROM CHANNELS "+ "WHERE NAME=? ");
					pstmt.setString(1,name);
					
					
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next()){
						PublicChannel temp=new PublicChannel(rs.getString("NAME"),rs.getString("DESCRIPTION"),rs.getString("CREATOR"));
						PublicChannelsList.add(temp);
					}
					
					
					//release rs object close select statement and close connection with DB
					if (counter==length){
						rs.close();
						pstmt.close();
						conn.close();
					}
					counter++;
					
					
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
				
			}
			
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage());  // print error to error stream
	    	}
		
		PublicChannelsList=getUsersAndMessages(PublicChannelsList,date_time,c);
		PublicChannelsList=getUnreadAndReplies(PublicChannelsList,UserName,c);
		
		return PublicChannelsList;
		
	}
	
	
	/**
	 * 
	 * @param  publicChannelsList
	 * @param  userName
	 * @param  c
	 * @return ArrayList<PublicChannel> 
	 * get all the data of notifaction for the user for the public channels
	 */
	private static ArrayList<PublicChannel> getUnreadAndReplies(ArrayList<PublicChannel> publicChannelsList,String userName, ServletContext c) {
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			for(int i=0;i<publicChannelsList.size();i++){
				
				try {
					
					pstmt = conn.prepareStatement("SELECT * FROM PUBLIC_INFO "+ "WHERE NICKNAME=? AND CHANNEL=? ");
					pstmt.setString(1,userName);
					pstmt.setString(2,publicChannelsList.get(i).getName());
					
					
					java.sql.ResultSet rs = pstmt.executeQuery();
					
					// loop over ResultSet 
					while(rs.next()){
						PublicChannel temp=publicChannelsList.get(i);
						temp.setUnread(rs.getInt("UNREAD"));
						temp.setReplies(rs.getInt("REPLIES"));
						publicChannelsList.set(i, temp);
						
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
		
		
		return publicChannelsList;
	}

	
	/**
	 * 
	 * @param  c
	 * @return  Created 
	 * true if created
	 * creates a public channel
	 */
	public boolean CreatChannel(ServletContext c){
			
			boolean Created=false;
			
			try{
				//obtain DB data source from Tomcat's context
				Context context = new InitialContext();
				BasicDataSource ds = (BasicDataSource)context.lookup(
						c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
				Connection conn = ds.getConnection();
				
				PreparedStatement stmt;
				
				try {
					
					stmt = conn.prepareStatement("INSERT INTO CHANNELS(NAME, DESCRIPTION, CREATOR)"
							+ "VALUES(?,?,?)");
					stmt.setString(1,getName());
					stmt.setString(2, getDescription());
					stmt.setString(3, getCreator());
	
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
				catch (SQLException e) {
					System.err.println(e.getMessage()); // print error to error stream
		    	}
		    	catch(NamingException e)
		    	{
		    		System.err.println(e.getMessage()); // print error to error stream
		    	}
		    	
			return Created;
			
			
		}
	
	
	/**
	 * 
	 * @param  c
	 * @param  channel
	 * @return ArrayList<PublicChannel> of channels
	 * get the channels from db with the name given
	 */
	public static ArrayList<PublicChannel> GetChannelSearchByChannel(ServletContext c,String channel){
		ArrayList<PublicChannel> ChannelNames = new ArrayList<PublicChannel>();
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

			PreparedStatement stmt,pstmt;
			
			try {
				
				pstmt = conn.prepareStatement("SELECT * FROM CHANNELS "+ "WHERE NAME=?");
				pstmt.setString(1,channel);
				
				java.sql.ResultSet rs = pstmt.executeQuery();
				
				// loop over ResultSet 
				while(rs.next()){
//					
					stmt = conn.prepareStatement("SELECT COUNT(*) AS ROWCOUNT FROM SUBSCRIPTIONS "+ "WHERE CHANNEL=?");
					stmt.setString(1,channel);
					ResultSet rs1=stmt.executeQuery();
					rs1.next();
					int count=rs1.getInt("ROWCOUNT");
					String rowCount = Integer.toString(count);
					PublicChannel channeltemp=new PublicChannel(rs.getString("NAME"),rs.getString("DESCRIPTION"),rowCount);
					ChannelNames.add(channeltemp);
					rs1.close();
					stmt.close();
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
		
		return ChannelNames;
	}
	
	
	/**
	 * 
	 * @param  c
	 * @param  nickname
	 * @return ArrayList<PublicChannel> of channels
	 * get the channels from db where the nickname is subscribed to
	 */
	public static ArrayList<PublicChannel> GetChannelSearchByNickName(ServletContext c,String nickname){
		ArrayList<String> ChannelNames = new ArrayList<String>();
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

			PreparedStatement pstmt;
			
			try {
				
				pstmt = conn.prepareStatement("SELECT * FROM SUBSCRIPTIONS "+ "WHERE USERNAME=?");
				pstmt.setString(1,nickname);
				
				java.sql.ResultSet rs = pstmt.executeQuery();
				
				// loop over ResultSet 
				while(rs.next()){
					ChannelNames.add(rs.getString("CHANNEL"));
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
		
		ArrayList<PublicChannel> ChannelNames1 = new ArrayList<PublicChannel>();
		for(int i=0;i<ChannelNames.size();i++){
			ChannelNames1.addAll(GetChannelSearchByChannel(c,ChannelNames.get(i)));
		}
		
		return ChannelNames1;
	}
	
	
}
