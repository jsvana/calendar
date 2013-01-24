import java.io.*;
import org.json.*;

public class Storage {
	private static final String FILENAME = "events.json";

	private JSONArray _calendars = null;

	public Storage() {
		try {
			loadEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			JSONObject calendar = _calendars.getJSONObject(0);
			System.out.println(calendar.getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void loadEvents() throws IOException {
		FileReader input = new FileReader(FILENAME);
		BufferedReader bufRead = new BufferedReader(input);
		String line;
		String data = "";
    int count = 0;

		line = bufRead.readLine();
		count++;

		while (line != null){
			data += line;
			line = bufRead.readLine();
			count++;
		}

		bufRead.close();

		try {
			_calendars = new JSONArray(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getEvent(String year, String month, String day) {

	}

	public static void main(String[] args) {
		Storage store = new Storage();
	}
}
