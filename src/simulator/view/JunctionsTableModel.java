package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = -5541501218567622631L;

	private List<Junction> _rowData;
	private static final String[] _columnNames = {"Id", "Green", "Queues"};
	
	
	public JunctionsTableModel(Controller controller) {
		_rowData = new ArrayList<Junction>();
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
		Junction j = _rowData.get(rowIndex);
		switch(columnIndex) {
		case 0: object = j.getId();
			break;
		case 1: object = (j.getRoadInGreen() != null)? j.getRoadInGreen().getId():"NONE";
			break;
		case 2: object = j.getRoadQueues().toString();
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
		_rowData = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_rowData = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_rowData.clear();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_rowData = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}
	
}
