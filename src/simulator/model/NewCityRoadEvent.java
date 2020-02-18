package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent {

	public NewCityRoadEvent(int time, String id, Junction srcJun, Junction destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) throws Exception {
		CityRoad cr = new CityRoad(_id, _source, _destination, _length, _contaminationAlarmLimit, _maximumSpeed, _weatherConditions);
		map.addRoad(cr);
	}

}
