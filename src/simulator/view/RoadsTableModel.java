package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = -5541501218567622631L;

	private List<Road> _rowData;
	private static final String[] _columnNames = {"Id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit"};
	
	
	public RoadsTableModel(Controller controller) {
		_rowData = new ArrayList<Road>();
		controller.addObserver(this);
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
		Object object = null;
		Road r = _rowData.get(rowIndex);
		switch(columnIndex) {
		case 0: object = r.getId();
			break;
		case 1: object = r.getLength();
			break;
		case 2: object = r.getWeatherConditions().toString();
			break;
		case 3: object = r.getMaximumSpeed();
			break;
		case 4: object = r.getCurrentSpeedLimit();
			break;
		case 5: object = r.getTotalContamination();
			break;
		case 6: object = r.getContaminationAlarmLimit();
			break;
		default: assert(false);
			break;
		}
		return object;
	}

	@Override
	public String getColumnName(int col) {
		if (_columnNames[col] == null) return "";
		else return _columnNames[col];
	}

	// TrafficSimObserver Interface Methods
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_rowData = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_rowData = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_rowData = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_rowData = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}
	
}
