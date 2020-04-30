package simulator.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import simulator.exceptions.WrongArgumentException;

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
	
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dsStrategy, int xCoor, int yCoor) throws WrongArgumentException {
		super(id);
		
		if(lsStrategy == null || dsStrategy == null) throw new WrongArgumentException("Las estrategias deben existir.");
		else {
			_estrategiaCambioSemaforo = lsStrategy;
			_estrategiaExtraerElementosCola = dsStrategy;
		}
		if(xCoor <= 0 || yCoor <= 0) throw new WrongArgumentException("Las coordenadas no deben ser negativas.");
		else {
			_x = xCoor;
			_y = yCoor;
		}
		
		_carreterasEntrantes = new LinkedList<Road>();
		_carreterasSalientes = new HashMap<Junction, Road>();
		_colas = new LinkedList<List<Vehicle>>();
		_colaCarretera = new HashMap<Road, List<Vehicle>>();
	}
	
	
	public boolean isGreen(Road road) {
		return _indiceSemaforoVerde != -1 && road.equals(_carreterasEntrantes.get(_indiceSemaforoVerde));
	}
	
	public Road getRoadInGreen() {
		if(_indiceSemaforoVerde != - 1) return _carreterasEntrantes.get(_indiceSemaforoVerde);
		else return null;
	}
	
	public Map<Road, List<Vehicle>> getRoadQueues(){
		return _colaCarretera;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	
	void addIncommingRoad(Road r) throws WrongArgumentException {
		if(r.getDestination() != this) throw new WrongArgumentException("La carretera no conecta a este cruce.");
		else {
			_carreterasEntrantes.add(r);
			LinkedList<Vehicle> nuevaCola = new LinkedList<Vehicle>();
			_colas.add(nuevaCola);
			_colaCarretera.put(r, nuevaCola);			
		}
	}
	
	void addOutGoingRoad(Road r) throws WrongArgumentException {
		if(_carreterasSalientes.containsKey(r.getDestination()) || r.getSource() != this) throw new WrongArgumentException("La carretera no conecta este cruce, o ya existe una que lo hace.");
		else _carreterasSalientes.put(r.getDestination(), r);
	}
	
	void enter(Road r, Vehicle v) {
		_colaCarretera.get(r).add(v);
	}
	
	Road roadTo(Junction j) {
		return _carreterasSalientes.get(j);
	}
	
	@Override
	void advance(int time) {
		//1)
		if(!_colas.isEmpty() && !_colas.get(_indiceSemaforoVerde).isEmpty()) {
			List<Vehicle> moverCola = _estrategiaExtraerElementosCola.dequeue(_colas.get(_indiceSemaforoVerde));
			for(Vehicle v : moverCola) {
				try {
					v.moveToNextRoad();
				} catch (Exception e) {
					System.out.format(e.getMessage() + " %n %n");
				}
			}
			moverCola.clear();
		}
		//2)
		int idx = _estrategiaCambioSemaforo.chooseNextGreen(_carreterasEntrantes, _colas, _indiceSemaforoVerde, _ultimoPasoCambioSemaforo, time);
		if(idx != _indiceSemaforoVerde) {
			_indiceSemaforoVerde = idx;
			_ultimoPasoCambioSemaforo = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("id", _id);
		if(_indiceSemaforoVerde == -1) json.put("green", "none");
		else json.put("green", _carreterasEntrantes.get(_indiceSemaforoVerde).getId());
		int i = 0;
		for(List<Vehicle> lv : _colas) {
			JSONObject obj = new JSONObject();
			obj.put("road", _carreterasEntrantes.get(i).getId());
			if(!lv.isEmpty()) {
				for(Vehicle v : lv) {
					obj.append("vehicles", v.getId());
				}
			}
			json.append("queues", obj);
			i++;
		}
		return json;
	}

}
