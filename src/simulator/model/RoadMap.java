package simulator.model;

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
	
	
	RoadMap(List<Junction> lcr, List<Road> lca, List<Vehicle> lv, Map<String, Junction> mcr, Map<String, Road> mca, Map<String, Vehicle> mv){
		
	}
	
	
	void addJunction(Junction j) {
		
	}
	
	void addRoad(Road r) {
		
	}
	
	void addVehicle(Vehicle v) {
		
	}
	
	public Junction getJunction(String id) {
		return null;
	}
	
	public Road getRoad(String id) {
		return null;
	}
	
	public Vehicle getVehicle(String id) {
		return null;
	}
	
	public List<Junction> getJunctions(){
		return null;
	}
	
	public List<Road> getRoads(){
		return null;
	}
	
	public List<Vehicle> getVehicles(){
		return null;
	}
	
	void reset() {
		
	}
	
	public JSONObject report() {
		return null;
	}
	
}
