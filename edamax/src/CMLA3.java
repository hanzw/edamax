import java.io.BufferedWriter;
import java.util.Arrays;

/**
 * The Compact Multiagent Learning Algorithm. Fake name given to an algorithm
 * that applies the EGT model for CCEAs to form a unique type of univariate EDA.
 * In particular, payoff for a variable over pairwise "collaborations" with
 * other variables is computed.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class CMLA3 extends Algorithm {

	// cmla-specific Parameters and defaults
	int genome_size = 100; // the length of the genome
	int generations = 300; // the number of generations to run
	int samples = 5; // number of samples to use in fitness estimation
	double tournament_size = 2; // the size of the tournaments
	double alpha = 0.5; // the learning rate
	boolean use_best = false; // use the historical best?

	// probability vector for generation t and t+1
	double t[];
	double t1[];

	// the historical best
	double hist_best_0[];
	double hist_best_1[];

	/**
	 * @param props
	 *            The properties object containing parameters for the cmla.
	 * @param problem
	 *            The problem instance. Currently, this code assumes the problem
	 *            instance is a BitVectorIndividual.
	 */
	public CMLA3(PropertyReader props, Problem problem, BufferedWriter out) {

		// do setup from abstract super
		super(props, problem, out);

		// initialize some cmla-specific parameters
		try {

			// genome size
			genome_size = props.getIntProperty("cmla.genome_size", genome_size);
			if (genome_size <= 0)
				throw new PropertyException("cmla.genome_size should be > 0!");

			// generations
			generations = props.getIntProperty("cmla.generations", generations);
			if (generations <= 0)
				throw new PropertyException("cmla.generations should be > 0!");

			// samples
			samples = props.getIntProperty("cmla.samples", samples);
			if (samples <= 0)
				throw new PropertyException("cmla.samples should be > 0!");

			// alpha
			alpha = props.getDoubleProperty("cmla.alpha", alpha);
			if (alpha < 0 || alpha > 1)
				throw new PropertyException(
						"cmla.alpha must be 0 <= alpha <= 1!");

			// tournament size
			tournament_size = props.getDoubleProperty("cmla.tournament_size",
					tournament_size);
			if (tournament_size < 1)
				throw new PropertyException(
						"cmla.tournament_size should be >= 1!");

			// use best
			use_best = props.getBooleanProperty("cmla.use_historical_best", use_best);

		} catch (Exception e) {
			System.err.println("Could not continue due to error parsing "
					+ "parameters. Please check log for details. Exiting...");
			System.exit(1);
		}

	}

	/**
	 * Run the cmla algorithm.
	 * 
	 * @see Algorithm#execute()
	 */
	protected void execute() {
		logger.info("Starting CMLA\n--------------------------------");

		int i, j;
		double f0, f1;
		// double s0, s1;
		double fit;
		BitVectorIndividual ind = new BitVectorIndividual(genome_size, rand);

		while (runStats.gen_num < generations) {

			// for each slot i
			for (i = 0; i < genome_size; i++) {
				f0 = Double.NEGATIVE_INFINITY;
				f1 = Double.NEGATIVE_INFINITY;
				// s0 = 0.0;
				// s1 = 0.0;
				// create and evaluate n samples on t for all values of slot i
				for (j = 0; j < samples; j++) {
					// slot i = false
					ind.replaceUsingProbabilities(t); // new sample
					ind.genome[i] = true; // fix slot i = true
					ind.evaluated = false;
					ind.evaluate(problem); // evaluate
					runStats.endEval(ind); // stats
					fit = ind.getFitness(); // get fitness
					if (fit > f1)
						f1 = fit;
					// s1 += fit;

					// slot i = false
					ind.replaceUsingProbabilities(t); // new sample
					ind.genome[i] = false; // fix slot i = false
					ind.evaluated = false;
					ind.evaluate(problem); // evaluate
					runStats.endEval(ind); // stats
					fit = ind.getFitness(); // get fitness
					if (fit > f0)
						f0 = fit;
					// s0 += fit;
				}

				// get averages
				// s0 = s0 / samples;
				// s1 = s1 / samples;

				// keep track of historical best.
				if (f0 > hist_best_0[i])
					hist_best_0[i] = f0;
				if (f1 > hist_best_1[i])
					hist_best_1[i] = f1;

				// update t1 on this slot.
				if (use_best) {
					updateTournament(i, hist_best_0[i], hist_best_1[i]);
				} else {
					//updateDirect(i, f0, f1);
					//updateProportional(i,f0,f1);
					updateTournament(i, f0, f1);
				}
			}

			// print pvector if requested
			if (runStats.stat.contains(RunStatistics.StatFlag.pvector))
				System.out.println(Arrays.toString(t));

			// copy t1 into t
			t = (double[]) t1.clone();

			runStats.endGen();
		}
		runStats.summary();
	}

	
	  private void updateProportional(int i, double f0, double f1) { 
		double sol = t[i] * (f1 * t[i]) / (f0 * (1 - t[i]) + f1 * t[i]); 
		t1[i] = alpha * sol + (1-alpha) * t[i]; 
		}
	  
	 
	   private void updateDirect(int i, double f0, double f1) 
		{ 
		double sol = (f1 * t[i]) / (f0 * (1 - t[i]) + f1 * t[i]); t1[i] = alpha * sol + (1-alpha) * t[i]; 
		}
	  

	private void updateTournament(int i, double f0, double f1) {
		double x0 = 1 - t[i], x1 = t[i];
		// cap x1 at 0.0
		if (x1 <= 0.0)
			return;
		double num = Math.pow(x1 + (f0 <= f1 ? x0 : 0), tournament_size)
				- Math.pow(f0 < f1 ? x0 : 0, tournament_size);
		double sol = (x1 / (x1 + (f0 == f1 ? x0 : 0))) * num;
		t1[i] = alpha * sol + (1 - alpha) * x1;
	}

	protected void initialize() {
		// initialize vectors
		int i;
		t = new double[genome_size];
		t1 = new double[genome_size];
		hist_best_0 = new double[genome_size];
		hist_best_1 = new double[genome_size];
		for (i = 0; i < genome_size; i++) {
			t[i] = 0.5;
			t1[i] = 0.5;
			hist_best_0[i] = Double.NEGATIVE_INFINITY;
			hist_best_1[i] = Double.NEGATIVE_INFINITY;
		}

	}

}
