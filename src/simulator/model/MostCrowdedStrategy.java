 package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int _timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		_timeSlot = timeSlot;
	}
	
	
	private int findMaxLengthFrom(int index, List<List<Vehicle>> qs) {
		int maxLengthIdx = index;
		for(int i = 0; i < qs.size(); i++) {
			if(qs.get((i + index) % qs.size()).size() > qs.get(maxLengthIdx).size()) maxLengthIdx = (i + index) % qs.size();
		}
		return maxLengthIdx;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		//1)
		if(roads.isEmpty()) return -1;
		//2)
		else if(currGreen == -1) return findMaxLengthFrom(0, qs);
		//3)
		else if(currTime - lastSwitchingTime < _timeSlot) return currGreen;
		//4)
		else return findMaxLengthFrom(currGreen + 1, qs);
	}

}
