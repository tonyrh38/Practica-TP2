package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class SpeedTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 7339152444540503871L;

	private List<Pair<Integer,List<Vehicle>>> _rowData;
	private static final String[] _columnNames = {"Ticks","Vehicles"};
	
	private int _limit;
	
	
	public SpeedTableModel(Controller controller) {
		_rowData = new ArrayList<Pair<Integer,List<Vehicle>>>();
		controller.addObserver(this);
		_limit = 0;
	}	
	
	
	// AbstractTableModel Methods
	@Override
	public int getRowCount() {
		return _rowData.size();
	}

	@Override
	public int getColumnCount() {
		return _columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		
		Pair<Integer, List<Vehicle>> p = _rowData.get(rowIndex);
		
		switch(columnIndex) {
		case 0: o = p.getFirst();
			break;
		case 1:
			String str = "[";
			for(Vehicle v: p.getSecond()) {
				if(v.getCurrentSpeed() >= _limit) str += v.getId() + ",";
			}
			str += "]";
			o = str;
			break;
		default: assert(false);
		}
		
		return o;
	}
	
	@Override
	public String getColumnName(int col) {
		if (_columnNames[col] == null) return "";
		else return _columnNames[col];
	}
	
	public void setSpeedLimit(int limit) {
		if(limit >= 0) {
			_limit = limit;
			fireTableDataChanged();
		}	
	}
	
	// TrafficSimObserver Interface Methods
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		Pair<Integer,List<Vehicle>> p = new Pair<Integer,List<Vehicle>>(time,map.getVehicles());
		_rowData.add(p);
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_rowData.clear();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}

}
