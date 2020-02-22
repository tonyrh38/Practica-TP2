package simulator.model;

public abstract class NewRoadEvent extends Event {

	String _id;
	String _source;
	String _destination;
	int _length;
	int _contaminationAlarmLimit;
	int _maximumSpeed;
	Weather _weatherConditions;
	
	
	public NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		_id = id;
		_source = srcJun;
		_destination = destJunc;
		_length = length;
		_contaminationAlarmLimit = co2Limit;
		_maximumSpeed = maxSpeed;
		_weatherConditions = weather;
	}
	
}
