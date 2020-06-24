package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.LessContaminationStrategy;

public class LessContaminationStrategyBuilder extends Builder<DequeuingStrategy> {

	LessContaminationStrategyBuilder(String type) {
		super("less_cont_dqs");
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		int limit;
		try {
			limit = data.getInt("limit");
		}
		catch (JSONException e) {
			limit = 1;
		}
		if(limit < 1) throw new JSONException("El limite es incorrecto");
		return new LessContaminationStrategy(limit);
	}

}
