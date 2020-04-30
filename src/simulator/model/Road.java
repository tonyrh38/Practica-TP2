package simulator.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.WrongArgumentException;


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
	
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws WrongArgumentException {
		super(id);
		
		if(maxSpeed <= 0) throw new WrongArgumentException("La velocidad máxima debe ser positiva.");
		else _maximumSpeed = maxSpeed;
		if(contLimit < 0) throw new WrongArgumentException("El límite por contaminación no puede ser negativo.");
		else _contaminationAlarmLimit = contLimit;
		if(length <= 0) throw new WrongArgumentException("La longitud de la carretera debe ser positiva.");
		else _length = length;
		if(srcJunc == null || destJunc == null) throw new WrongArgumentException("Los cruces deben existir.");
		else {
			_source = srcJunc;
			_destination = destJunc;
			_source.addOutGoingRoad(this);
			_destination.addIncommingRoad(this);
		}
		if(weather == null) throw new WrongArgumentException("El tiempo debe existir.");
		else _weatherConditions = weather;
		
		_currentSpeedLimit = _maximumSpeed;
		_totalContamination = 0;
		_vehicles = new LinkedList<Vehicle>();
	}

	
	public boolean isGreen() {
		return _destination.isGreen(this);
	}
	
	public Junction getSource() {
		return _source;
	}
	
	public Junction getDestination() {
		return _destination;
	}
	
	public int getLength() {
		return _length;
	}
	
	public int getMaximumSpeed() {
		return _maximumSpeed;
	}
	
	public int getCurrentSpeedLimit() {
		return _currentSpeedLimit;
	}
	
	public int getContaminationAlarmLimit() {
		return _contaminationAlarmLimit;
	}
	
	public Weather getWeatherConditions() {
		return _weatherConditions;
	}
	
	public int getTotalContamination() {
		return _totalContamination;
	}
	
	void enter(Vehicle v) throws WrongArgumentException {
		if(v.getCurrentSpeed() != 0 || v.getLocation() != 0) throw new WrongArgumentException("La velocidad y la localización del vehiculo deben ser 0.");
		else _vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		_vehicles.remove(v);
	}
	
	void setWeather(Weather w) throws WrongArgumentException {
		if(w == null) throw new WrongArgumentException("El tiempo debe existir.");
		else _weatherConditions = w;
	}
	
	void addContamination(int c) throws WrongArgumentException {
		if(c < 0) throw new WrongArgumentException("El límite por contaminación no puede ser negativo.");
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
		if(!_vehicles.isEmpty()) {
			for	(Vehicle v : _vehicles) {
				//a)
				if(!v.isStopped()) {
					try {
						v.setSpeed(calculateVehicleSpeed(v));
					} catch (Exception e) {
						System.out.format(e.getMessage() + " %n %n");
					}
				}
				//b)
				v.advance(time);
			}
			Collections.sort(_vehicles, (v1, v2) -> {return v2.getLocation() - v1.getLocation();});
		}
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("id", _id);
		json.put("speedlimit", _currentSpeedLimit);
		json.put("weather", _weatherConditions.toString());
		json.put("co2", _totalContamination);
		if(!_vehicles.isEmpty()) {
			for(Vehicle v : _vehicles) {
				json.append("vehicles", v.getId());
			}
		}
		return json;
	}

}
