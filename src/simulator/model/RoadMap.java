package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> _listaCruces;
	private List<Road> _listaCarreteras;
	private List<Vehicle> _listaVehiculos;
	
	private Map<String, Junction> _mapaCruces;
	private Map<String, Road> _mapaCarreteras;
	private Map<String, Vehicle> _mapaVehiculos;
	
	
	RoadMap(){
		_listaCruces = new LinkedList<Junction>();
		_listaCarreteras = new LinkedList<Road>();
		_listaVehiculos = new LinkedList<Vehicle>();
		_mapaCruces = new HashMap<String, Junction>();
		_mapaCarreteras = new HashMap<String, Road>();
		_mapaVehiculos = new HashMap<String, Vehicle>();
	}
	
	
	private boolean roadConnects(Junction a, Junction b) {
		boolean connected = false;
		for(int i = 0; i < _listaCarreteras.size() && !connected; i++) {
			if(_listaCarreteras.get(i).getSource().equals(a) && _listaCarreteras.get(i).getDestination().equals(b))
				connected = true;
		}
		return connected;
	}
	
	void addJunction(Junction j) throws Exception {
		if(_mapaCruces.containsKey(j.getId())) throw new Exception("El cruce ya existe");
		else {
			_listaCruces.add(j);
			_mapaCruces.put(j.getId(),j);
		}
	}
	
	void addRoad(Road r) throws Exception {
		if(_mapaCarreteras.containsKey(r.getId()) || !_mapaCruces.containsKey(r.getSource().getId()) || !_mapaCruces.containsKey(r.getDestination().getId()))
			throw new Exception("Compruebe si la carretera o los cruces que une ya existen.");
		else {
			_listaCarreteras.add(r);
			_mapaCarreteras.put(r.getId(), r);
		}
	}
	
	void addVehicle(Vehicle v) throws Exception {
		if(_mapaVehiculos.containsKey(v.getId())) throw new Exception("EL vehiculo ya existe.");
		else {
			List<Junction> it = v.getItinerary();
			boolean connected = true;
			for(int i = 0; i < it.size() - 1 && connected; i++) {
				if(!roadConnects(it.get(i), it.get(i + 1))) connected = false;
			}
			if(!connected) throw new Exception("El itinerario del vehiculo no es valido.");
			else {
				_listaVehiculos.add(v);
				_mapaVehiculos.put(v.getId(), v);
			}
		}
	}
	
	public Junction getJunction(String id) {
		return _mapaCruces.get(id);
	}
	
	public Road getRoad(String id) {
		return _mapaCarreteras.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return _mapaVehiculos.get(id);
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(new ArrayList<>(_listaCruces));
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(new ArrayList<>(_listaCarreteras));
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(new ArrayList<>(_listaVehiculos));
	}
	
	void reset() {
		_listaCruces.clear();
		_listaCarreteras.clear();
		_listaVehiculos.clear();
		_mapaCruces.clear();
		_mapaCarreteras.clear();
		_mapaVehiculos.clear();
	}
	
	public JSONObject report() {
		JSONObject json = new JSONObject();
		for(Junction j : _listaCruces) {
			json.append("junctions", j.report());
		}
		for(Road r : _listaCarreteras) {
			json.append("road", r.report());
		}
		for(Vehicle v : _listaVehiculos) {
			json.append("vehicles", v.report());
		}
		
		return json;
	}
	
}
