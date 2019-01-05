package recommendation;

import java.io.UnsupportedEncodingException;
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
	public List<Jobs> recommendJobs (String userId) throws JSONException, UnsupportedEncodingException{
		
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
		
		double[][] jobGeos = GeoCoding.getGeoCoordinates(jobGeosJSON);
		
		double recommendLat=0,recommendLng=0;
		for(double[] coordinate:jobGeos) {
			recommendLat+=coordinate[0];
			recommendLng+=coordinate[1];
		}
		recommendLat/=jobGeos.length;
		recommendLng/=jobGeos.length;
		GitHubJobsAPI ghjApi = new GitHubJobsAPI();
		List<Jobs> recJobs = ghjApi.search(GeoCoding.getLocation(recommendLat,recommendLng), recommendTitle);
		
		return recJobs;
	}
	
	public static void main(String[] args) {
		
	}
}
