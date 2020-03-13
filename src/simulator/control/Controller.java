package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimulator;

public class Controller {
	
	TrafficSimulator _simuladorTrafico;
	Factory<Event> _factoriaEventos;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws Exception {
		if(sim == null || eventsFactory == null) throw new Exception("Los datos no son validos");
		else {
			_simuladorTrafico = sim;
			_factoriaEventos = eventsFactory;
		}
	}
	
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray array = jo.getJSONArray("events");
		for(int i = 0; i < array.length(); i++) {
			_simuladorTrafico.addEvent(_factoriaEventos.createInstance(array.getJSONObject(i)));
		}
	}
	
	public void run(int n, OutputStream out) {
		JSONObject json = new JSONObject();
		try {
			for(int i = 0; i < n; i++) {
				_simuladorTrafico.advance();
				json.append("states",_simuladorTrafico.report());
			}
			PrintStream p = new PrintStream(out);
			p.print(json.toString(3));
		} catch (Exception e) {
			System.out.format(e.getMessage() + " %n %n");
		}
	}
	
	public void reset() {
		_simuladorTrafico.reset();
	}
	
}
