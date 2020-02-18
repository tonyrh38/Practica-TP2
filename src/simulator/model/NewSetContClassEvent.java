package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {

	private List<Pair<String,Integer>> _contaminationClassList;
	
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) throws Exception {
		super(time);
		if (cs == null) throw new Exception("La lista de pares no es valida");
		else _contaminationClassList = cs;
	}

	@Override
	void execute(RoadMap map) throws Exception {
		for(Pair<String,Integer> cc : _contaminationClassList) {
			if(map.getVehicle(cc.getFirst()) == null) throw new Exception("El vehiculo no existe");
			else map.getVehicle(cc.getFirst()).setContaminationClass(cc.getSecond());
		}
	}

}
