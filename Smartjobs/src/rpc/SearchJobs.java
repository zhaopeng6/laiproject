package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Jobs;
import db.DBConnection;
import db.DBConnectionFactory;
import external.GitHubJobsAPI;


/**
 * Servlet implementation class SearchJobs
 */
@WebServlet("/SearchJobs")
public class SearchJobs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchJobs() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String location= request.getParameter("location");
		String keyword= request.getParameter("description");
		
		GitHubJobsAPI ghjAPI = new GitHubJobsAPI();
		List<Jobs> allJobs = ghjAPI.search(location, keyword);
		
		JSONArray array = new JSONArray();
		try {
			for (Jobs onejob : allJobs) {
				JSONObject obj = onejob.toJSONObject();
				array.put(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		RpcHelper.writeJsonArray(response, array);
		
    	/*
    	response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		JSONArray array = new JSONArray();
		
		try {
			array.put(new JSONObject().put("username", "abcd"));
			array.put(new JSONObject().put("username", "1234"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.print(array);
		out.close();
		*/
					
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
