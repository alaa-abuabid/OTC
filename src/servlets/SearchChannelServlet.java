package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.PublicChannel;
import models.SearchResponse;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchChannelServlet")
public class SearchChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchChannelServlet() {
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
	 * get channel name and search in db for the channel with the same name
	 * @return HttpServletResponse response 
	 * return boolean result and ArrayList<PublicChannel> ChannelNames (SearchResponse);
	 * if channel was found puts it in ChannelNames and sets result to true
	 * else sets result to false and ChannelNames to null
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<PublicChannel> ChannelNames = new ArrayList<PublicChannel>();
		Gson gson = new GsonBuilder().create();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		String channel = gson.fromJson(jsonFileContent.toString(),String.class);
		ServletContext c=getServletContext();
		ChannelNames=PublicChannel.GetChannelSearchByChannel(c, channel);
		boolean result=false;
		if(!(ChannelNames.isEmpty())){
			result=true;
		}
			
		SearchResponse ret =new SearchResponse(result, ChannelNames);
		try {
        	response.setContentType("application/json; charset=UTF-8");
        	PrintWriter out = response.getWriter();
        	out.println(gson.toJson(ret, SearchResponse.class));
        	out.close();
           
            	
        } catch (IOException e) {  
            e.printStackTrace();  
        }


	}

}
