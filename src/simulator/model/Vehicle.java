package simulator.model;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {

	private List<Junction> _itinerary;
	
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
		// Completar
	}
	
	
	void setSpeed(int s) {
		
	}
	
	void setContaminationClass(int c) {
		
	}
	
	@Override
	void advance(int time) {
		// TODO Auto-generated method stub

	}
	
	void moveToNextRoad() {
		
	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
	}

}
