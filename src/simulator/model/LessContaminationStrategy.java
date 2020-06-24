package simulator.model;

import java.util.Collections;
import java.util.List;


public class LessContaminationStrategy implements DequeuingStrategy {
	
	private int _limit;
	
	
	public LessContaminationStrategy(int limit) {
		_limit = limit;
	}
	
	
	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		Collections.sort(q, (v1, v2) -> {return v2.getContaminationClass() - v1.getContaminationClass();});
		if(q.size() < _limit) return q;
		else return q.subList(0, _limit);
	}

}
