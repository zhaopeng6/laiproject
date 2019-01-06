package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Login
 */
@WebServlet(“/registration”)
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registration() {
        super();
        // TODO Auto-generated constructor stub
    }

       /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
          
		String email = request.getParameter(“email”);  
		String psw   = request.getParameter(“psw”);  
		String name  = request.getParameter(“name”);  
		String jobtype = request.getParameter(“jobtype”);  
		String city   = request.getParameter(“city”);  
		String zipcode  = request.getParameter(“zipcode”); 

		DBConnection connection = DBConnectionFactory.getConnection(); 
        try {
        /* Working in progress
       } catch (Exception e) {
    	   e.printStackTrace();
       } finally {
    	   connection.close();
       }

}
