package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = -5541501218567622631L;

	private List<Event> _rowData;
	private static final String[] _columnNames = {"Time", "Desc."};
	
	
	public EventsTableModel(Controller controller) {
		_rowData = new ArrayList<Event>();
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
		switch(columnIndex) {
		case 0: object = _rowData.get(rowIndex).getTime();
			break;
		case 1: object = _rowData.get(rowIndex).toString();
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
		_rowData = events;
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_rowData = events;
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_rowData = events;
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_rowData = events;
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}

}
