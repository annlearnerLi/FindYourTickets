package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;


public class TicketMasterAPI {
	private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
	private static final String DEFAULT_KEYWORD = ""; // no restriction
	private static final String API_KEY = "8viIGvzcAZcKNGzqCejWuQ7F70mRwObA";
	public static void main(String[] args) {
		TicketMasterAPI tmApi = new TicketMasterAPI();
		// Mountain View, CA
		// tmApi.queryAPI(37.38, -122.08);
		// London, UK
		// tmApi.queryAPI(51.503364, -0.12);
		// Houston, TX
		tmApi.queryAPI(29.682684, -95.295410);
	}

	public JSONArray search(double lat, double lon, String keyword) {
		if(keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		try {
			// parse by space and deal with the unvalid input
			keyword = URLEncoder.encode(keyword, "UTF-8"); //"Rick Sun" => "Rick%20Sun"

		}catch(UnsupportedEncodingException e){
			e.printStackTrace();			
		}
		String query = String.format("apikey=%s&latlong=%s,%s&keyword=%s&radius=%s", API_KEY, lat, lon, keyword, 50);
		String url = URL + "?" + query;
		System.out.print("URL = " + URL);
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
			// will call ticketmaster API
			int responseCode = connection.getResponseCode();
			System.out.println("Response code" + responseCode);
			if(responseCode != 200) {
				return new JSONArray();
			}
			// efficiently read 
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			StringBuilder response = new StringBuilder();
			while((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			JSONObject object = new JSONObject(response.toString());
			
			// check whether the JSON contains the key "_embedded"
			// because in ticketMaster the _embedded is starter
			// if it is here, that means the object is valid
			if(!object.isNull("_embedded")){
				JSONObject embedded = object.getJSONObject("_embedded");
				return embedded.getJSONArray("events");
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new JSONArray();
		
	}
	private void queryAPI(double lat, double lon) {
		JSONArray events = search(lat, lon, null);
		try {
			for(int i = 0; i < events.length(); ++i) {
				JSONObject event = events.getJSONObject(i);
				System.out.println(event.toString(2));	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}



}
}
