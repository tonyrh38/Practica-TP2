package simulator.model;

import java.util.LinkedList;
import java.util.List;


public class NewVehicleEvent extends Event {

	private String _id;
	private int _maximumSpeed;
	private int _contaminationClass;
	private List<String> _itinerary;
	
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		_id = id;
		_maximumSpeed = maxSpeed;
		_contaminationClass = contClass;
		_itinerary = itinerary;
	}

	@Override
	// Se deja en "throws Exception" porque puede lanzar tanto WrongArgumentException como WrongStatusException desde el metodo "v.moveToNextRoad()"
	void execute(RoadMap map) throws Exception {
		List<Junction> itinerary = new LinkedList<Junction>();
		for(String v : _itinerary) {
			itinerary.add(map.getJunction(v));
		}
		Vehicle v = new Vehicle(_id, _maximumSpeed, _contaminationClass, itinerary);
		map.addVehicle(v);
		v.moveToNextRoad();
	}

}
