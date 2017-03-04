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

public class Users {
	
	private String UserName;
	private String Password;
	private String NickName;
	private String Description;
	private String Photo;

	
	/*public Users(String UserName, String Password) {
		this.UserName = UserName;
		this.Password = Password;
	}*/
	
	public Users(String UserName, String Password, String NickName,String Description,String Photo) {
		this.UserName = UserName;
		this.Password = Password;
		this.NickName=NickName;
		this.Description=Description;
		this.Photo=Photo;
	}

	public Users() {
		// TODO Auto-generated constructor stub
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		this.UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		this.Password = password;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		this.NickName = nickName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		this.Photo = photo;
	}
	
	
	/**
	 * 
	 * @param  newuser
	 * @param  c
	 * @return boolean Registered true if registered
	 */
	public boolean RegisterUser(Users newuser,ServletContext c)
	{
		boolean added=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

			PreparedStatement stmt,pstmt;//,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6;
			
			try {
				
				pstmt = conn.prepareStatement("SELECT * FROM USERS "+ "WHERE USERNAME=? AND NICKNAME=? ");
				pstmt.setString(1,newuser.getUserName());
				pstmt.setString(2,newuser.getNickName());
				
				
				java.sql.ResultSet rs = pstmt.executeQuery();
				
				// loop over ResultSet 
				if(!rs.next()){
					
					stmt = conn.prepareStatement("INSERT INTO USERS(USERNAME, PASSWORD, NICKNAME, DESCRIPTION, PHOTO)"
							+ "VALUES(?,?,?,?,?)");
					stmt.setString(1,newuser.getUserName());
					stmt.setString(2, newuser.getPassword());
					stmt.setString(3, newuser.getNickName());
					stmt.setString(4, newuser.getDescription());
					stmt.setString(5, newuser.getPhoto());
					
					
					//execute query commit changes and close insert statement
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					added = true;
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
		
		return added;
	}
	


	/**
	 * 
	 * @param  newuser
	 * @param  c
	 * @return boolean
	 * returns true if the username and password exists in db
	 */
	public boolean LoginUser(Users newuser,ServletContext c)
	{
		boolean login=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			try {
				
				pstmt = conn.prepareStatement("SELECT * FROM USERS "+ "WHERE USERNAME=? ");
				pstmt.setString(1,newuser.getUserName());
				
				
				java.sql.ResultSet rs = pstmt.executeQuery();
				
				// loop over ResultSet 
				while(rs.next()){
					String pass = rs.getString(2);
					if (pass.equals(newuser.getPassword())){
						login = true;
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
		
		return login;
	}
	
	
	/**
	 * 
	 * @param  newuser
	 * @param  c
	 * @return Users
	 * given username and password returns user of type Users with all the information from db
	 */
	public Users GetUser(Users newuser,ServletContext c){
		
		Users user =new Users();
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			try {
				pstmt = conn.prepareStatement("SELECT * FROM USERS "+ "WHERE USERNAME=? AND PASSWORD=? ");
				pstmt.setString(1,newuser.getUserName());
				pstmt.setString(2,newuser.getPassword());
				java.sql.ResultSet rs = pstmt.executeQuery();
				
				// loop over ResultSet 
				while(rs.next()){
					user.setUserName(rs.getString(1));
					user.setPassword(rs.getString(2));
					user.setNickName(rs.getString(3));
					user.setDescription(rs.getString(4));
					user.setPhoto(rs.getString(5));
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

		return user;
		
	}
	
	
	
	
	

}
