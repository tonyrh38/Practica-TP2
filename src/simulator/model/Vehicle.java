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
	
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary){
		super(id);
		
		if(maxSpeed <= 0) throw new speedBelowZeroException();
		else _maximumSpeed = maxSpeed;
		if(contClass < 0 || contClass > 10) throw new contClassOffLimitsException();
		else _contaminationClass = contClass;
		if(itinerary.size() < 2) throw new tooShortLengthItineraryException();
		else _itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		
		_lastJunctionIndex = 0;
		_currentSpeed = 0;
		_status = VehicleStatus.PENDING;
		_location = 0;
		_totalContamination = 0;
		_totalTravelledDistance = 0;
		
	}
	
	
	void setSpeed(int s) {
		if(s < 0) throw new speedBelowZeroException();
		else _currentSpeed = (s < _maximumSpeed)? s : _maximumSpeed;
	}
	
	void setContaminationClass(int c) {
		if(c < 0 || c > 10) throw new contClassOffLimitsException();
		else _contaminationClass = c;
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
			_road.addContamination(cont);
			//c)
			if(_location == length) {
				_road.getDestination().enter(_road, this);
				_status = VehicleStatus.WAITING;
				_currentSpeed = 0;
			}
		}
	}
	
	void moveToNextRoad() {
		if(_status != VehicleStatus.PENDING || _status != VehicleStatus.WAITING) throw new invalidMoveException();
		else if(_status == VehicleStatus.PENDING) {
			_itinerary.get(0).
		}
		else {
			
		}
		_location = 0;
	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
	}

}
