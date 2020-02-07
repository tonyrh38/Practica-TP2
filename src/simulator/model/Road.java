package simulator.model;

import java.util.List;

import org.json.JSONObject;

public abstract class Road extends SimulatedObject {

	private Junction _source;
	private Junction _destination;
	private int _length;
	
	private int _maximumSpeed;
	private int _currentSpeedLimit;
	
	private int _contaminationAlarmLimit;
	private Weather _weatherConditions;
	private int _totalContamination;
	
	private List<Vehicle> vehicles;
	
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int condLimit, int length, Weather weather) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	
	Junction getDestination() {
		return _destination;
	}
	
	int getLength() {
		return _length;
	}
	
	void enter(Vehicle v) {
		
	}
	
	void exit(Vehicle v) {
		
	}
	
	void setWeather(Weather w) {
		
	}
	
	void addContamination(int c) {
		
	}
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	
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
