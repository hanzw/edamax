import java.io.*;
import java.lang.reflect.Constructor;
import java.util.logging.*;

/**
 * Application (main method). Sets up the algorithm and problem, then runs it.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class Application {

	/**
	 * @param args
	 *            The command line arguments given to EDA.
	 */
	public static void main(String[] args) {
		
		String filename = "edatest.config";
		PropertyReader props = new PropertyReader();
		
		// create an output stream
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new
			         FileOutputStream(java.io.FileDescriptor.out), "ASCII"), 4096);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		// read in properties file
		if (args.length > 0)
			filename = args[0];

		try {
			File file = new File(".", filename);
			props.load(new FileInputStream(file));
		} catch (Exception e) {
			System.err.println("Usage: java Application [config file name]");
			System.exit(1);
		}

		// get "output" parameter
		String output = "ALL";
		try {

		} catch (Exception e) {
			System.err
					.println("Error while parsing 'output' parameter. Exiting...");
			System.exit(1);
		}

		// make a logger, initially allow all logging.
		Logger logger = Logger.getLogger("EC");
		logger.setLevel(Level.ALL);

		// get parameters
		int runs = 1;
		String props_algname = "";
		String props_probname = "";
		boolean finalstat_on = false;
		try {
			runs = props.getIntProperty("runs", runs);
			props_algname = props.getStringProperty("algorithm", props_algname);
			props_probname = props.getStringProperty("problem", props_probname);
			output = props.getStringProperty("loglevel", output).toUpperCase();
			finalstat_on = props.getBooleanProperty("finalstats", finalstat_on);
		} catch (Exception e) {
			System.err
					.println("Error parsing parameters. Please check logs for details.");
			System.exit(1);
		}

		// initial parameter parsing succeeded, from now on use logging level
		// specified in the parameter file.
		logger.setLevel(Level.parse(output));
		logger.info("Using properties: " + props);


		// instantiate the problem and algorithm using reflection.
		Class cls;
		Class[] types;
		Object arglist[];
		Constructor ct;
		Problem prob;
		Algorithm alg = null;
		try {
			// Problem (no parameters)
			cls = Class.forName(props_probname);
			types = new Class[1];
			types[0] = PropertyReader.class;
			ct = cls.getConstructor(types);
			arglist = new Object[1];
			arglist[0] = props;
			prob = (Problem) ct.newInstance(arglist);
			// Algorithm (some parameters)
			// Why load algorithms this way? No reason, just wanted to try
			// reflection since I heard a lot about it.
			cls = Class.forName(props_algname);
			types = new Class[3];
			types[0] = PropertyReader.class;
			types[1] = Problem.class;
			types[2] = BufferedWriter.class;
			ct = cls.getConstructor(types);
			arglist = new Object[3];
			arglist[0] = props;
			arglist[1] = prob;
			arglist[2] = out;
			alg = (Algorithm) ct.newInstance(arglist);
		} catch (ClassNotFoundException e) {
			System.err.println("The algorithm class specified by the "
					+ "configuration file could not be found. Exiting...");
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			System.err.println("The algorithm class specified by the "
					+ "configuration file was not loaded due to an error."
					+ "Exiting...");
			e.printStackTrace();
			System.exit(1);
		}

		// initialize the statistics
		RunStatistics[] runlist = new RunStatistics[runs];

		// run the algorithm
		for (int i = 0; i < runs; i++) {
			logger.info("Run "+ i + ": ");
			alg.runAlgorithm();
			runlist[i] = (RunStatistics)alg.runStats.clone();
		}
		
		// print final statistics
		if(finalstat_on) {
			FinalStatistics finalstats = new FinalStatistics(props, out, runlist);
			finalstats.printStatistics();
		}
		
	}
}
