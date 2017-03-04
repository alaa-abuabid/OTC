package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.AppConstants;

public class ChatInfo {
	String NickName;
	String Chat;
	int Unread;
	int Replies;
	
	public ChatInfo(String nickName, String chat, int unread, int replies) {
		NickName = nickName;
		Chat = chat;
		Unread = unread;
		Replies = replies;
	}
	
	public ChatInfo(String nickName, String chat, int unread) {
		NickName = nickName;
		Chat = chat;
		Unread = unread;
	}
	
	public ChatInfo(String nickName, String chat) {
		NickName = nickName;
		Chat = chat;
	}
	
	

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getChat() {
		return Chat;
	}

	public void setChat(String chat) {
		Chat = chat;
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

	
	/**
	 * 
	 * @param reset
	 * @param  c
	 * @return reseted
	 * reset: (nickname and chatname)
	 * resets unread and comment in db for the public chat
	 */
	public static boolean PublicReset(ChatInfo reset, ServletContext c) {
		boolean reseted=false;
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			try {
				
				pstmt = conn.prepareStatement("UPDATE PUBLIC_INFO SET UNREAD=?, REPLIES=? WHERE NICKNAME=? AND CHANNEL=?");
				pstmt.setInt(1,0);
				pstmt.setInt(2,0);
				pstmt.setString(3, reset.getNickName());
				pstmt.setString(4, reset.getChat());
				
				pstmt.executeUpdate();
				
				reseted=true;
				
				//release rs object close select statement and close connection with DB
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
		
		
		return reseted;
	}

	
	/**
	 * 
	 * @param reset 
	 * @param  c
	 * @return  order
	 * reset: (nickname and chatname)
	 * return 1 for order user1 user2 in db or 2 for order user2 user1
	 */
	private static int PrivateOrder(ChatInfo reset, ServletContext c) {
		int order=0;
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt,pstmt1;
			
			try {
				
				pstmt = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? OR USER2=? ");
				pstmt.setString(1,reset.getNickName());
				pstmt.setString(2,reset.getChat());
				
				java.sql.ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()){
					order=1;
				}
				
				//release rs object close select statement and close connection with DB
				pstmt.close();
				
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			try {
				
				pstmt1 = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? OR USER2=? ");
				pstmt1.setString(1,reset.getChat());
				pstmt1.setString(2,reset.getNickName());
				
				java.sql.ResultSet rs1 = pstmt1.executeQuery();
				
				if(rs1.next()){
					order=2;
				}
				
				//release rs object close select statement and close connection with DB
				pstmt1.close();
				
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
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
		
		return order;
	}

	
	/**
	 * 
	 * @param reset
	 * @param  c
	 * @return  reseted
	 * reset: (nickname and chatname)
	 * resets unread and comment in db for the private chat
	 */
	public static boolean PrivateReset(ChatInfo reset, ServletContext c) {
		
		//int order= PrivateOrder(reset,c);
		boolean reseted=false;
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			try {
				
				pstmt = conn.prepareStatement("UPDATE PRIVATE_INFO SET UNREAD=?, REPLIES=? WHERE NICKNAME1=? AND NICKNAME2=?");
				pstmt.setInt(1,0);
				pstmt.setInt(2,0);
				pstmt.setString(3, reset.getNickName());
				pstmt.setString(4, reset.getChat());
				
				pstmt.executeUpdate();
				
				reseted=true;
				
				//release rs object close select statement and close connection with DB
				pstmt.close();
				
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
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
		
		
		return reseted;
	}
	
	
	
	/**
	 * 
	 * @param unsubscribe 
	 * @param  c
	 * @return  Unsubscribed
	 * unsubscribe: (nickname and chatname)
	 * Unsubscribe from private channel
	 */
	public static boolean PrivateUnsubscribe(ChatInfo unsubscribe,ServletContext c){
		
		boolean Unsubscribed=false;
		int order=PrivateOrder(unsubscribe,c);
		String user1 = unsubscribe.getNickName();
		String user2= unsubscribe.getChat();
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,stmt2,stmt3;
			
			if(order==1){
				try {
					
					stmt = conn.prepareStatement("DELETE FROM PRIVATECHAT WHERE USER1=? AND USER2=? ");
					stmt.setString(1,user1);
					stmt.setString(2, user2);

					//execute query commit changes and close insert statement
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					Unsubscribed = true;
					
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
				
			}
			
			else if(order==2){
				try {
					
					stmt = conn.prepareStatement("DELETE FROM PRIVATECHAT WHERE USER1=? AND USER2=? ");
					stmt.setString(1,user2);
					stmt.setString(2, user1);

					//execute query commit changes and close insert statement
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					Unsubscribed = true;
					
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
				
				stmt2 = conn.prepareStatement("DELETE FROM PRIVATE_INFO WHERE NICKNAME1=? AND NICKNAME2=? ");
				stmt2.setString(1,user1);
				stmt2.setString(2, user2);

				//execute query commit changes and close insert statement
				stmt2.executeUpdate();
				conn.commit();
				stmt2.close();
				
				stmt3 = conn.prepareStatement("DELETE FROM PRIVATE_INFO WHERE NICKNAME1=? AND NICKNAME2=? ");
				stmt3.setString(1,user2);
				stmt3.setString(2, user1);

				//execute query commit changes and close insert statement
				stmt3.executeUpdate();
				conn.commit();
				stmt3.close();
				
				
				
				
				conn.close();
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
			DeleteFromPrivateChatInfo(c,user1,user2);
		}
	    	
		return Unsubscribed;
		
		
	}

	
	/**
	 * 
	 * @param  c
	 * @param  user1
	 * @param  user2
	 * removes data for private chat after unsubscribe
	 */
	private static void DeleteFromPrivateChatInfo(ServletContext c, String user1, String user2) {

		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,stmt2;
			
			try {
				
				stmt = conn.prepareStatement("DELETE FROM PRIVATE_INFO WHERE NICKNAME1=? AND NICKNAME2=? ");
				stmt.setString(1,user1);
				stmt.setString(2, user2);

				//execute query commit changes and close insert statement
				stmt.executeUpdate();
				conn.commit();
				stmt.close();
				
				
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			try {
				
				stmt2 = conn.prepareStatement("DELETE FROM PRIVATE_INFO WHERE NICKNAME1=? AND NICKNAME2=? ");
				stmt2.setString(1,user2);
				stmt2.setString(2, user1);

				//execute query commit changes and close insert statement
				stmt2.executeUpdate();
				conn.commit();
				stmt2.close();
				
				
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




