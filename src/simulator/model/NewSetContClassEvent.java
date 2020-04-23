package simulator.model;

import java.util.List;

import simulator.exceptions.WrongArgumentException;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {

	private List<Pair<String,Integer>> _contaminationClassList;
	
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) throws WrongArgumentException {
		super(time);
		if (cs == null) throw new WrongArgumentException("La lista de pares no es valida");
		else _contaminationClassList = cs;
	}

	@Override
	void execute(RoadMap map) throws WrongArgumentException {
		for(Pair<String,Integer> cc : _contaminationClassList) {
			if(map.getVehicle(cc.getFirst()) == null) throw new WrongArgumentException("El vehiculo no existe");
			else map.getVehicle(cc.getFirst()).setContaminationClass(cc.getSecond());
		}
	}
	
	@Override
	public String toString() {
		return "New ContaminationClass Setted";
	}
	
}
