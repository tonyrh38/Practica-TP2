package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy {

	private int _timeSlot;
	
	public RoundRobinStrategy(int timeSlot) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		// TODO Auto-generated method stub
		return 0;
	}

}
