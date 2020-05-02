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
	private RoadMap _roadMap;
	private Controller _controller;
	
	public ControlPanel(Controller controller) {
		_fc = new JFileChooser();
		_controller = controller;
		initGUI();
	}
	
	
	private void initGUI() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		//TODO: Revisar si se necesitan los botones como atributos privados
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
		
		JButton changeCO2 = new JButton();
		changeCO2.setToolTipText("Change CO2 Class");
		changeCO2.addActionListener(new  ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangeCO2ClassDialog(_controller, _roadMap, (JFrame)getParent());
			}
		});
		changeCO2.setIcon(new ImageIcon("resources/icons/co2class.png"));
		add(changeCO2);
	}
	
	// TrafficSimObserver Interface Methods
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_roadMap = map;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_roadMap = map;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_roadMap = null;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_roadMap = map;
	}

	@Override
	public void onError(String err) {}

}
