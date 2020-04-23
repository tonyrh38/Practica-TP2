package simulator.model;

public abstract class NewRoadEvent extends Event {
	
	//Los atributos se dejan en protected para que las clases hijas puedan acceder a ellos
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
	
	@Override
	public String toString() {
		return "New Road: '" + _id + "'";
	}
}
