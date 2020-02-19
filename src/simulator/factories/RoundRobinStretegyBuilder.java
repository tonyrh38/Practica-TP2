package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStretegyBuilder extends Builder<LightSwitchingStrategy>{

	RoundRobinStretegyBuilder(String type) {
		super(type);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		return new RoundRobinStrategy(data.has("timeslot")? data.getInt("timeslot"): 1);
	}

}
