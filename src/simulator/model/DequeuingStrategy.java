package simulator.model;

public interface DequeuingStrategy {
	List<Vehicle> dequeue(List<Vehicle> q);
}
