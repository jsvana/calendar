import org.json.*;

public class Event {
	public String name;
	public Date begin;
	public boolean repeat = false;
	public int[] days;
	public boolean allDay = false;
	public Time beginTime;
	public Time endTime;
	public Date end;
	public Date[] except;

	public static class Date {
		public int year;
		public int month;
		public int day;
		public static final String[] MONTHS = new String[] {"January", "February",
			"March", "April", "May", "June", "July", "August", "September", "October",
			"November", "December"};

		public Date(int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;
		}

		@Override
		public boolean equals(Object other){
			if (other == null) {
				return false;
			}

			if (other == this) {
				return true;
			}

			if (!(other instanceof Date)) {
				return false;
			}

			Date date = (Date)other;
			return this.year == date.year && this.month == date.month
				&& this.day == date.day;
		}

		@Override
		public String toString() {
			return this.day + " " + MONTHS[this.month] + " " + this.year;
		}
	}

	public static class Time {
		public int hour;
		public int minute;

		public Time(int hour, int minute) {
			this.hour = hour;
			this.minute = minute;
		}

		@Override
		public boolean equals(Object other){
			if (other == null) {
				return false;
			}

			if (other == this) {
				return true;
			}

			if (!(other instanceof Time)) {
				return false;
			}

			Time time = (Time)other;

			return this.hour == time.hour && this.minute == time.minute;
		}

		@Override
		public String toString() {
			return this.hour + ":" + this.minute;
		}
	}

	public boolean occursOn(Date date) {
		if (this.begin.equals(date)) {
			return true;
		}

		if (this.repeat) {
			if (this.end.equals(date)) {
				return true;
			}

			// Determine if it happens on a repeated day
		} else {
			return false;
		}
	}

	public static Event fromJSONObject(JSONObject json) {
		try {
			int i;

			Event event = new Event();

			event.name = json.getString("name");

			JSONObject begin = json.getJSONObject("begin");
			event.begin = new Event.Date(begin.getInt("year"), begin.getInt("month"),
				begin.getInt("day"));

			event.repeat = json.getBoolean("repeat");

			if (event.repeat) {
				JSONArray days = json.getJSONArray("days");
				event.days = new int[days.length()];
				for (i = 0; i < days.length(); i++) {
					event.days[i] = days.getInt(i);
				}

				JSONObject end = json.getJSONObject("end");
				event.end = new Event.Date(end.getInt("year"), end.getInt("month"),
					end.getInt("day"));

				JSONArray except = json.getJSONArray("except");
				event.except = new Event.Date[except.length()];
				for (i = 0; i < except.length(); i++) {
					JSONObject next = except.getJSONObject(i);
					event.except[i] = new Event.Date(next.getInt("year"),
						next.getInt("month"), next.getInt("day"));
				}
			}

			event.allDay = json.getBoolean("day-long");

			if (!event.allDay) {
				JSONObject beginTime = json.getJSONObject("time").getJSONObject("begin");
				boolean hasEnd = json.getJSONObject("time").getBoolean("has-end");
				event.beginTime = new Event.Time(beginTime.getInt("hour"),
					beginTime.getInt("minute"));

				if (hasEnd) {
					JSONObject endTime = json.getJSONObject("time").getJSONObject("end");
					event.endTime = new Event.Time(endTime.getInt("hour"),
						endTime.getInt("minute"));
				}
			}

			return event;
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}
}
