import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Keep statistics for single run of an EC.
 * 
 * @author Christopher Vo
 * 
 */
public class RunStatistics implements Cloneable {

	// statistics flags
	public enum StatFlag {
		gen_num, // the generation number
		eval_num, // the evaluation number
		bsf_fitness, // the best so far (fitness)
		bsf_individual, // the best so far (value)
		pvector, // the probability vector
		freq_gen, // print stats at end of each generation
		freq_eval, // print stats at end of each eval
		freq_run, // print stats at end of each run
		run_summary, // print summary at end
		freq_change, // print stats when a new BSF is found
		none
	}

	public EnumSet<StatFlag> stat;

	PropertyReader props;
	Logger logger;
	BufferedWriter out;

	// elapsed generation number
	public int gen_num;
	// elapsed evaluation number
	public int eval_num;
	// best-so-far fitness
	public ArrayList<Double> bsf_fitness_list;
	public ArrayList<Integer> bsf_fitness_idx;
	public double bsf_fitness;
	// best found individual
	public Individual bsf_individual;
	// whether the ideal individual was found
	public boolean found_ideal;
	// the generation number in which the ideal was found
	public int found_ideal_gen;
	// the evaluation number in which the ideal was found
	public int found_ideal_eval;
	// whether the run is complete
	public boolean runComplete = false;

	/**
	 * Constructor for RunStatistics
	 * 
	 * @param props
	 *            The properties object from which the statistics parameters
	 *            will be read.
	 */
	public RunStatistics(PropertyReader props, BufferedWriter out) {
		this.bsf_fitness_list = new ArrayList<Double>();
		this.bsf_fitness_idx = new ArrayList<Integer>();
		this.props = props;
		this.out = out;
		this.logger = Logger.getLogger("EC");
		initStatisticsFlags();
		reset();
	}

	/**
	 * Read the statistics flags from the configuration file.
	 */
	private void initStatisticsFlags() {
		stat = EnumSet.noneOf(StatFlag.class);
		String[] flags = null;
		try {
			flags = props.getStringProperty("statflags", "").split(",");
		} catch (Exception e) {
			System.err.println("Error reading statistics flags. Exiting...");
			System.exit(1);
		}
		if (flags != null) {
			for (String flag : flags) {
				try {
					flag = flag.trim();
					stat.add(StatFlag.valueOf(flag));
				} catch (IllegalArgumentException e) {
					logger.severe("Error (stats): " + flag + " is invalid.");
					System.err.println("Could not continue due to error"
							+ "parsing parameters. Please check "
							+ "log for details. Exiting...");
					System.exit(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			logger.info("Run statistics: " + stat);
		}
	}

	/**
	 * Reset the run statistics.
	 */
	public void reset() {
		gen_num = 0;
		eval_num = 0;
		bsf_fitness_list.clear();
		bsf_fitness_idx.clear();
		bsf_fitness = Double.NEGATIVE_INFINITY;
		bsf_individual = null;
		found_ideal = false;
		found_ideal_gen = 0;
		found_ideal_eval = 0;
		runComplete = false;
	}

	/**
	 * Run statistics after evaluating Individual x.
	 * 
	 * @param x
	 *            The individual to run evaluation statistics on.
	 */
	public void endEval(Individual x) {
		eval_num++;
		if (x.getFitness() > bsf_fitness) {
			bsf_fitness = x.getFitness();
			bsf_individual = (Individual) x.clone();
			//bsf_fitness_list.add(bsf_fitness);
			//bsf_fitness_idx.add(eval_num);
			if (x.isIdeal() && !found_ideal) {
				found_ideal = true;
				found_ideal_gen = gen_num;
				found_ideal_eval = eval_num;
			}
			if(stat.contains(StatFlag.freq_change)) {
				printCurrStats();
			}
		}
		if (stat.contains(StatFlag.freq_eval))
			printCurrStats();

	}

	/**
	 * Run statistics after generation.
	 */
	public void endGen() {
		gen_num++;
		if (stat.contains(StatFlag.freq_gen))
			printCurrStats();
	}

	/**
	 * End run.
	 */
	public void endRun() {
		try {
			out.write("==== END RUN ====\n");
			out.flush();
			runComplete = true;
		} catch (IOException e) {
			logger.severe("IOException while flushing IO in endRun()!\n" + e);
			System.exit(1);
		}
	}

	/**
	 * Print current statistics based on config file parameters.
	 */
	public void printCurrStats() {
		try {
			if (stat.contains(StatFlag.gen_num))
				out.write(gen_num + " ");
			if (stat.contains(StatFlag.eval_num))
				out.write(eval_num + " ");
			if (stat.contains(StatFlag.bsf_fitness))
				out.write(bsf_fitness + " ");
			if (stat.contains(StatFlag.bsf_individual))
				out.write("\n" + bsf_individual);
			if (!stat.isEmpty())
				out.write("\n");
			out.flush();
		} catch (IOException e) {
			logger.severe("IOException in printCurrStats()!\n" + e);
			System.exit(1);
		}
	}

	/**
	 * Print a summary for the run.
	 */
	public void summary() {
		try {
			if (stat.contains(StatFlag.run_summary)) {
				out.write("--------------------------------\n");
				out.write("Best fitness found: " + bsf_fitness + "\n");
				out.write("Best individual found: " + bsf_individual + "\n");
				if (found_ideal) {
					out.write(String.format("Found ideal individual after: "
							+ "evaluation %d (generation %d)\n", found_ideal_eval,
							found_ideal_gen));
				}
				out.flush();
			}
		} catch (IOException e) {
			logger.severe("IOException in summary()!\n" + e);
			System.exit(1);
		}
	}

	/**
	 * Make a copy of the Run Statistics.
	 */
	public Object clone() {
		RunStatistics tmp = null;
		try {
			tmp = (RunStatistics) super.clone();
			tmp.bsf_fitness_idx = (ArrayList<Integer>) bsf_fitness_idx.clone();
			tmp.bsf_fitness_list = (ArrayList<Double>) bsf_fitness_list.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return tmp;
	}

}
