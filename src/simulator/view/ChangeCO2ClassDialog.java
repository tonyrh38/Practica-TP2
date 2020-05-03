package simulator.view;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import simulator.control.Controller;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {

	private static final long serialVersionUID = 8309966873726870455L;

	private String _vehicleID;
	private int _CO2Class;
	private int _ticks;
	
	private RoadMap _roadMap;
	private Controller _controller; 
	
	public ChangeCO2ClassDialog(Controller controller, RoadMap roadMap, JFrame parent) {
		super(parent,true);
		_vehicleID = "";
		_CO2Class = 0;
		_ticks = 0;
		_roadMap = roadMap;
		_controller = controller;
		initGUI();
	}
	
	private void initGUI() {
		setTitle("Change CO2 Class");
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		setContentPane(mainPane);
		
		// Message Pane
		JLabel message = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		mainPane.add(message);
		
		// Selection Pane
		JPanel selectionPane = new JPanel();
		selectionPane.setLayout(new GridLayout(1,3));
		mainPane.add(selectionPane);
		
		// Vehicle Selection
		JPanel vehiclePane = new JPanel();
		vehiclePane.setLayout(new BoxLayout(vehiclePane, BoxLayout.LINE_AXIS));
		selectionPane.add(vehiclePane);
		
		JLabel vehicleLabel = new JLabel("Vehicle: ");
		vehiclePane.add(vehicleLabel);
		
		addVehicles(vehiclePane);
		
		// Contamination Class Selection
		JPanel contaminationClassPane = new JPanel();
		contaminationClassPane.setLayout(new BoxLayout(contaminationClassPane, BoxLayout.LINE_AXIS));
		selectionPane.add(contaminationClassPane);
		
		JLabel contaminationClassLabel = new JLabel("CO2 Class: ");
		contaminationClassPane.add(contaminationClassLabel);
		
		addContaminationClass(contaminationClassPane);
		
		// Ticks Selection
		JPanel ticksPane = new JPanel();
		ticksPane.setLayout(new BoxLayout(ticksPane, BoxLayout.LINE_AXIS));
		selectionPane.add(ticksPane);
		
		JLabel ticksLabel = new JLabel("Ticks: ");
		ticksPane.add(ticksLabel);
		
		JSpinner ticksSpinner = new JSpinner();
		ticksPane.add(ticksSpinner);
		
		// Buttons Pane
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new BoxLayout(buttonsPane, BoxLayout.LINE_AXIS));
		mainPane.add(buttonsPane);
		
		// Ok Button
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	
	private void addVehicles(JPanel vehiclePane) {
		Vector<String> vehicles = new Vector<String>();
		for(Vehicle v : _roadMap.getVehicles()) {
			vehicles.add(v.getId());
		}
		 vehiclePane.add(new JComboBox<String>(vehicles));
	}
	
	private void addContaminationClass(JPanel contaminationClassPane) {
		Vector <Integer> contaminationClass = new Vector<Integer>();
		for(int i = 0; i <= Vehicle.maxContaminationClass; i++) {
			contaminationClass.add(i);
		}
		contaminationClassPane.add(new JComboBox<Integer>(contaminationClass));
	}
	
}
