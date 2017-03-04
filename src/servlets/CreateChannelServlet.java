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

import models.PublicChannel;
import models.SubscribeResponse;
import models.Subscription;

/**
 * Servlet implementation class CreateChannelServlet
 */
@WebServlet("/CreateChannelServlet")
public class CreateChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateChannelServlet() {
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
	 * get information needed to create a public channel from client side and creates it
	 * @return HttpServletResponse response 
	 * creates channel and adds user to subscription of the channel and created a row in...
	 * public info for the user and channel
	 * returns boolean result and PublicChannel channel that was created it in db
	 * result is true if everything went fine else its false
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
		PublicChannel channel = gson.fromJson(jsonFileContent.toString(),PublicChannel.class);
		ServletContext c=getServletContext();
		
		boolean created=channel.CreatChannel(c);
		
		Subscription subscriber=new Subscription(channel.getName(), channel.getCreator());
		boolean subscribed=subscriber.Subscribe(c);
		
		try {
        	response.setContentType("application/json; charset=UTF-8");
        	PrintWriter out = response.getWriter();
            if(subscribed&&created){
            	SubscribeResponse res=new SubscribeResponse(true, channel);
            	out.println(gson.toJson(res, SubscribeResponse.class));
            	out.close();
            }
            else{
            	SubscribeResponse res=new SubscribeResponse(false, channel);
            	out.println(gson.toJson(res, SubscribeResponse.class));
            	out.close();
            }
            	
        } catch (IOException e) {  
            e.printStackTrace();  
        }
		
		
	}

}
