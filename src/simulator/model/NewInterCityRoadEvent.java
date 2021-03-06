package simulator.model;

import simulator.exceptions.WrongArgumentException;

public class NewInterCityRoadEvent extends NewRoadEvent {

	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) throws WrongArgumentException {
		InterCityRoad icr = new InterCityRoad(_id, map.getJunction(_source), map.getJunction(_destination), _maximumSpeed, _contaminationAlarmLimit, _length, _weatherConditions);
		map.addRoad(icr);
	}

	@Override
	public String toString() {
		return "New InterCityRoad: '" + _id + "'";
	}
	
}
