import java.io.BufferedWriter;
import java.util.Arrays;

/**
 * The Univariate Marginal Distribution Algorithm (UMDA). (Muhlenbein & PaaB,
 * 1996)
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class UMDA extends Algorithm {

	// UMDA-specific parameters
	int genome_size = 100;
	int selection_size = 10;
	int population_size = 200;
	int generations = 400;

	// probability vector
	double[] p;

	// the population.
	BitVectorIndividual[] pop;

	public UMDA(PropertyReader props, Problem problem, BufferedWriter out) {
		
		super(props, problem, out);

		// initialize some umda-specific parameters
		try {
			population_size = props.getIntProperty("umda.population_size",
					population_size);
			if (population_size <= 0)
				throw new PropertyException(
						"umda.population_size should be > 0!");

			selection_size = props.getIntProperty("umda.selection_size",
					selection_size);
			if (selection_size <= 0)
				throw new PropertyException(
						"umda.selection size should be > 0!");
			if (selection_size >= population_size)
				throw new PropertyException("umda.selection_size should be < "
						+ "umda.population_size!");

			genome_size = props.getIntProperty("umda.genome_size", genome_size);
			if (genome_size <= 0)
				throw new PropertyException("umda.genome_size should be > 0!");

			generations = props.getIntProperty("umda.generations", generations);
			if (generations <= 0)
				throw new PropertyException("umda.generations should be > 0!");

		} catch (Exception e) {
			System.err.println("Could not continue due to error parsing "
					+ "parameters. Please check log for details. Exiting...");
			System.exit(1);
		}

	}

	protected void execute() {
		logger.info("Starting UMDA\n--------------------------------");

		int i, j;
		double sum;
		while (runStats.gen_num <= generations) {
			// evaluate fitness and sort population by fitness
			for (i = 0; i < population_size; i++) {
				pop[i].evaluate(problem);
				runStats.endEval(pop[i]);
			}
			Arrays.sort(pop);

			// print pvector if requested
			if (runStats.stat.contains(RunStatistics.StatFlag.pvector))
				System.out.println(Arrays.toString(p));

			// estimate joint probability distribution p for
			// the best selection_size individuals
			for (j = 0; j < genome_size; j++) {
				sum = 0.0;
				for (i = 0; i < selection_size; i++)
					sum += (pop[i].genome[j] ? 1.0 : 0.0);
				p[j] = sum / (double) selection_size;
			}

			// generate new individuals from p to replace the worst
			// population_size - selection_size individuals.
			for (i = selection_size; i < population_size; i++)
				pop[i].replaceUsingProbabilities(p);

			runStats.endGen();
		}
		runStats.summary();
	}

	/**
	 * Reset the algorithm (perhaps for another run)
	 */
	protected void initialize() {

		// initialize probability vector
		int i;
		p = new double[genome_size];
		for (i = 0; i < genome_size; i++)
			p[i] = 0.5;

		// initialize population
		pop = new BitVectorIndividual[population_size];
		for (i = 0; i < population_size; i++) {
			pop[i] = new BitVectorIndividual(genome_size, rand);
			pop[i].replaceUsingProbabilities(p);
		}

	}

}
