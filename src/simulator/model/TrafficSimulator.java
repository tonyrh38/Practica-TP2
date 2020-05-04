package simulator.model;

import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	
	private RoadMap _mapaCarreteras;
	private List<Event> _listaEventos;
	private int _time;
	
	private List<TrafficSimObserver> _observers;

	
	public TrafficSimulator() {
		_mapaCarreteras = new RoadMap();
		_listaEventos = new SortedArrayList<Event>();
		_time = 0;
	}

	
	// Observable Interface Methods
	@Override
	public void addObserver(TrafficSimObserver o) {
		_observers.add(o);
		for(TrafficSimObserver ob : _observers) {
			ob.onRegister(_mapaCarreteras, _listaEventos, _time);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		_observers.remove(o);
	}
	
	public void addEvent(Event e) {
		_listaEventos.add(e);
		for(TrafficSimObserver o : _observers) {
			o.onEventAdded(_mapaCarreteras, _listaEventos, e, _time);
		}
	}
	
	public void advance() throws Exception {
		try {
			//1)
			_time++;
			//1.5)
			for(TrafficSimObserver o : _observers) {
				o.onAdvanceStart(_mapaCarreteras, _listaEventos, _time);
			}
			//2) Tengo que hacerlo así porque si lo hago con (Event e : _listaEventos) salta excepcion.
			for(int i = 0; i < _listaEventos.size();) {
				Event e = _listaEventos.get(i);
				if(e.getTime() == _time) {
					e.execute(_mapaCarreteras);
					_listaEventos.remove(i);
				}
				else i++;
			}
			//3)
			for(Junction j : _mapaCarreteras.getJunctions()) {
				j.advance(_time);
			}
			//4)
			for(Road r: _mapaCarreteras.getRoads()) {
				r.advance(_time);
			}
			//4.5)
			for(TrafficSimObserver o : _observers) {
				o.onAdvanceEnd(_mapaCarreteras, _listaEventos, _time);
			}
		}
		catch (Exception e) {
			for(TrafficSimObserver o : _observers) {
				o.onError(e.getMessage());
			}
			throw e;
		}
	}
	
	public void reset() {
		_mapaCarreteras.reset();
		_listaEventos.clear();
		_time = 0;
		for(TrafficSimObserver o : _observers) {
			o.onReset(_mapaCarreteras, _listaEventos, _time);
		}
	}
	
	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("time", _time);
		json.put("state", _mapaCarreteras.report());
		
		return json;
	}
	
}
