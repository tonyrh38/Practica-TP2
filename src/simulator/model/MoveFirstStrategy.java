package simulator.model;

import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		return q.subList(0, 1);
	}

}
