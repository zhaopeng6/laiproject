package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;


public class GitHubJobsAPI {
	private static final String URL = "https://jobs.github.com/positions.json";
	private static final String DEFAULT_KEYWORD = "material"; // no restriction
	//private static final String API_KEY = "USE_YOUR_OWN_KEY";
	
	public JSONArray search(double lat, double lon, String keyword) {
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8"); // Rick Sun => Rick20%Sun
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		String query = String.format("description=%s&lat=%s&long=%s", keyword, lat, lon);

	    
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(URL + "?" + query).openConnection();
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode();
			System.out.println("Sending 'GET' request to URL: " + URL + "?" + query);
			System.out.println("Response Code: " + responseCode);
		
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			JSONArray jobs = new JSONArray(response.toString());
			if (jobs != null) {
				return jobs;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONArray();
	}
	
	private void queryAPI(double lat, double lon) {
		JSONArray jobs = search(lat, lon, null);

		try {
			for (int i = 0; i < jobs.length(); ++i) {
				JSONObject job = jobs.getJSONObject(i);
				System.out.println(job.toString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main entry for sample TicketMaster API requests.
	 */
	public static void main(String[] args) {
		GitHubJobsAPI ghjApi = new GitHubJobsAPI();
		// Mountain View, CA
		ghjApi.queryAPI(37.38, -122.08);
		// London, UK
		// ghjApi.queryAPI(51.503364, -0.12);
		// Houston, TX
		// ghjApi.queryAPI(29.682684, -95.295410);
	}


}

