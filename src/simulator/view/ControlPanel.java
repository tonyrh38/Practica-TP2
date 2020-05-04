package simulator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = -4423199850333010661L;
	
	private JFileChooser _fc;
	private ChangeCO2ClassDialog _co2Class;
	private ChangeWeatherDialog _weather;
	
	private RoadMap _roadMap;
	private int _time;
	
	private Controller _controller;
	
	public ControlPanel(Controller controller) {
		_fc = new JFileChooser();
		_co2Class = new ChangeCO2ClassDialog(_controller, (JFrame)getParent());
		_controller = controller;
		_controller.addObserver(this);
		initGUI();
	}
	
	
	private void initGUI() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		// Load Button
		JButton load = new JButton();
		load.setToolTipText("Load a file");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int option = _fc.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION) {
					_controller.reset();
					try {
						_controller.loadEvents(new FileInputStream(_fc.getSelectedFile()));
					} 
					catch(Exception ex) {
						JOptionPane.showMessageDialog(null, "El archivo no es válido", "", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}	
		});
		load.setIcon(new ImageIcon("resources/icons/open.png"));
		add(load);
		
		// Change CO2 Button
		JButton changeCO2 = new JButton();
		changeCO2.setToolTipText("Change CO2 Class");
		changeCO2.addActionListener(new  ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_co2Class.show(_roadMap, _time);
			} 
		});
		changeCO2.setIcon(new ImageIcon("resources/icons/co2class.png"));
		add(changeCO2);
		
		// Change Weather Button
		JButton changeWeather = new JButton();
		changeWeather.setToolTipText("Change Weather Conditions");
		changeWeather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_weather.show(_roadMap, _time);
			}
		});
		add(changeWeather);
		
		// Run/Stop Simulation Pane
		JPanel runPane = new JPanel();
		runPane.setLayout(new BoxLayout(runPane, BoxLayout.LINE_AXIS));
		add(runPane);
		
		// Run Button
		JButton runButton = new JButton();
		
		
		
	}
	
	// TrafficSimObserver Interface Methods
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_roadMap = map;
		_time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_roadMap = map;
		_time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_roadMap = map;
		_time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_roadMap = map;
		_time = time;
	}

	@Override
	public void onError(String err) {}

}
