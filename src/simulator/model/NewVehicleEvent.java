package simulator.model;

import java.util.List;

public class NewVehicleEvent extends Event {

	private String _id;
	private int _maximumSpeed;
	private int _contaminationClass;
	private List<Junction> _itinerary;
	
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(time);
		_id = id;
		_maximumSpeed = maxSpeed;
		_contaminationClass = contClass;
		_itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) throws Exception {
		Vehicle v = new Vehicle(_id, _maximumSpeed, _contaminationClass, _itinerary);
		map.addVehicle(v);
		v.moveToNextRoad();
	}

}
