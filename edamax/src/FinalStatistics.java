import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Class to compute the statistics over a set of runs.
 * 
 * @author vo
 * 
 */
public class FinalStatistics {

	// column indices for stats
	public static int STAT_MEAN = 0;
	public static int STAT_STDDEV = 1;
	public static int STAT_LO = 2;
	public static int STAT_HI = 3;

	boolean complete = false;
	Logger logger;
	BufferedWriter out;
	PropertyReader props;
	RunStatistics[] runlist;
	double[][] table = null;
	double[][] averages = null;
	int evals = 0;
	int runs = 0;
	int resolution = 1;

	public FinalStatistics(PropertyReader props, BufferedWriter out,
			RunStatistics[] runlist) {
		complete = false;
		logger = Logger.getLogger("EC");
		this.runlist = runlist;
		this.props = props;
		this.out = out;

		try {
			resolution = props.getIntProperty("finalstats_resolution",
					resolution);
			if (resolution <= 0)
				throw new PropertyException(
						"finalstats_resolution should be >= 1!");
		} catch (Exception e) {
			System.err.println("Could not continue due to error parsing "
					+ "parameters. Please check log for details. Exiting...");
			System.exit(1);
		}
	}

	/**
	 * Compute the statistics. When the computation is complete, the boolean
	 * flag "complete" will be set to true. If the computation does not complete
	 * (for example, due to an error, the flag "complete" will remain false.
	 */
	private void computeStatistics() {

		// if the runlist is null, or empty, do nothing.
		if (runlist == null || runlist.length < 1) {
			logger.warning("The runlist was null or empty.\n"
					+ "No statistics to compute!");
			return;
		}

		logger.info("Beginning statistics calculations..."
				+ "\n-------------------------");

		fillTable();

		fillAverages();

		complete = true;
	}

	/**
	 * Fill the avg and stdev for each evaluation into the averages array. The
	 * standard two-pass method is used here. Double precision and range may or
	 * may not suffice here for this calculation; I assume it will all be OK.
	 * Prerequisites: table is filled and evals and runs are set correctly.
	 */
	private void fillAverages() {
		// initialize averages table
		// column 0 will be averages
		// column 1 will be standard deviation
		// column 2 will be lo CI 95%
		// column 3 will be hi CI 95%
		averages = new double[table.length][4];

		int i, j;
		double sum, avg, var, stddev, lo, hi;
		// for each row i
		for (i = 0; i < evals; i++) {
			sum = 0.0;
			// compute sample mean
			for (j = 0; j < runs; j++)
				sum += table[i][j];
			avg = sum / runs;

			// compute sample variance
			sum = 0.0;
			for (j = 0; j < runs; j++)
				sum += (table[i][j] - avg) * (table[i][j] - avg);
			var = sum / (runs - 1);
			stddev = Math.sqrt(var);
			lo = avg - 1.96 * stddev;
			hi = avg + 1.96 * stddev;

			// record values
			averages[i][STAT_MEAN] = avg;
			averages[i][STAT_STDDEV] = stddev;
			averages[i][STAT_LO] = lo;
			averages[i][STAT_HI] = hi;
		}
	}

	/**
	 * Copy the collected run statistics into a master table on which we can
	 * perform statistical operations.
	 */
	private void fillTable() {
		// look at first run and ascertain the number of evaluations run. each
		// row of the stats table will represent an evaluation, and each column
		// will represent a run.
		evals = runlist[0].eval_num;
		runs = runlist.length;
		table = new double[evals][runs];

		// ----- FILL TABLE -----
		// for run i
		int i, j;
		for (i = 0; i < runs; i++) {
			if (runlist[i] == null) {
				// error: null run
				logger.warning(String.format(
						"Run %d was null. " + "Exiting...", i));
				System.exit(1);
			} else if (!runlist[i].runComplete) {
				// error: incomplete run
				logger.warning(String.format("Run %d was incomplete. "
						+ "Exiting...", i));
				System.exit(1);
			} else if (runlist[i].eval_num != evals) {
				// error: run has different number of evaluations from first
				logger.warning(String.format("Run %d had a different "
						+ "number of evaluations from run 0. Exiting...", i));
				System.exit(1);
			}
			// fill in column i
			int write = 0, seek = 0;
			double tmp = 0.0;
			for (j = 0; j < runlist[i].bsf_fitness_idx.size(); j++) {
				// seek forward to next changed value
				// note that evaluation 1 is index 0
				seek = runlist[i].bsf_fitness_idx.get(j);
				// write until "write head" is equal to the seek
				while (write < seek) {
					table[write][i] = tmp;
					write++;
				}
				// get new value
				tmp = runlist[i].bsf_fitness_list.get(j);
			}
			// fill remainder of column i until "write head" is at end of list
			while (write < evals) {
				table[write][i] = tmp;
				write++;
			}
		}
	}

	/**
	 * Print the computed statistics.
	 */
	public void printStatistics() {
		// if not complete, try to compute statistics. if still not complete,
		// return empty handed.
		if (!complete) {
			computeStatistics();
			if (!complete)
				return;
		}
		// output averages table
		try {
			for (int i = 1; i < evals; i += resolution) {
				out.write(String.format("%d %.4f %.4f %.4f %.4f\n", i,
						averages[i][STAT_MEAN], averages[i][STAT_STDDEV],
						averages[i][STAT_LO], averages[i][STAT_HI]));

			}
			out.flush();
		} catch (IOException e) {
			logger.severe("IOException in printStatistics!\n" + e);
			System.exit(1);
		}
	}
}
