package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Jobs;
import entity.Jobs.JobsBuilder;


public class GitHubJobsAPI {
	private static final String JOBID = "id";
	private static final String JOBTYPE = "type";
	private static final String GITHUBLINK = "url";
	private static final String POSTTIME = "created_at";
	private static final String COMPANY = "company";
	private static final String COMPANYWEBSITE = "company_url";
	private static final String LOCATION = "location";
	private static final String JOBTITLE = "title";
	private static final String JOBDESCRIPTION = "description";
	private static final String APPLYLINK = "how_to_apply";
	private static final String COMPANYLOGO = "company_logo";
	
	private static final String URL = "https://jobs.github.com/positions.json";
	private static final String DEFAULT_KEYWORD = "material"; // no restriction
	//private static final String API_KEY = "USE_YOUR_OWN_KEY";
	
	public List<Jobs> search(double lat, double lon, String keyword) {
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
			
			JSONArray apiJobs = new JSONArray(response.toString());
			if (apiJobs != null) {
				return getJobsList(apiJobs);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	
	//Convert JSONArray to a list of item objects
	private List<Jobs> getJobsList(JSONArray apiJobs) throws JSONException {
		List<Jobs> jobsList = new ArrayList<>();

		for (int i = 0; i < apiJobs.length(); ++i) {
			JSONObject oneJob = apiJobs.getJSONObject(i);
			
			JobsBuilder builder = new JobsBuilder();
			
			if (!oneJob.isNull(JOBID)) {
				builder.setId(oneJob.getString(JOBID));
			}
			if (!oneJob.isNull(JOBTYPE)) {
				builder.setType(oneJob.getString(JOBTYPE));
			}
			if (!oneJob.isNull(GITHUBLINK)) {
				builder.setUrl(oneJob.getString(GITHUBLINK));
			}
			if (!oneJob.isNull(POSTTIME)) {
				builder.setCreated_at(oneJob.getString(POSTTIME));
			}
			if (!oneJob.isNull(COMPANY)) {
				builder.setCompany(oneJob.getString(COMPANY));
			}
			if (!oneJob.isNull(COMPANYWEBSITE)) {
				builder.setCompany_url(oneJob.getString(COMPANYWEBSITE));
			}
			if (!oneJob.isNull(LOCATION)) {
				builder.setLocation(oneJob.getString(LOCATION));
			}
			if (!oneJob.isNull(JOBTITLE)) {
				builder.setTitle(oneJob.getString(JOBTITLE));
			}
			if (!oneJob.isNull(JOBDESCRIPTION)) {
				builder.setDescription(oneJob.getString(JOBDESCRIPTION));
			}
			if (!oneJob.isNull(APPLYLINK)) {
				builder.setHow_to_apply(oneJob.getString(APPLYLINK));
			}
			if (!oneJob.isNull(COMPANYLOGO)) {
				builder.setCompany_logo(oneJob.getString(COMPANYLOGO));
			}
			
			jobsList.add(builder.build());
		}
		return jobsList;
	}
	
	
	private void queryAPI(double lat, double lon) {
		List<Jobs> jobsList = search(lat, lon, null);

		try {
			for (Jobs aJob : jobsList) {
				JSONObject jsonObject = aJob.toJSONObject();
				System.out.println(jsonObject);
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

