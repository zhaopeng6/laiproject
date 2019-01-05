package recommendation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
	public static JSONObject revGeoCoding (double lat, double lng) throws UnsupportedEncodingException {
		
		//http://www.mapquestapi.com/geocoding/v1/reverse?key=pXuBVSQHeF6hh5dqUe3qNuTnuNvT1XUX&location=30.333472,-81.470448
		StringBuilder sb=new StringBuilder();
		sb.append("http://www.mapquestapi.com/geocoding/v1/reverse?key=");
		sb.append(KEY);
		sb.append("&location="+Double.toString(lat)+","+Double.toString(lng));
		//sb.append(URLEncoder.encode(Double.toString(lat)+","+Double.toString(lng), "UTF-8"));
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
			
			return new JSONObject(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONObject();
	}
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
	
	public static double[][] getGeoCoordinates(JSONObject GeosJSON) throws JSONException{
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
	
	public static String getLocation(double lat, double lng) throws JSONException, UnsupportedEncodingException{
		JSONObject geos= GeoCoding.revGeoCoding(lat, lng);
		String loc = "";
		if(!geos.isNull("results")) {
			JSONArray results = geos.getJSONArray("results");
			JSONObject result = results.getJSONObject(0);
			JSONArray locations = result.getJSONArray("locations");
			JSONObject location = locations.getJSONObject(0);
			if(!location.isNull("adminArea5")){
				if(location.getString("adminArea5Type").equals("City")) {
					loc += location.getString("adminArea5");
				}
			}
			if(!location.isNull("adminArea3")){
				if(location.getString("adminArea3Type").equals("State")) {
					loc += ",";
					loc += location.getString("adminArea3");
				}
			}
		}
		return loc;
	}
	
	public static void main(String[] args) throws JSONException, UnsupportedEncodingException {
		JSONObject geos=revGeoCoding(30.333472,-81.470448);
		String loc = "";
		if(!geos.isNull("results")) {
			JSONArray results = geos.getJSONArray("results");
			JSONObject result = results.getJSONObject(0);
			JSONArray locations = result.getJSONArray("locations");
			JSONObject location = locations.getJSONObject(0);
			if(!location.isNull("adminArea5")){
				if(location.getString("adminArea5Type").equals("City")) {
					loc += location.getString("adminArea5");
				}
			}
			if(!location.isNull("adminArea3")){
				if(location.getString("adminArea3Type").equals("State")) {
					loc += ",";
					loc += location.getString("adminArea3");
				}
			}
		}
		System.out.println(loc);
	}
}
