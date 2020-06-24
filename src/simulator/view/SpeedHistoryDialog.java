package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.model.RoadMap;

public class SpeedHistoryDialog extends JDialog {

	private static final long serialVersionUID = 5967055706437016154L;
	
	private SpeedTableModel _speedTableModel;
	private int _speedLimit;
	
	
	public SpeedHistoryDialog(Controller controller, JFrame parent) {
		super(parent,true);
		_speedTableModel = new SpeedTableModel(controller);
		_speedLimit = 0;
		initGUI();
	}

	
	private void initGUI() {
		setTitle("Vehicles Speed History");
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		setContentPane(mainPane);
		
		// Message Pane
		JLabel message = new JLabel("Select a speed limit and press UPDATE to show the vehicles that exceeded this speed in each tick.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		message.setBorder(new EmptyBorder(10,10,10,10));
		mainPane.add(message);
		
		// Selection Pane
		JPanel speedPane = new JPanel();
		speedPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		mainPane.add(speedPane);
		
		JLabel ticksLabel = new JLabel("Speed Limit: ");
		speedPane.add(ticksLabel);
		
		JSpinner ticksSpinner = new JSpinner(new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1));
		ticksSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_speedLimit = (int)ticksSpinner.getModel().getValue();
			}
		});
		speedPane.add(ticksSpinner);
		
		
		// Buttons Pane
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		mainPane.add(buttonsPane);
		
		// Close Button
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonsPane.add(closeButton);
		
		// Update Button
		JButton okButton = new JButton("Update");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_speedTableModel.setSpeedLimit(_speedLimit);
			}
		});
		buttonsPane.add(okButton);		
		
		// Table View
		JPanel tablePane = new JPanel(new BorderLayout());
		tablePane.setPreferredSize(new Dimension(100, 200));
		mainPane.add(tablePane);
		
		JTable speedTable = new JTable(_speedTableModel);
		speedTable.setFillsViewportHeight(true);
		tablePane.add(speedTable);

		
		pack();
		setVisible(false);
	}
	
	public void show(RoadMap _roadMap, int _time) {
		setVisible(true);
	}

}
