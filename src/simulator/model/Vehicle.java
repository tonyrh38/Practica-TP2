package simulator.model;

import java.util.*;

import org.json.JSONObject;


public class Vehicle extends SimulatedObject {

	private List<Junction> _itinerary;
	private int _lastJunctionIndex;
	
	private int _maximumSpeed;
	private int _currentSpeed;
	
	private VehicleStatus _status;
	
	private Road _road;
	private int _location;
	
	private int _contaminationClass;
	private int _totalContamination;
	
	private int _totalTravelledDistance;
	
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws Exception{
		super(id);
		
		if(maxSpeed <= 0) throw new Exception("La velocidad máxima no puede ser menor o igual que cero.");
		else _maximumSpeed = maxSpeed;
		if(contClass < 0 || contClass > 10) throw new Exception("La clase de contaminación excede los límites (0 a 10).");
		else _contaminationClass = contClass;
		if(itinerary.size() < 2) throw new Exception("El itinerario debe contener al menos dos cruces.");
		else _itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		
		_lastJunctionIndex = 0;
		_currentSpeed = 0;
		_status = VehicleStatus.PENDING;
		_location = 0;
		_totalContamination = 0;
		_totalTravelledDistance = 0;
		
	}
	
	
	void setSpeed(int s) throws Exception {
		if(s < 0) throw new Exception("La velocidad máxima no puede ser menor o igual que cero.");
		else _currentSpeed = (s < _maximumSpeed)? s : _maximumSpeed;
	}
	
	void setContaminationClass(int c) throws Exception {
		if(c < 0 || c > 10) throw new Exception("La clase de contaminación excede los límites (0 a 10).");
		else _contaminationClass = c;
	}
	
	List<Junction> getItinerary(){
		return _itinerary;
	}
	
	int getCurrentSpeed() {
		return _currentSpeed;
	}
	
	int getLocation() {
		return _location;
	}
	
	int getContaminationClass() {
		return _contaminationClass;
	}
	
	@Override
	void advance(int time) {
		if(_status != VehicleStatus.TRAVELING) {
			//a)
			int locAntigua = _location, locActual = _location + _currentSpeed, length = _road.getLength();
			_location = (locActual <= length)? locActual : length;
			_totalTravelledDistance += _location - locAntigua;
			//b)
			int cont = (_location - locAntigua)*_contaminationClass/10;
			_totalContamination += cont;
			try {
				_road.addContamination(cont);
			} catch (Exception e) {
				System.out.format(e.getMessage() + " %n %n");
			}
			//c)
			if(_location == length) {
				_road.getDestination().enter(_road, this);
				_status = VehicleStatus.WAITING;
				_currentSpeed = 0;
			}
		}
	}
	
	void moveToNextRoad() throws Exception {
		if(_status != VehicleStatus.PENDING && _status != VehicleStatus.WAITING) throw new Exception("El vehículo no se encuentra en un cruce.");
		else {
			if(_status == VehicleStatus.WAITING) _road.exit(this);
			if(_lastJunctionIndex + 1 == _itinerary.size()) {
				_status = VehicleStatus.ARRIVED;
				_currentSpeed = 0;
			}
			else {
				_itinerary.get(_lastJunctionIndex).roadTo(_itinerary.get(_lastJunctionIndex + 1)).enter(this);
				_lastJunctionIndex++;
				_status = VehicleStatus.TRAVELING;
				_location = 0;
			}
		}
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("id", _id);
		json.put("speed", _currentSpeed);
		json.put("distance", _totalTravelledDistance);
		json.put("co2", _totalContamination);
		json.put("class", _contaminationClass);
		json.put("status", _status.toString());
		if(_status != VehicleStatus.PENDING || _status != VehicleStatus.ARRIVED) {
			json.put("road", _road.getId());
			json.put("location", _location);
		}
		return json;
	}
	
}
