package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		List<Pair<String,Integer>> ls = new LinkedList<Pair<String,Integer>>();
		JSONArray info = data.getJSONArray("info");
		for(int i = 0;i < info.length();i++) {
			ls.add(new Pair<String,Integer>(info.getJSONObject(i).getString("vehicle"),info.getJSONObject(i).getInt("class")));
		}
		try {
			return new NewSetContClassEvent(time,ls);
		} catch (Exception e) {
			System.out.format(e.getMessage() + " %n %n");
			e.printStackTrace();
			return null;
		}
	}

}
