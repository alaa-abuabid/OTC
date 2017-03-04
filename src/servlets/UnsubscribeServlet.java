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

import models.Subscription;

/**
 * Servlet implementation class UnsubscribeServlet
 */
@WebServlet("/UnsubscribeServlet")
public class UnsubscribeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnsubscribeServlet() {
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
	 * get information needed to unsubscribe from a publicchannel ( channel name and nickname)
	 * @return HttpServletResponse response 
	 * deletes the nickname subscription for the channe from the subscription table in db 
	 * and deletes the channel and nickname in public info
	 * returns String result
	 * result is "unsubscribed" if everything went fine else its "error"
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
		Subscription unsubscriber = gson.fromJson(jsonFileContent.toString(),Subscription.class);
		ServletContext c=getServletContext();
		
		boolean unsubscribed=unsubscriber.Unsubscribe(c);
		System.out.println(unsubscribed);
		
		try {
	    	response.setContentType("application/json; charset=UTF-8");
	    	PrintWriter out = response.getWriter();
	        if(unsubscribed){
	        	out.println("{ \"result\": \"unsubscribed\"}");
	        	out.close();
	        }
	        else{
	        	out.println("{ \"result\": \"error\"}");
	        	out.close();
	        }
	        	
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
		
	}
	
	

}
