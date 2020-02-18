package simulator.model;

public class NewJunctionEvent extends Event {

	private String _id;
	private LightSwitchingStrategy _estrategiaCambioSemaforo;
	private DequeuingStrategy _estrategiaExtraerElementosCola;
	private int _x;
	private int _y;
	
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		_id = id;
		_estrategiaCambioSemaforo = lsStrategy;
		_estrategiaExtraerElementosCola = dqStrategy;
		_x = xCoor;
		_y = yCoor;
	}

	@Override
	void execute(RoadMap map) throws Exception {
		Junction j = new Junction(_id, _estrategiaCambioSemaforo, _estrategiaExtraerElementosCola, _x, _y);
		map.addJunction(j);
	}

}
