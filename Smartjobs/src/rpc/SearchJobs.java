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
    	
    	//TEST  http://localhost:8080/Smartjobs/SearchJobs?lat=37.38&lon=-122.08&term=software

    	double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		
		// term can be empty
		String term= request.getParameter("term");
		DBConnection connection = DBConnectionFactory.getConnection();

		List<Jobs> jobsList = connection.searchJobs(lat, lon, term);
		
		try {
			JSONArray array = new JSONArray();
			for (Jobs job : jobsList) {
				array.put(job.toJSONObject());
			}
			RpcHelper.writeJsonArray(response, array);
			
		 } catch (Exception e) {
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
