package recommendation;

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

public class GeoCoding {
	private static final String KEY="pXuBVSQHeF6hh5dqUe3qNuTnuNvT1XUX";
	public static JSONObject getGeoCoordinates (List<String> locations){
		StringBuilder sb=new StringBuilder();
		sb.append("http://www.mapquestapi.com/geocoding/v1/batch?key=");
		sb.append(KEY);
		sb.append("&thumbMaps=false&maxResults=5");
		
		for(String location:locations) {
			sb.append("&location=");
			try {
				sb.append(URLEncoder.encode(location, "UTF-8"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(sb.toString()).openConnection();
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode();
			System.out.println("Sending 'GET' request to URL: " + sb);
			System.out.println("Response Code: " + responseCode);
		
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			JSONObject coordinates = new JSONObject(response.toString());
			if (coordinates != null) {
				return coordinates;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	public static void main(String[] args) throws JSONException {
		List<String> locs=new ArrayList<>();
		locs.add("Fremont,CA");
		locs.add("San Jose,CA");
		
		JSONObject geos=getGeoCoordinates(locs);
		List<List<Double>> result = new ArrayList<>();
		if(!geos.isNull("results")) {
			JSONArray locations = geos.getJSONArray("results");
			for(int i=0;i<locations.length();i++) {
				JSONArray location=locations.getJSONObject(i).getJSONArray("locations");
				//may have multiple return coordinates due to not unique city
				
				JSONObject latlng=location.getJSONObject(0).getJSONObject("latLng");
				Double lng = latlng.getDouble("lng");
				Double lat = latlng.getDouble("lat");
				List<Double> coordinate = new ArrayList<>();
				coordinate.add(lng);
				coordinate.add(lat);
				result.add(coordinate);
			}
		}
		System.out.println(result);
	}
}
