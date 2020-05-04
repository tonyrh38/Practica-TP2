package simulator.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.exceptions.WrongArgumentException;
import simulator.misc.Pair;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {

	private static final long serialVersionUID = 8309966873726870455L;

	private String _vehicleID;
	private int _co2Class;
	private int _ticks;
	
	private int _time;
	
	private Controller _controller; 
	
	private DefaultComboBoxModel<String> _vehicleModel;
	private DefaultComboBoxModel<Integer> _co2ClassModel;
	
	public ChangeCO2ClassDialog(Controller controller, JFrame parent) {
		super(parent,true);
		_vehicleID = "";
		_co2Class = 0;
		_ticks = 0;
		_controller = controller;
		_vehicleModel = new DefaultComboBoxModel<String>();
		_co2ClassModel = new DefaultComboBoxModel<Integer>();
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
		
		JComboBox<String> vehicleCB = new JComboBox<String>(_vehicleModel);
		vehicleCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_vehicleID = (String)vehicleCB.getSelectedItem();
			}	
		});
		vehiclePane.add(vehicleCB);
		
		// Contamination Class Selection
		JPanel co2ClassPane = new JPanel();
		co2ClassPane.setLayout(new BoxLayout(co2ClassPane, BoxLayout.LINE_AXIS));
		selectionPane.add(co2ClassPane);
		
		JLabel co2ClassLabel = new JLabel("CO2 Class: ");
		co2ClassPane.add(co2ClassLabel);
		
		JComboBox<Integer> co2ClassCB = new JComboBox<Integer>(_co2ClassModel);
		co2ClassCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_co2Class = (int)co2ClassCB.getSelectedItem();
			}
		});
		co2ClassPane.add(co2ClassCB);
		
		// Ticks Selection
		JPanel ticksPane = new JPanel();
		ticksPane.setLayout(new BoxLayout(ticksPane, BoxLayout.LINE_AXIS));
		selectionPane.add(ticksPane);
		
		JLabel ticksLabel = new JLabel("Ticks: ");
		ticksPane.add(ticksLabel);
		
		JSpinner ticksSpinner = new JSpinner(new SpinnerNumberModel(1,1,10000,1));
		ticksSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_ticks = (int)ticksSpinner.getModel().getValue();
			}
		});
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
				if(_vehicleID != "" & _ticks != 0) {
					List<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
					list.add(new Pair<String,Integer>(_vehicleID,_co2Class));
					try {
						_controller.addEvent(new NewSetContClassEvent(_time + _ticks, list));
					} catch (WrongArgumentException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		buttonsPane.add(okButton);
		
		// Cancel Button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonsPane.add(cancelButton);
		
		pack();
		setVisible(false);
	}
	
	public void show(RoadMap roadMap, int time) {
		_vehicleModel.removeAllElements();
		for(Vehicle v : roadMap.getVehicles()) {
			_vehicleModel.addElement(v.getId());
		}
		
		_co2ClassModel.removeAllElements();
		for(int i = 0; i <= Vehicle.maxContaminationClass; i++) {
			_co2ClassModel.addElement(i);			
		}
		
		_time = time;
		setVisible(true);
	}
	
}
