package simulator.model;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Junction extends SimulatedObject {

	private List<Road> _carreterasEntrantes;
	private Map<Junction,Road> _carreterasSalientes;
	
	private List<List<Vehicle>> _colas;
	private Map<Road, List<Vehicle>> _colaCarretera;
	
	private int _indiceSemaforoVerde;
	private int _ultimoPasoCambioSemaforo;
	
	private LightSwitchingStrategy _estrategiaCambioSemaforo;
	private DequeuingStrategy _estrategiaExtraerElementosCola;
	
	private int _x;
	private int _y;
	
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dsStrategy, int xCoor, int yCoor) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	

	void addIncommingRoad(Road r) {
		
	}
	
	void addOutGoingRoad(Road r) {
		
	}
	
	void enter(Road r, Vehicle v) {
		
	}
	
	Road roadTo(Junction j) {
		
		return null;
	}
	
	@Override
	void advance(int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
	}

}
