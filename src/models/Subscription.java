package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.AppConstants;

public class Subscription {
	public String Channel;
	public String NickName;
	
	public Subscription(String Channel,String NickName){
		this.Channel=Channel;
		this.NickName=NickName;
	}

	
	public String getChannel() {
		return Channel;
	}



	public void setChannel(String Channel) {
		this.Channel = Channel;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String NickName) {
		this.NickName = NickName;
	}

	
	/**
	 * 
	 * @param  c
	 * @return  Subscribed 
	 * // true if subscribed
	 * subscribes to public channel
	 */
	public boolean Subscribe(ServletContext c){
		
		boolean Subscribed=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,pstmt;
			
			try {
				
				
				pstmt = conn.prepareStatement("SELECT * FROM SUBSCRIPTIONS "+ "WHERE CHANNEL=? AND USERNAME=? ");
				pstmt.setString(1,getChannel());
				pstmt.setString(2,getNickName());
				
				ResultSet rs = pstmt.executeQuery();
				if(!rs.next()){
					Timestamp x=new Timestamp(System.currentTimeMillis());
					stmt = conn.prepareStatement("INSERT INTO SUBSCRIPTIONS(CHANNEL, USERNAME, DATE_TIME)"
							+ "VALUES(?,?,?)");
					stmt.setString(1,getChannel());
					stmt.setString(2, getNickName());
					stmt.setTimestamp(3, x);

					//execute query commit changes and close insert statement
					stmt.executeUpdate();	
					Subscribed = true;
					stmt.close();

					
				}

				pstmt.close();
				conn.commit();
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
		if (Subscribed){
			AddToChatInfo(c,getChannel(),getNickName());
		}
	    	
		return Subscribed;
		
		
	}
	
	
	/**
	 * 
	 * @param  c
	 * @param  channel2
	 * @param  nickName2
	 * after subscribe adds the user and channel to chat info in db 
	 */
	private void AddToChatInfo(ServletContext c, String channel2, String nickName2) {
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				
				stmt = conn.prepareStatement("INSERT INTO PUBLIC_INFO(NICKNAME, CHANNEL, UNREAD, REPLIES)"
						+ "VALUES(?,?,?,?)");
				stmt.setString(1,nickName2);
				stmt.setString(2, channel2);
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
	 * @return  Unsubscribed 
	 * true if Unsubscribed
	 * unsubscribe from public channel
	 */
	public boolean Unsubscribe(ServletContext c){
		
		boolean Unsubscribed=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,stmt2;
			String channel =getChannel();
			String nickname= getNickName();
			
			try {
				
				stmt = conn.prepareStatement("DELETE FROM SUBSCRIPTIONS WHERE CHANNEL=? AND USERNAME=? ");
				stmt.setString(1,channel);
				stmt.setString(2, nickname);

				//execute query commit changes and close insert statement
				stmt.executeUpdate();
				conn.commit();
				stmt.close();
				
				stmt2 = conn.prepareStatement("DELETE FROM PUBLIC_INFO WHERE NICKNAME=? AND CHANNEL=? ");
				stmt2.setString(1,nickname);
				stmt2.setString(2, channel);

				//execute query commit changes and close insert statement
				stmt2.executeUpdate();
				conn.commit();
				stmt2.close();
				conn.close();
				
				Unsubscribed = true;
				
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
		
		if (Unsubscribed){
			DeleteFromChatInfo(c,getChannel(),getNickName());
		}
	    	
		return Unsubscribed;
		
		
	}


	/**
	 * 
	 * @param  c
	 * @param  channel2
	 * @param  nickName2
	 * after unsubscribe delete the user and channel to chat info in db
	 */
	private void DeleteFromChatInfo(ServletContext c, String channel2, String nickName2) {

		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				
				stmt = conn.prepareStatement("DELETE FROM PUBLIC_INFO WHERE NICKNAME=? AND CHANNEL=? ");
				stmt.setString(1,nickName2);
				stmt.setString(2, channel2);

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
	

	
}
