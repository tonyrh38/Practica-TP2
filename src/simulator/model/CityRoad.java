package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws Exception {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x;
		switch(_weatherConditions) {
		case STORM: x = 10;
			break;
		case WINDY: x = 10;
			break;
		default: x = 2;
			break;
		}
		_totalContamination = (x > _totalContamination)? 0 : _totalContamination - x;
	}

	@Override
	void updateSpeedLimit() {
		_currentSpeedLimit = _maximumSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (int)(((11.0 - v.getContaminationClass())/11.0)*_currentSpeedLimit);
	}
	
}
