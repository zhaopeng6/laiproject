package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map.Entry;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Jobs;
import entity.Jobs.JobsBuilder;
import external.GitHubJobsAPI;

public class JobRecommendation {
	public List<Jobs> recommendJobs (String userId, String address) throws JSONException{
		
		DBConnection connection = DBConnectionFactory.getConnection();
		Set<String> favoritedJobIds = connection.getFavoriteJobIds(userId);
		Map<String, Integer> allTitles = new HashMap<>();
		List<String> jobLocations=new ArrayList<>();
		for(String jobId:favoritedJobIds) {
			String title = connection.getJobTitle(jobId);
			String location = connection.getJobLocation(jobId);
			allTitles.put(title, allTitles.getOrDefault(title,0)+1);
			jobLocations.add(location);
		}
		List<Entry<String,Integer>> titleList = new ArrayList<>(allTitles.entrySet());
		Collections.sort(titleList, (e1,e2)->Integer.compare(e2.getValue(), e1.getValue()));
		String recommendTitle = "";
		if(!titleList.isEmpty()) {
			recommendTitle=titleList.get(0).getKey();
		}
		JSONObject jobGeosJSON=GeoCoding.getGeoCoordinates(jobLocations);
		
		double[][] jobGeos = getGeoCoordinates(jobGeosJSON);
		
		double recommendLat=0,recommendLng=0;
		for(double[] coordinate:jobGeos) {
			recommendLat+=coordinate[0];
			recommendLng+=coordinate[1];
		}
		recommendLat/=jobGeos.length;
		recommendLng/=jobGeos.length;
		GitHubJobsAPI ghjApi = new GitHubJobsAPI();
		List<Jobs> recJobs = ghjApi.search(recommendLat, recommendLng, recommendTitle);
		
		return recJobs;
	}
	
	private List<Jobs> getJobList(JSONArray rjobs) throws JSONException{
		List<Jobs> result = new ArrayList<>();
		for(int i=0;i<rjobs.length();i++) {
			JSONObject obj = rjobs.getJSONObject(i);
			JobsBuilder builder = new JobsBuilder();
			if(!obj.isNull("type")) {
				builder.setType(obj.getString("type"));
			}
			if(!obj.isNull("created_at")) {
				builder.setUrl(obj.getString("created_at"));
			}
			if(!obj.isNull("company")) {
				builder.setUrl(obj.getString("company"));
			}
			if(!obj.isNull("company_url")) {
				builder.setUrl(obj.getString("company_url"));
			}
			if(!obj.isNull("location")) {
				builder.setUrl(obj.getString("location"));
			}
			if(!obj.isNull("title")) {
				builder.setUrl(obj.getString("title"));
			}
			if(!obj.isNull("description")) {
				builder.setUrl(obj.getString("description"));
			}
			if(!obj.isNull("how_to_apply")) {
				builder.setUrl(obj.getString("how_to_apply"));
			}
			if(!obj.isNull("company_logo")) {
				builder.setUrl(obj.getString("company_logo"));
			}
			result.add(builder.build());
		}
		return result;
	}
	
	private double[][] getGeoCoordinates(JSONObject GeosJSON) throws JSONException{
		;
		int k = 3; //KMeans cluster number
		if(!GeosJSON.isNull("results")) {
			JSONArray locations = GeosJSON.getJSONArray("results");
			double[][] result = new double[locations.length()][2];
			for(int i=0;i<locations.length();i++) {
				JSONArray location=locations.getJSONObject(i).getJSONArray("locations");
				//may have multiple return coordinates due to not unique city
				//TODO: add more location parameters to make return unique
				JSONObject latlng=location.getJSONObject(0).getJSONObject("latLng");
				double lng = latlng.getDouble("lng");
				double lat = latlng.getDouble("lat");
				result[i][0] = lat; result[i][1] = lng;
			}
			return result;
		}
		
		return new double[0][0];
	}
	
	public static void main(String[] args) {
		
	}
}
