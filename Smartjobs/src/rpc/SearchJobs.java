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
import javax.servlet.http.HttpSession;

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
@WebServlet("/search")
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
    	// allow access only if session exists
    	/* HttpSession session = request.getSession(false);
    	  if (session == null) {
    		response.setStatus(403);
    		return;
    	 } 

    	// optional
    	String userId = session.getAttribute("user_id").toString(); */
    	
    	String location= request.getParameter("location");
		String keyword= request.getParameter("description");
		String userId = request.getParameter("user_id");
		
		DBConnection connection = DBConnectionFactory.getConnection();
        try {
		  // GitHubJobsAPI ghjAPI = new GitHubJobsAPI();
		  List<Jobs> allJobs = connection.searchJobs(location, keyword);
		  Set<String> favoritedJobIds = connection.getFavoriteJobIds(userId);
		
		  JSONArray array = new JSONArray();
			for (Jobs onejob : allJobs) {
				JSONObject obj = onejob.toJSONObject();
				obj.put("favorite", favoritedJobIds.contains(onejob.getId()));
				array.put(obj);
			}
			RpcHelper.writeJsonArray(response, array);
        } catch (JSONException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
