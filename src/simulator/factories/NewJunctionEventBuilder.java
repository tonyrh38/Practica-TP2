package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {

	Factory<LightSwitchingStrategy> _estrategiaCambioSemaforo;
	Factory<DequeuingStrategy> _estrategiaExtraerElementosCola;
	
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		_estrategiaCambioSemaforo = lssFactory;
		_estrategiaExtraerElementosCola = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		JSONArray coor = data.getJSONArray("coor");
		int x = coor.getInt(0);
		int y = coor.getInt(1);
		LightSwitchingStrategy ls_strategy = _estrategiaCambioSemaforo.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dq_strategy = _estrategiaExtraerElementosCola.createInstance(data.getJSONObject("dq_strategy"));
		return new NewJunctionEvent(time, id, ls_strategy, dq_strategy, x, y);
	}

}
