package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.AppConstants;

public class SubscribeResponse {

	private boolean result;
	private PublicChannel channel;
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public PublicChannel getChannel() {
		return channel;
	}
	public void setChannel(PublicChannel channel) {
		this.channel = channel;
	}
	public SubscribeResponse(boolean result, PublicChannel channel) {
		this.result = result;
		this.channel = channel;
	}
	
	
	/**
	 * 
	 * @param  c
	 * @param  Name
	 * @return PublicChannel
	 * get public channel from db by the name of it
	 */
	public static PublicChannel GetChannel(ServletContext c, String Name){
		PublicChannel temp = null;

		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			try {
				pstmt = conn.prepareStatement("SELECT * FROM CHANNELS "+ "WHERE NAME=? ");
				pstmt.setString(1,Name);
				
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					temp=new PublicChannel(rs.getString("NAME"),rs.getString("DESCRIPTION"),rs.getString("CREATOR"));
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
	    		System.err.println(e.getMessage());  // print error to error stream
	    	}
		return temp;
	}
	
	
	
	
}
