import java.io.BufferedWriter;
import java.util.Arrays;

/**
 * The Compact Genetic Algorithm (cGA) (Harik, Lobo, & Goldberg, 1998)
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class CGA extends Algorithm {

	// cGA-specific Parameters and defaults
	int genome_size = 100; // the length of the genome
	int generations = 300; // the number of generations to run cGA
	int n = 20; // "population size" (update step will use 1/n)

	// probability vector
	double p[];

	// the pair of solution vectors
	BitVectorIndividual[] sv;

	/**
	 * @param props
	 *            The properties object containing parameters for the cGA.
	 * @param problem
	 *            The problem instance. Currently, this code assumes the problem
	 *            instance is a BitVectorIndividual.
	 */
	public CGA(PropertyReader props, Problem problem, BufferedWriter out) {

		// do setup from abstract super
		super(props, problem, out);

		// initialize some cga-specific parameters
		try {

			// genome size
			genome_size = props.getIntProperty("cga.genome_size", genome_size);
			if (genome_size <= 0)
				throw new PropertyException("cga.genome_size should be > 0!");

			// generations
			generations = props.getIntProperty("cga.generations", generations);
			if (generations <= 0)
				throw new PropertyException("cga.generations should be > 0!");

			// n
			n = props.getIntProperty("cga.n", n);
			if (n <= 0)
				throw new PropertyException("cga.n should be > 0!");

		} catch (Exception e) {
			System.err.println("Could not continue due to error parsing "
					+ "parameters. Please check log for details. Exiting...");
			System.exit(1);
		}

	}

	/**
	 * Run the cGA algorithm.
	 * 
	 * @see Algorithm#execute()
	 */
	protected void execute() {
		logger.info("Starting cGA\n--------------------------------");

		int i;
		double update = 1.0 / n;

		while (runStats.gen_num < generations) {
			// generate two individuals
			sv[0].replaceUsingProbabilities(p);
			sv[1].replaceUsingProbabilities(p);

			// compare and find the winner
			// (better one ends up in sv[0])
			sv[0].evaluate(problem);
			sv[1].evaluate(problem);
			Arrays.sort(sv);

			// run stats
			runStats.endEval(sv[0]);
			runStats.endEval(sv[1]);

			// print pvector if requested
			if (runStats.stat.contains(RunStatistics.StatFlag.pvector))
				System.out.println(Arrays.toString(p));

			// update probability vector towards the better one
			for (i = 0; i < genome_size; i++) {
				if ((sv[0].genome[i]) != (sv[1].genome[i])) {
					// update bit
					if (sv[0].genome[i]) {
						p[i] = p[i] + update;
					} else {
						p[i] = p[i] - update;
					}
					// fix numerical problems with p[i] > 1 or < 0
					if (p[i] > 1.0)
						p[i] = 1.0;
					if (p[i] < 0.0)
						p[i] = 0.0;
				}
			}

			runStats.endGen();
		}
		runStats.summary();
	}

	protected void initialize() {

		// initialize probability vector
		int i;
		p = new double[genome_size];
		for (i = 0; i < genome_size; i++)
			p[i] = 0.5;

		// initialize pair of solution vectors
		sv = new BitVectorIndividual[2];
		for (i = 0; i < 2; i++) {
			sv[i] = new BitVectorIndividual(genome_size, rand);
			sv[i].replaceUsingProbabilities(p);
		}

	}

}
