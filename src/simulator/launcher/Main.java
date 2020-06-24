package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static int _ticks = 0;
	private static String _mode;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			if(_mode != "gui") parseOutFileOption(line);
			parseTicksOption(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator’s main loop (default value is 10)").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Choose between GUI or Console view").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && _mode != "gui") {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
		if(_outFile == null) throw new ParseException("An output file is missing");
	}
	
	private static void parseTicksOption(CommandLine line) throws ParseException {
		_ticks = (line.getOptionValue("t") != null)? Integer.parseInt(line.getOptionValue("t")) : _timeLimitDefaultValue;
	}

	private static void parseModeOption(CommandLine line) throws ParseException {
		_mode = line.getOptionValue("m");
		if(_mode == null) _mode = "gui";
		else if (!_mode.equalsIgnoreCase("gui") && !_mode.equalsIgnoreCase("console")) throw new ParseException("Invalid mode selected");
	}
	
	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder());
		dqbs.add(new MoveAllStrategyBuilder());
		dqbs.add(new LessContaminationStrategyBuilder());
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(lssFactory,dqsFactory));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		Factory<Event> eventsFactory = new BuilderBasedFactory<>(ebs);
		
		_eventsFactory = eventsFactory;
	}

	private static void startBatchMode() throws IOException {
		InputStream in = new FileInputStream(new File(_inFile));
		OutputStream out = (_outFile == null)? System.out : new FileOutputStream(new File(_outFile));
		
		TrafficSimulator ts = new TrafficSimulator();
		try {
			Controller controller = new Controller(ts, _eventsFactory);
			controller.loadEvents(in);
			controller.run(_ticks, out);
		} catch (Exception e) {
			System.out.format(e.getMessage() + " %n %n");
		}		
		in.close();
	}

	private static void startGUIMode() {
	
		TrafficSimulator ts = new TrafficSimulator();		
		
		try {
			Controller controller = new Controller(ts, _eventsFactory);
			try {
				InputStream in = new FileInputStream(new File(_inFile));
				controller.loadEvents(in);
				in.close();
				} 
			catch (Exception e1) {System.out.format(e1.getMessage() + " %n %n");}
			SwingUtilities.invokeLater(new Runnable() {
				@ Override
				public void run() {
					new MainWindow(controller);
				}
			});
		} catch (Exception e) {
			System.out.format(e.getMessage() + " %n %n");
		}	
	}
	
	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_mode.equalsIgnoreCase("console")) startBatchMode(); // Mostrar error 
		else if(_mode.equalsIgnoreCase("gui")) startGUIMode();
		else System.out.println("Something's wrong, I can feel it");
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
