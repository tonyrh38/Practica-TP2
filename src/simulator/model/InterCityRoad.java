package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws Exception {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x;
		switch (_weatherConditions) {
		case CLOUDY: x = 3;
			break;
		case RAINY: x = 10;
			break;
		case STORM: x = 20;
			break;
		case SUNNY: x = 2;
			break;
		case WINDY: x = 15;
			break;
		default: x = 0;
			break;
		}
		_totalContamination = (int)(((100.0 - x)/100.0)*_totalContamination);		
	}

	@Override
	void updateSpeedLimit() {
		_currentSpeedLimit = (_maximumSpeed > _contaminationAlarmLimit)?(int)(_maximumSpeed*0.5):_maximumSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (_weatherConditions == Weather.STORM)?(int)(_currentSpeedLimit*0.8):_currentSpeedLimit;
	}

}
