package simulator.model;

import simulator.exceptions.WrongArgumentException;

public class NewCityRoadEvent extends NewRoadEvent {

	public NewCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) throws WrongArgumentException {
		CityRoad cr = new CityRoad(_id, map.getJunction(_source), map.getJunction(_destination), _maximumSpeed, _contaminationAlarmLimit, _length, _weatherConditions);
		map.addRoad(cr);
	}

	@Override
	public String toString() {
		return "New CityRoad: '" + _id + "'";
	}
	
}
