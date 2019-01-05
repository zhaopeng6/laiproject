package rpc;

import java.io.IOException;
import java.util.ArrayList;
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

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Jobs;

/**
 * Servlet implementation class JobHistory
 */
@WebServlet("/history")
public class JobHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JobHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("user_id");
		JSONArray array = new JSONArray();
		
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			Set<Jobs> jobs = conn.getFavoriteJobs(userId);
			for (Jobs job : jobs) {
				JSONObject obj = job.toJSONObject();
				obj.append("favorite", true);
				array.put(obj);
			}
			
			RpcHelper.writeJsonArray(response, array);
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}


	/**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       DBConnection connection = DBConnectionFactory.getConnection();
       try {
    	   JSONObject input = RpcHelper.readJSONObject(request);
    	   String userId = input.getString("user_id");
    	   JSONArray array = input.getJSONArray("favorite");
    	   List<String> jobIds = new ArrayList<>();
    	   for(int i = 0; i < array.length(); ++i) {
    		   jobIds.add(array.getString(i));
    	   }
    	   connection.setFavoriteJobs(userId, jobIds);
    	   RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
	 
       } catch (Exception e) {
    	   e.printStackTrace();
       } finally {
    	   connection.close();
       }

}


    /**
     * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
    */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	DBConnection connection = DBConnectionFactory.getConnection();
    	try {
    		JSONObject input = RpcHelper.readJSONObject(request);
    		String userId = input.getString("user_id");
			 JSONArray array = input.getJSONArray("favorite");
			 List<String> jobIds = new ArrayList<>();
			 for(int i = 0; i < array.length(); ++i) {
				 jobIds.add(array.getString(i));
			 }
			 connection.unsetFavoriteJobs(userId, jobIds);
			 RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
			 
		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			 connection.close();
			}
		}

}
