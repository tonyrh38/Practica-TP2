package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	NewVehicleEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int cl = data.getInt("class");
		JSONArray it = data.getJSONArray("itinerary");
		List<String> itinerary = new LinkedList<String>();
		for(int i = 0; i < it.length();i++) {
			itinerary.add(it.getString(i));
		}		
		return new NewVehicleEvent(time, id, maxSpeed, cl, itinerary);
	}

}
