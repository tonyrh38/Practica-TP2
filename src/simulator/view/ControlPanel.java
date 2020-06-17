package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.Box;
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
	
	private JButton _load;
	private JButton _changeCO2;
	private JButton _changeWeather;
	private JButton _runButton;
	private JButton _stopButton;
	private JSpinner _ticksSpinner;
	private JButton _quitButton;
	
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
		_load = new JButton();
		_load.setToolTipText("Load a file");
		_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int option = _fc.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION) {
					_controller.reset();
					try {
						_controller.loadEvents(new FileInputStream(_fc.getSelectedFile()));
					} 
					catch(Exception ex) {
						JOptionPane.showMessageDialog(null, "El archivo no es valido", "", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}	
		});
		_load.setIcon(new ImageIcon("resources/icons/open.png"));
		_toolBar.add(_load);
		
		_toolBar.addSeparator();
		
		// Change CO2 Button
		_changeCO2 = new JButton();
		_changeCO2.setToolTipText("Change CO2 Class");
		_changeCO2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_co2Class.show(_roadMap, _time);
			} 
		});
		_changeCO2.setIcon(new ImageIcon("resources/icons/co2class.png"));
		_toolBar.add(_changeCO2);
		
		// Change Weather Button
		_changeWeather = new JButton();
		_changeWeather.setToolTipText("Change Weather Conditions");
		_changeWeather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_weather.show(_roadMap, _time);
			}
		});
		_changeWeather.setIcon(new ImageIcon("resources/icons/weather.png"));
		_toolBar.add(_changeWeather);
		
		_toolBar.addSeparator();
		
		// Run Button
		_runButton = new JButton();
		_runButton.setToolTipText("Runs the simulator");
		_runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					_stopped = false;
					enableToolBar(false);
					run_sim(_ticks);
			}
		});
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_toolBar.add(_runButton);
		
		// Stop Button
		_stopButton = new JButton();
		_stopButton.setToolTipText("Stops the simulator");
		_stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = true;
			}
		});
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_toolBar.add(_stopButton);
		
		// Tick Selection
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(1,1,10000,1));
		_ticksSpinner.setMaximumSize(new Dimension(32,48));
		_ticksSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_ticks = (int)_ticksSpinner.getModel().getValue();
			}
		});
		_toolBar.add(_ticksSpinner);
		
		_toolBar.add(Box.createGlue());
		_toolBar.addSeparator();
		
		// Quit Button
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit the program");
		_quitButton.setHorizontalAlignment(SwingConstants.RIGHT);
		_quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					int option = JOptionPane.showOptionDialog(getParent(), "¿Desea salir?", "Traffic Simulator", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					if(option == JOptionPane.OK_OPTION) System.exit(0);
			}
		});
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_toolBar.add(_quitButton);
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
			enableToolBar(true);
			_stopped = true;
		}
	}
	
	private void enableToolBar(boolean enable) {
		_load.setEnabled(enable);
		_changeCO2.setEnabled(enable);
		_changeWeather.setEnabled(enable);
		_runButton.setEnabled(enable);
		_ticksSpinner.setEnabled(enable);
		_quitButton.setEnabled(enable);
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
