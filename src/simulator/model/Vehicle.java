package simulator.model;

import java.util.*;

import org.json.JSONObject;

import simulator.exceptions.WrongArgumentException;
import simulator.exceptions.WrongStatusException;


public class Vehicle extends SimulatedObject {

	public static final int maxContaminationClass = 10;
	
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
	
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws WrongArgumentException{
		super(id);
		
		if(maxSpeed <= 0) throw new WrongArgumentException("La velocidad m�xima no puede ser menor o igual que cero.");
		else _maximumSpeed = maxSpeed;
		if(contClass < 0 || contClass > 10) throw new WrongArgumentException("La clase de contaminaci�n excede los l�mites (0 a 10).");
		else _contaminationClass = contClass;
		if(itinerary.size() < 2) throw new WrongArgumentException("El itinerario debe contener al menos dos cruces.");
		else _itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		
		_lastJunctionIndex = 0;
		_currentSpeed = 0;
		_status = VehicleStatus.PENDING;
		_location = 0;
		_totalContamination = 0;
		_totalTravelledDistance = 0;
		
	}
	
	
	boolean isStopped() {
		return _status == VehicleStatus.WAITING || _status == VehicleStatus.ARRIVED;
	}
	
	public List<Junction> getItinerary(){
		return _itinerary;
	}
	
	public int getMaximumSpeed() {
		return _maximumSpeed;
	}
	
	public int getCurrentSpeed() {
		return _currentSpeed;
	}
	
	public VehicleStatus getStatus() {
		return _status;
	}
	
	public Road getRoad() {
		return _road;
	}
	
	public int getLocation() {
		return _location;
	}
	
	public int getContaminationClass() {
		return _contaminationClass;
	}
	
	public int getTotalContamination() {
		return _totalContamination;
	}
	
	public int getTotalTravelledDistance() {
		return _totalTravelledDistance;
	}
	
	void setSpeed(int s) throws WrongArgumentException {
		if(s < 0) throw new WrongArgumentException("La velocidad actual no puede ser menor que cero.");
		else _currentSpeed = (s < _maximumSpeed)? s : _maximumSpeed;
	}
	
	void setContaminationClass(int c) throws WrongArgumentException {
		if(c < 0 || c > maxContaminationClass) throw new WrongArgumentException("La clase de contaminaci�n excede los l�mites (0 a 10).");
		else _contaminationClass = c;
	}
	
	@Override
	void advance(int time) {
		if(_status == VehicleStatus.TRAVELING) {
			//a)
			int locAntigua = _location, locActual = _location + _currentSpeed, length = _road.getLength();
			_location = (locActual <= length)? locActual : length;
			_totalTravelledDistance += _location - locAntigua;
			//b)
			int cont = (_location - locAntigua)*_contaminationClass;
			_totalContamination += cont;
			try {
				_road.addContamination(cont);
			} catch (Exception e) {
				System.out.format(e.getMessage() + " %n %n");
			}
			//c)
			if(_location >= length) {
				_road.getDestination().enter(_road, this);
				_status = VehicleStatus.WAITING;
				_currentSpeed = 0;
			}
		}
	}
	
	// Se deja en "throws Exception" porque puede lanzar tanto WrongArgumentException como WrongStatusException
	void moveToNextRoad() throws Exception {
		if(_status != VehicleStatus.PENDING && _status != VehicleStatus.WAITING) throw new WrongStatusException("El veh�culo no se encuentra en un cruce.");
		else {
			//El vehiculo sale de su carretera en el cruce
			if(_status == VehicleStatus.WAITING) _road.exit(this);
			//Si el vehiculo llega a su destino:
			if(_lastJunctionIndex + 1 >= _itinerary.size()) {
				_status = VehicleStatus.ARRIVED;
				_currentSpeed = 0;
			}
			//Si el vehiculo continua circulando:
			else {
				_road = _itinerary.get(_lastJunctionIndex).roadTo(_itinerary.get(_lastJunctionIndex + 1));
				_location = 0;
				_road.enter(this);
				_lastJunctionIndex++;
				_status = VehicleStatus.TRAVELING;
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
		if(_status != VehicleStatus.PENDING && _status != VehicleStatus.ARRIVED) {
			json.put("road", _road.getId());
			json.put("location", _location);
		}
		return json;
	}
	
}
