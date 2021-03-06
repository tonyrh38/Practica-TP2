package simulator.view;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = -7853057030893328962L;

	private JLabel _time;
	private JLabel _message;
	private Controller _controller;
	
	
	public StatusBar(Controller controller) {
		_time = new JLabel("Time: " + Integer.toString(0));
		_message = new JLabel("Event: ");
		_controller = controller;
		_controller.addObserver(this);
		initGUI();
	}
	
	
	private void initGUI() {
		setLayout(new GridLayout(1,2));
		setBorder(new EmptyBorder(0, 5, 5, 0));
		add(_time);
		add(_message);	
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_time.setText("Time: " + Integer.toString(time));
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_message.setText("Event: " + e.toString());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_time.setText("Time: 0");
		_message.setText("");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}

}
