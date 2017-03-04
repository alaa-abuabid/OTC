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

import models.CreatePrivateResponse;
import models.PrivateChannel;
import utils.ChatEndPoint;

/**
 * Servlet implementation class CreatePrivateChannelServlet
 */
@WebServlet("/CreatePrivateChannelServlet")
public class CreatePrivateChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreatePrivateChannelServlet() {
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
	 * get information needed to create a private channel from client side and creates it
	 * @return HttpServletResponse response 
	 * creates channel and adds the two users to subscription of the channel and created a row in...
	 * private info for the user and channel
	 * returns boolean result and PrivateChannel channel that was created it in db
	 * result is true if everything went fine else its false
	 * if true it send to the other user if was online to create the channel there too
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
		PrivateChannel channel = gson.fromJson(jsonFileContent.toString(),PrivateChannel.class);
		ServletContext c=getServletContext();
		
		boolean created=false;
		if(!((channel.getNickName()).equals(channel.ID))){
			created=channel.CreatChannel(c);

		}
		
		
		try {
        	response.setContentType("application/json; charset=UTF-8");
        	PrintWriter out = response.getWriter();
            if(created){
            	String id=channel.GetIdFromDB(c);
            	String NickName=channel.getId();
            	PrivateChannel retChannel=new PrivateChannel(NickName, id);
            	PrivateChannel temp=new PrivateChannel(channel.getNickName(),"OpenPrivate");
            	String jsonChannel=gson.toJson(temp, PrivateChannel.class);
            	ChatEndPoint.SendPrivateChannel(jsonChannel,retChannel.getNickName());
            	CreatePrivateResponse res=new CreatePrivateResponse(true, retChannel);
            	//String temp = gson.toJson(res, CreatePrivateResponse.class);
            	out.println(gson.toJson(res, CreatePrivateResponse.class));
            	out.close();
            }
            else{
            	CreatePrivateResponse res=new CreatePrivateResponse(false, null);
            	out.println(gson.toJson(res, CreatePrivateResponse.class));
            	out.close();
            }
            	
        } catch (IOException e) {  
            e.printStackTrace();  
        }
		
		
		
	}

}
