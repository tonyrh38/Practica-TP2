package simulator.view;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;

public class ChangeCO2ClassDialog extends JDialog {

	private static final long serialVersionUID = 8309966873726870455L;

	private String _vehicleID;
	private int _CO2Class;
	private int _ticks;
	private Controller _controller; 
	
	public ChangeCO2ClassDialog(Controller controller, JFrame parent) {
		super(parent,true);
		_vehicleID = "";
		_CO2Class = 0;
		_ticks = 0;
		_controller = controller;
		initGUI();
	}
	
	private void initGUI() {
		setTitle("Change CO2 Class");
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		setContentPane(mainPane);
		
		JLabel message = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		mainPane.add(message);
		
		JPanel selectionPane = new JPanel();
		selectionPane.setLayout(new GridLayout(1,3));
		mainPane.add(selectionPane);
		
		JPanel vehiclePane = new JPanel();
		vehiclePane.setLayout(new BoxLayout(vehiclePane, BoxLayout.LINE_AXIS));
		selectionPane.add(vehiclePane);
		
		JLabel vehicleLabel = new JLabel("Vehicle: ");
		vehiclePane.add(vehicleLabel);
		
		JComboBox<String> vehicles = new JComboBox<String>(getVehiclesID());
		
	}
	
	private String[] getVehiclesID() {
		return _controller.getVehiclesID();		
	}
	
}
