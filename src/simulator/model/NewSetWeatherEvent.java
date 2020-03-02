package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetWeatherEvent extends Event {

	private List<Pair<String,Weather>> _weatherList;
	
	
	public NewSetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws Exception {
		super(time);
		if(ws == null) throw new Exception("La lista de pares no es valida.");
		else _weatherList = ws;
	}

	@Override
	void execute(RoadMap map) throws Exception {
		for(Pair<String,Weather> p : _weatherList) {
			if(map.getRoad(p.getFirst()) == null) throw new Exception("La carretera no existe.");
			else map.getRoad(p.getFirst()).setWeather(p.getSecond());
		}
	}

}
