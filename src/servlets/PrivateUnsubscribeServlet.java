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
import models.PrivateChannel;
import utils.ChatEndPoint;

/**
 * Servlet implementation class PrivateUnsubscribeServlet
 */
@WebServlet("/PrivateUnsubscribeServlet")
public class PrivateUnsubscribeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrivateUnsubscribeServlet() {
        super();
        
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
	 * get information needed to unsubscribe from a privatechannel ( nickname1 and nickname2)
	 * @return HttpServletResponse response 
	 * deletes the private channel between both users in db and deletes the channel and users in private info
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
		ChatInfo unsubscribe = gson.fromJson(jsonFileContent.toString(),ChatInfo.class);
		ServletContext c=getServletContext();
		boolean result=ChatInfo.PrivateUnsubscribe(unsubscribe,c);
		try {
	    	response.setContentType("application/json; charset=UTF-8");
	    	PrintWriter out = response.getWriter();
	        if(result){
	        	PrivateChannel temp=new PrivateChannel(unsubscribe.getNickName(),"ClosePrivate");
            	String jsonChannel=gson.toJson(temp, PrivateChannel.class);
            	ChatEndPoint.SendPrivateChannel(jsonChannel,unsubscribe.getChat());
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
