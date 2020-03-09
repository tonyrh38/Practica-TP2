package simulator.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;


public abstract class Road extends SimulatedObject {

	private Junction _source;
	private Junction _destination;
	private int _length;
	
	protected int _maximumSpeed;
	protected int _currentSpeedLimit;
	
	protected int _contaminationAlarmLimit;
	protected Weather _weatherConditions;
	protected int _totalContamination;
	
	private List<Vehicle> _vehicles;
	
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws Exception {
		super(id);
		
		if(maxSpeed <= 0) throw new Exception("La velocidad máxima no puede ser menor o igual que cero.");
		else _maximumSpeed = maxSpeed;
		if(contLimit < 0) throw new Exception("El límite por contaminación no puede ser menor que cero.");
		else _contaminationAlarmLimit = contLimit;
		if(length <= 0) throw new Exception("La longitud de la carretera debe ser mayor que cero.");
		else _length = length;
		if(srcJunc == null || destJunc == null) throw new Exception("Los cruces deben existir.");
		else {
			_source = srcJunc;
			_destination = destJunc;
			_source.addOutGoingRoad(this);
			_destination.addIncommingRoad(this);
		}
		if(weather == null) throw new Exception("El tiempo debe tener un valor válido");
		else _weatherConditions = weather;
		
		_currentSpeedLimit = _maximumSpeed;
		_totalContamination = 0;
		_vehicles = new LinkedList<Vehicle>();
	}

	
	Junction getSource() {
		return _source;
	}
	
	Junction getDestination() {
		return _destination;
	}
	
	int getLength() {
		return _length;
	}
	
	void enter(Vehicle v) throws Exception {
		if(v.getCurrentSpeed() != 0 || v.getLocation() != 0) throw new Exception("La velocidad y la localización del vehiculo deben ser 0.");
		else _vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		_vehicles.remove(v);
	}
	
	void setWeather(Weather w) throws Exception {
		if(w == null) throw new Exception("El tiempo debe tener un valor válido");
		else _weatherConditions = w;
	}
	
	void addContamination(int c) throws Exception {
		if(c < 0) throw new Exception("El límite por contaminación no puede ser menor que cero.");
		else _totalContamination += c;
	}
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	
	@Override
	void advance(int time) {
		//1)
		reduceTotalContamination();
		//2)
		updateSpeedLimit();
		//3)
		for	(Vehicle v : _vehicles) {
			//a)
			try {
				v.setSpeed(calculateVehicleSpeed(v));
			} catch (Exception e) {
				System.out.format(e.getMessage() + " %n %n");
			}
			//b)
			v.advance(time);
		}
		Collections.sort(_vehicles, (v1, v2) -> {return v2.getLocation() - v1.getLocation();});
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("id", _id);
		json.put("speedlimit", _currentSpeedLimit);
		json.put("weather", _weatherConditions.name());
		json.put("co2", _totalContamination);
		for(Vehicle v : _vehicles) {
			json.append("vehicles", v.getId());
		}
		return json;
	}

}
