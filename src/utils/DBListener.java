package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.AppConstants;




/**
 * 
 */
@WebListener
public class DBListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public DBListener() {
        // TODO Auto-generated constructor stub
    }
    
    //utility that checks whether the customer tables already exists
    /**
     * 
     * @param e
     * @return boolean exists
     * exists is true if db exists else false
     */
    private boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        if(e.getSQLState().equals("X0Y32")) {
            exists = true;
        } else {
            exists = false;
        }
        return exists;
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     * creates the data base if doesn't exist
     */
    public void contextInitialized(ServletContextEvent event)  { 
    	ServletContext cntx = event.getServletContext();
    	AppConstants.generalcntx=cntx;
    	
    	try{
    		
    		//obtain CustomerDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection connection = ds.getConnection();
    		
    		boolean created = false;
    		try{
    			//create Customers table
    			Statement statement = connection.createStatement();
            	statement.executeUpdate("CREATE TABLE USERS ("
    								+ 		"USERNAME 		VARCHAR(10) NOT NULL PRIMARY KEY,"
    								+ 		"PASSWORD 		VARCHAR(8) NOT NULL,"
    								+ 		"NICKNAME 		VARCHAR(20) UNIQUE,"
    								+		"DESCRIPTION 	VARCHAR(50),"
    								+ 		"PHOTO   		VARCHAR(250)"
    								+   
									")"
    								   );

									   
									   
            	statement.executeUpdate("CREATE TABLE CHANNELS ("
    					 			+ 		"NAME 			VARCHAR(30) PRIMARY KEY,"
    					 			+ 		"DESCRIPTION 	VARCHAR(500),"
    					 			+ 		"CREATOR    	VARCHAR(10) NOT NULL"
    					 			+ 	
									")"
    							   	   );
									   
									   
				statement.executeUpdate("CREATE TABLE MESSAGES ("
    								+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
    								+ 		"SENDER 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
    								+ 		"CHANNEL 		VARCHAR(30) NOT NULL REFERENCES CHANNELS(NAME) ON DELETE CASCADE,"
    								+ 		"MESSAGE 		VARCHAR(500) NOT NULL,"
    								+ 		"DATE_TIME 		TIMESTAMP NOT NULL,"
    								+ 		"PARENT  		INTEGER NOT NULL"
    								+ 	
									")"
    								   );
									   
									   

            	statement.executeUpdate("CREATE TABLE SUBSCRIPTIONS ("
    								+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
    								+ 		"CHANNEL 		VARCHAR(30) NOT NULL REFERENCES CHANNELS(NAME) ON DELETE CASCADE,"
    								+ 		"USERNAME 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME),"
    								+ 		"DATE_TIME 		TIMESTAMP NOT NULL"
    					 			+ 
									")"
    						   	       );

            	
            	
            	
            	statement.executeUpdate("CREATE TABLE PRIVATECHAT ("
									+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
									+ 		"USER1		 	VARCHAR(20) NOT NULL,"
									+ 		"USER2	    	VARCHAR(20) NOT NULL"
									+ 	
									")"
									   );
            	
            	

            	statement.executeUpdate("CREATE TABLE PRIVATEMESSAGES ("
									+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1) PRIMARY KEY,"
									+ 		"PRIVATECHAT 	INTEGER NOT NULL REFERENCES PRIVATECHAT(ID) ON DELETE CASCADE,"
									+ 		"SENDER 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
									+ 		"MESSAGE 		VARCHAR(500) NOT NULL,"
									+ 		"DATE_TIME 		TIMESTAMP NOT NULL,"
									+ 		"PARENT  		INTEGER NOT NULL"
									+ 
									")"
									   );
            	
            	statement.executeUpdate("CREATE TABLE PUBLIC_INFO ("
									+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
									+ 		"NICKNAME 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
									+ 		"CHANNEL 		VARCHAR(30) NOT NULL REFERENCES CHANNELS(NAME) ON DELETE CASCADE,"
									+ 		"UNREAD 		INTEGER,"
									+ 		"REPLIES 		INTEGER"
									+ 
									")"
									   );
            	
            	statement.executeUpdate("CREATE TABLE PRIVATE_INFO ("
									+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
									+ 		"NICKNAME1 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
									+ 		"NICKNAME2 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
									+ 		"UNREAD 		INTEGER,"
									+ 		"REPLIES 		INTEGER"
									+ 
									")"
									   );

            	
            	System.out.println("database was created.");
            	connection.commit();
            	statement.close();
            	connection.close();
            	
            	
    		}catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
    			created = tableAlreadyExists(e);
    			if (!created){
    				throw e;//re-throw the exception so it will be caught in the
    				//external try..catch and recorded as error in the log
    			}
    		}


    		//close connection
    		connection.close();

    	} catch ( SQLException | NamingException e) {
    		//log error 
    		cntx.log("Error during database initialization",e);
    	}
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     * shuts down the data base
     */
    public void contextDestroyed(ServletContextEvent event)  { 
    	 ServletContext cntx = event.getServletContext();
    	 
         //shut down database
    	 try {
     		//obtain CustomerDB data source from Tomcat's context and shutdown
     		Context context = new InitialContext();
     		BasicDataSource ds = (BasicDataSource)context.lookup(
     				cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.SHUTDOWN);
     		ds.getConnection();
     		ds = null;
		} catch (SQLException | NamingException e) {
			cntx.log("Error shutting down database",e);
		}

    }
    
  
	
}

