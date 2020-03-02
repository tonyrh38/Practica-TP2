package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		List<Pair<String,Weather>> ls = new LinkedList<Pair<String,Weather>>();
		JSONArray info = data.getJSONArray("info");
		for(int i = 0;i < info.length();i++) {
			ls.add(new Pair<String,Weather>(info.getJSONObject(i).getString("road"), Weather.valueOf(info.getJSONObject(i).getString("weather").toUpperCase())));
		}
		try {
			return new NewSetWeatherEvent(time, ls);
		} catch (Exception e) {
			System.out.format(e.getMessage() + " %n %n");
			return null;
		}
	}

}
