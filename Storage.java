import java.io.*;
import java.util.ArrayList;

import org.json.*;

public class Storage {
	private static final String FILENAME = "events.json";

	private JSONArray _calendars = null;
	private Event[] _events;

	public Storage() {
		try {
			loadEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			JSONObject calendar = _calendars.getJSONObject(0);
			JSONArray events = calendar.getJSONArray("events");
			_events = new Event[events.length()];

			for (int i = 0; i < events.length(); i++) {
				_events[i] = Event.fromJSONObject(events.getJSONObject(i));
			}

			System.out.println("number of events: " + _events.length);
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

	public void getEvent(int year, int month, int day) {
		Event.Date date = new Event.Date(year, month, day);

		for (Event event : _events) {
			if (event.occursOn(date)) {
				System.out.println(event.toString());
			}
		}
	}

	public static void main(String[] args) {
		Storage store = new Storage();

		store.getEvent(2013, 1, 1);
	}
}
