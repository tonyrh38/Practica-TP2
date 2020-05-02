package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	private static final long serialVersionUID = -1241524094400052053L;
	
	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	
	private RoadMap _roadMap;
	
	
	public MapByRoadComponent(Controller controller) {
		setPreferredSize(new Dimension(300, 200));
		controller.addObserver(this);
	}
	
	
	private void update(RoadMap map) {
		_roadMap = map;
		repaint();
	}
	
	private void drawRoads(Graphics g) {
		int i = 0;
		int x1 = 50;
		int x2 = getWidth() - 100;
		for(Road r : _roadMap.getRoads()) {
			int y = (i+1) * 50;	
			g.drawLine(x1, y, x2, y);
			
			// Source Junction
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);	
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSource().getId(), x1, y);
			// Destination Junction
			g.setColor((r.isGreen())? _GREEN_LIGHT_COLOR:_RED_LIGHT_COLOR);
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);	
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getDestination().getId(), x2, y);
			
			// Vehicles
			for(Vehicle v : r.getVehicles()) {
				int x = x1 + (int)((x2 - x1) * ((double)v.getLocation() / (double)r.getLength()));
				g.drawImage(loadImage("car.png"), x, y - 8, 16, 16, this);
				g.drawString(v.getId(), x, y - 6);
			}
				
			// ID
			g.drawString(r.getId(), x1/2, y);
			
			// Weather Conditions
			String weather = "";
			switch(r.getWeatherConditions()) {
				case CLOUDY: weather = "cloud";
					break;
				case RAINY: weather = "rain";
					break;
				case STORM: weather = "storm";
					break;
				case SUNNY: weather = "sun";
					break;
				case WINDY: weather = "wind";
					break;
				default:
					break;
			}
			g.drawImage(loadImage(weather + ".png"), x2 + 5, y - 16, 32, 32, this);
			
			// Contamination Class
			int C = (int) Math.floor(Math.min((double) r.getTotalContamination() / (1.0 + (double)r.getContaminationAlarmLimit()),1.0)/ 0.19);
			g.drawImage(loadImage("cont_" + C + ".png"), x2 + 42, y - 16, 32, 32, this);
			
			i++;
		}
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_roadMap == null || _roadMap.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawRoads(g);
		}
	}
	
	// TrafficSimObserver Interface Methods
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {}

}
