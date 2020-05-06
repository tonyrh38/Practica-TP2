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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = -4423199850333010661L;
	
	private JFileChooser _fc;
	private ChangeCO2ClassDialog _co2Class;
	private ChangeWeatherDialog _weather;
	
	private int _ticks;
	
	private boolean _stopped;
	
	private RoadMap _roadMap;
	private int _time;
	
	private Controller _controller;
	
	public ControlPanel(Controller controller) {
		_fc = new JFileChooser();
		_co2Class = new ChangeCO2ClassDialog(_controller, (JFrame)getParent());
		_weather = new ChangeWeatherDialog(_controller, (JFrame)getParent());
		_ticks = 0;
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
		changeWeather.setIcon(new ImageIcon("resources/icons/weather.png"));
		add(changeWeather);
		
		// Run/Stop Simulation Pane
		JPanel runPane = new JPanel();
		runPane.setLayout(new BoxLayout(runPane, BoxLayout.LINE_AXIS));
		add(runPane);
		
		// Run Button
		JButton runButton = new JButton();
		runButton.setToolTipText("Runs the simulator");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run_sim(_ticks);
			}
		});
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runPane.add(runButton);
		
		// Stop Button
		JButton stopButton = new JButton();
		stopButton.setToolTipText("Stops the simulator");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		runPane.add(stopButton);
		
		// Tick Selection
		JSpinner ticksSpinner = new JSpinner(new SpinnerNumberModel(10,1,10000,1));
		ticksSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_ticks = (int)ticksSpinner.getModel().getValue();
			}
		});
		runPane.add(ticksSpinner);
		
		// Quit Button
		JButton quitButton = new JButton();
		quitButton.setHorizontalAlignment(SwingConstants.RIGHT);
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(getParent(), "¿Está seguro de querer salir?") == JOptionPane.OK_OPTION) System.exit(0);
			}
		});
		quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		add(quitButton);
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_controller.run(1,null);
			} catch (Exception e ) {
				JOptionPane.showMessageDialog(getParent(), "Error en la ejecución del simulador", "", JOptionPane.ERROR_MESSAGE);
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim( n - 1);
				}
			});
		} else {
			//enableToolBar( true );
			_stopped = true ;
		}
	}
	
	private void stop() {
		_stopped = true ;
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
