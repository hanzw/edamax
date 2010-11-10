import java.io.BufferedWriter;
import java.util.logging.*;
import util.MersenneTwisterFast;

/**
 * Generalization of an algorithm. Upon construction, sets up and retains
 * general items such as random number generators, output modules, as specified
 * by the parameter file. Also provides some utility methods for computing
 * statistics. The subclass is not required to implement or use the statistics
 * functions.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public abstract class Algorithm {

	// various state 
	public PropertyReader props;
	public Logger logger;
	public Problem problem;
	public MersenneTwisterFast rand;
	public BufferedWriter out;
	
	// statistics and multiple runs
	public RunStatistics runStats;

	public Algorithm(PropertyReader props, Problem problem, BufferedWriter out) {
		// setup properties and problem as passed in
		this.props = props;
		this.problem = problem;
		this.logger = Logger.getLogger("EC");
		this.out = out;
		
		try {
			// setup seed
			long seed = props.getLongProperty("seed", 271L);
			if(seed <= 0) 
				throw new PropertyException("You cannot use a seed <= 0!");
			this.rand = new MersenneTwisterFast(seed);
			logger.info("Using seed: " + seed);
		} catch (Exception e) {
			System.err.println("Could not continue due to error parsing "
					+ "parameters. Please check log for details. Exiting...");
			System.exit(1);
		}
		this.runStats = new RunStatistics(props, out);
	}
	
	/**
	 * Run the algorithm. (Called by an application)
	 */
	public void runAlgorithm() { 
		logger.info("Resetting run statistics...");
		runStats.reset();
		logger.info("Initializing algorithm...");
		initialize();
		execute();
		runStats.endRun();
	}

	/**
	 * Execute the algorithm (Called by runAlgorithm()) 
	 */
	protected abstract void execute();

	/**
	 * Initialize the algorithm. (Called by runAlgorithm());
	 */
	protected abstract void initialize();
	
}
