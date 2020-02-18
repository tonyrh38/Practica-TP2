package simulator.model;

import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	
	private RoadMap _mapaCarreteras;
	private List<Event> _listaEventos;
	private int _time;

	
	public TrafficSimulator() {
		_mapaCarreteras = new RoadMap();
		_listaEventos = new SortedArrayList<Event>();
		_time = 0;
	}

	
	public void addEvent(Event e) {
		_listaEventos.add(e);
	}
	
	public void advance() throws Exception {
		//1)
		_time++;
		//2)
		for(Event e : _listaEventos) {
			if(e.getTime() == _time) {
				e.execute(_mapaCarreteras);
				_listaEventos.remove(e);
			}
		}
		//3)
		for(Junction j : _mapaCarreteras.getJunctions()) {
			j.advance(_time);
		}
		//4)
		for(Road r: _mapaCarreteras.getRoads()) {
			r.advance(_time);
		}
	}
	
	public void reset() {
		_mapaCarreteras.reset();
		_listaEventos.clear();
		_time = 0;
	}
	
	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("time", _time);
		json.put("state", _mapaCarreteras.report());
		
		return json;
	}
	
}
