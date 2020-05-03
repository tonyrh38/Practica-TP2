package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = -2074292342943475326L;

	private List<Vehicle> _rowData;
	private static final String[] _columnNames = {"Id", "Status", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance"};
	
	
	public VehiclesTableModel(Controller controller) {
		_rowData = new ArrayList<Vehicle>();
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
		Vehicle v = _rowData.get(rowIndex);
		switch(columnIndex) {
			case 0: object = v.getId();
			case 1: 
				switch(v.getStatus()) {
					case ARRIVED: object = "Arrived";
						break;
					case PENDING: object = "Pending";
						break;
					case TRAVELING: object = v.getRoad() + ": " + v.getLocation();
						break;
					case WAITING: object = "Waiting: " + v.getRoad().getDestination();
						break;
					default:
						break;
				}
			case 2: object = v.getItinerary().toString();
			case 3: object = v.getContaminationClass();
			case 4: object = v.getMaximumSpeed();
			case 5: object = v.getCurrentSpeed();
			case 6: object = v.getTotalContamination();
			case 7: object = v.getTotalTravelledDistance();
			default: assert(false);
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
		_rowData = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_rowData.clear();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}

}
