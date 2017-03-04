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
import models.Users;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
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
	 * get information needed to register a user 
	 * @return HttpServletResponse response 
	 * register user if both username and nickname don't exist already
	 * returns String result
	 * result is "registered" if everything went fine else "exists"
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
		Users u = gson.fromJson(jsonFileContent.toString(),Users.class);
		ServletContext c=getServletContext();
		boolean registered =u.RegisterUser(u,c);
		try {
        	response.setContentType("application/json; charset=UTF-8");
        	PrintWriter out = response.getWriter();
            if(registered){
            	out.println("{ \"result\": \"registered\"}");
            	out.close();
            }
            else{
            	out.println("{ \"result\": \"exists\"}");
            	out.close();
            }
            	
        } catch (IOException e) {  
            e.printStackTrace();  
        }
	}

}
