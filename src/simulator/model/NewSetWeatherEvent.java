package simulator.model;

import java.util.List;

import simulator.exceptions.WrongArgumentException;
import simulator.misc.Pair;

public class NewSetWeatherEvent extends Event {

	private List<Pair<String,Weather>> _weatherList;
	
	
	public NewSetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws WrongArgumentException {
		super(time);
		if(ws == null) throw new WrongArgumentException("La lista de pares no es valida.");
		else _weatherList = ws;
	}

	@Override
	void execute(RoadMap map) throws WrongArgumentException {
		for(Pair<String,Weather> p : _weatherList) {
			if(map.getRoad(p.getFirst()) == null) throw new WrongArgumentException("La carretera no existe.");
			else map.getRoad(p.getFirst()).setWeather(p.getSecond());
		}
	}

	@Override
	public String toString() {
		String message = "Change Weather: [";
		for(Pair<String, Weather> p : _weatherList) {
			message += "("+ p.getFirst() +","+ p.getSecond().toString() +")";
		}
		return  message + "]";
	}
	
}
