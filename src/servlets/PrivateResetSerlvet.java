package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.ChatInfo;

/**
 * Servlet implementation class PrivateUnreadSerlvet
 */
@WebServlet("/PrivateResetSerlvet")
public class PrivateResetSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrivateResetSerlvet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response 
	 * get nickname1 and nickname2 of the privatechannel and resets the notifactions for the user with nickname1 in db
	 * @return HttpServletResponse response 
	 * return result
	 * result is "true" if everything went fine else its "false"
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Gson gson = new GsonBuilder().create();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		ChatInfo reset = gson.fromJson(jsonFileContent.toString(),ChatInfo.class);
		ServletContext c=getServletContext();
		boolean result=ChatInfo.PrivateReset(reset,c);
		try {
	    	response.setContentType("application/json; charset=UTF-8");
	    	PrintWriter out = response.getWriter();
	        if(result){
	        	out.println("{ \"result\": \"true\"}");
	        	out.close();
	        }
	        else{
	        	out.println("{ \"result\": \"false\"}");
	        	out.close();
	        }
	        	
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	}

}
