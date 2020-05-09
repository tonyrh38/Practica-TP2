package simulator.view;

import java.awt.FlowLayout;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.exceptions.WrongArgumentException;
import simulator.misc.Pair;
import simulator.model.NewSetWeatherEvent;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	private static final long serialVersionUID = -160120575380121879L;
	
	private String _roadID;
	private String _weather;
	private int _ticks;
	
	private int _time;
	
	private Controller _controller;
	
	private DefaultComboBoxModel<String> _roadModel;
	private DefaultComboBoxModel<String> _weatherModel;
	
	
	public ChangeWeatherDialog(Controller controller, JFrame parent) {
		super(parent,true);
		_roadID = "";
		_weather = "";
		_ticks = 1;
		_controller = controller;
		_roadModel = new DefaultComboBoxModel<String>();
		_weatherModel = new DefaultComboBoxModel<String>();
		initGUI();
	}
	
	
	private void initGUI() {
		setTitle("Change Road Weather");
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		setContentPane(mainPane);
		
		// Message Pane
		JLabel message = new JLabel("Schedule an event to change the weather of a road after a given number of simulation ticks from now.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		message.setBorder(new EmptyBorder(10,10,10,10));
		mainPane.add(message);
		
		// Selection Pane
		JPanel selectionPane = new JPanel();
		selectionPane.setLayout(new GridLayout(1,3));
		selectionPane.setBorder(new EmptyBorder(10,10,10,10));
		mainPane.add(selectionPane);
		
		//Road Selection
		JPanel roadPane = new JPanel();
		roadPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		selectionPane.add(roadPane);
		
		JLabel roadLabel = new JLabel("Road: ");
		roadPane.add(roadLabel);
		
		JComboBox<String> roadCB = new JComboBox<String>(_roadModel);
		roadCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_roadID = (String)roadCB.getSelectedItem();
			}	
		});
		roadPane.add(roadCB);
		
		// Weather Selection
		JPanel weatherPane = new JPanel();
		weatherPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		selectionPane.add(weatherPane);
		
		JLabel weatherLabel = new JLabel("Weather: ");
		weatherPane.add(weatherLabel);
		
		JComboBox<String> weatherCB = new JComboBox<String>(_weatherModel);
		weatherCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_weather = (String)weatherCB.getSelectedItem();
			}
		});
		weatherPane.add(weatherCB);
		
		// Ticks Selection
		JPanel ticksPane = new JPanel();
		ticksPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
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
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		mainPane.add(buttonsPane);
		
		// Ok Button
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_roadID != "" & _weather != "") {
					List<Pair<String, Weather>> list = new ArrayList<Pair<String, Weather>>();
					list.add(new Pair<String,Weather>(_roadID,Weather.valueOf(_weather)));
					try {
						_controller.addEvent(new NewSetWeatherEvent(_time + _ticks, list));
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
		_roadModel.removeAllElements();
		for(Road r : roadMap.getRoads()) {
			_roadModel.addElement(r.getId());
		}
		
		_weatherModel.removeAllElements();
		for(Weather w : Weather.values()) {
			_weatherModel.addElement(w.toString());
		}
		
		_time = time;
		setVisible(true);
	}
}
