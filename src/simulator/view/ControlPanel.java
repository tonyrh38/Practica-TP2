package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = -4423199850333010661L;
	
	private JToolBar _toolBar;
	
	private JFileChooser _fc;
	private ChangeCO2ClassDialog _co2Class;
	private ChangeWeatherDialog _weather;
	
	private int _ticks;
	
	private boolean _stopped;
	
	private RoadMap _roadMap;
	private int _time;
	
	private Controller _controller;
	
	public ControlPanel(Controller controller) {
		_toolBar = new JToolBar();
		_fc = new JFileChooser();
		_co2Class = new ChangeCO2ClassDialog(controller, (JFrame)getParent());
		_weather = new ChangeWeatherDialog(controller, (JFrame)getParent());
		_ticks = 1;
		_stopped = true;
		_controller = controller;
		_controller.addObserver(this);
		initGUI();
	}
	
	
	private void initGUI() {
		setLayout(new BorderLayout());
		
		_toolBar.setBorder(new EmptyBorder(5,5,5,5));
		_toolBar.setFloatable(false);
		add(_toolBar);
		
		// Load Button
		JButton load = new JButton();
		load.setToolTipText("Load a file");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_stopped) {
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
			}	
		});
		load.setIcon(new ImageIcon("resources/icons/open.png"));
		_toolBar.add(load);
		
		_toolBar.addSeparator();
		
		// Change CO2 Button
		JButton changeCO2 = new JButton();
		changeCO2.setToolTipText("Change CO2 Class");
		changeCO2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_stopped) _co2Class.show(_roadMap, _time);
			} 
		});
		changeCO2.setIcon(new ImageIcon("resources/icons/co2class.png"));
		_toolBar.add(changeCO2);
		
		// Change Weather Button
		JButton changeWeather = new JButton();
		changeWeather.setToolTipText("Change Weather Conditions");
		changeWeather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_stopped) _weather.show(_roadMap, _time);
			}
		});
		changeWeather.setIcon(new ImageIcon("resources/icons/weather.png"));
		_toolBar.add(changeWeather);
		
		_toolBar.addSeparator();
		
		// Run Button
		JButton runButton = new JButton();
		runButton.setToolTipText("Runs the simulator");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_stopped) {
					_stopped = false;
					run_sim(_ticks);
				}
			}
		});
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_toolBar.add(runButton);
		
		// Stop Button
		JButton stopButton = new JButton();
		stopButton.setToolTipText("Stops the simulator");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = true;
			}
		});
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_toolBar.add(stopButton);
		
		// Tick Selection
		JSpinner ticksSpinner = new JSpinner(new SpinnerNumberModel(1,1,10000,1));
		ticksSpinner.setMaximumSize(new Dimension(32,48));
		ticksSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_ticks = (int)ticksSpinner.getModel().getValue();
			}
		});
		_toolBar.add(ticksSpinner);
		
		_toolBar.add(Box.createGlue());
		_toolBar.addSeparator();
		
		// Quit Button
		JButton quitButton = new JButton();
		quitButton.setToolTipText("Quit the program");
		quitButton.setHorizontalAlignment(SwingConstants.RIGHT);
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_stopped) {
					int option = JOptionPane.showOptionDialog(getParent(), "¿Desea salir?", "Traffic Simulator", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					if(option == JOptionPane.OK_OPTION) System.exit(0);
				}
			}
		});
		quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_toolBar.add(quitButton);
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
					try {
						Thread.sleep(250);
						run_sim( n - 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
			});
		} 
		else {
			_stopped = true ;
		}
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
