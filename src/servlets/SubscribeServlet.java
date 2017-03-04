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
 * Servlet implementation class SubscribeServlet
 */
@WebServlet("/SubscribeServlet")
public class SubscribeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubscribeServlet() {
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
	 * get information needed to subscribe to a publicchannel ( channel name and the user nickname)
	 * @return HttpServletResponse response 
	 * add the nickname and channel to the subscription table db 
	 * and adds the channel and nickname to public info
	 * returns private boolean result PublicChannel channel(SubscribeResponse)
	 * and then get the channel information and users that are subscribed to it from db
	 * and puts it in channel
	 * result is "true" if everything went fine else its "error"
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
		Subscription subscriber = gson.fromJson(jsonFileContent.toString(),Subscription.class);
		ServletContext c=getServletContext();
		
		boolean subscribed=subscriber.Subscribe(c);
		System.out.println(subscribed);
		
		try {
        	response.setContentType("application/json; charset=UTF-8");
        	PrintWriter out = response.getWriter();
            if(subscribed){
            	PublicChannel channel=  SubscribeResponse.GetChannel(c, subscriber.getChannel());
            	SubscribeResponse res=new SubscribeResponse(true, channel);
            	out.println(gson.toJson(res, SubscribeResponse.class));
            	out.close();
            }
            else{
            	SubscribeResponse res=new SubscribeResponse(false, null);
            	out.println(gson.toJson(res, SubscribeResponse.class));
            	out.close();
            }
            	
        } catch (IOException e) {  
            e.printStackTrace();  
        }

	}

}
