import java.io.BufferedWriter;
import java.util.*;

/**
 * The Population-Based Incremental Learning (PBIL) Algorithm (Baluja, 1994)
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class PBIL extends Algorithm {

	// PBIL-specific parameters and defaults
	int samples = 200; // the number of samples to grab each iteration
	double learn_rate = 0.005; // strength of the update function
	int selection_size = 2; // how large the selection pool is
	int genome_size = 100; // the length of the genome
	int generations = 300; // the number of generations to run PBIL.

	// Probability vector
	// NOTE: Note that this probability vector assumes a BitVectorIndividual.
	// We'll keep it like this because the update rule in the literature depends
	// on this kind of representation.
	double p[];

	// The list of samples.
	BitVectorIndividual[] sv;

	/**
	 * Constructor of the PBIL algorithm.
	 * 
	 * @param props
	 *            The properties object containing parameters for the PBIL
	 *            algorithm such as sample size, selection size, genome, size,
	 *            and number of generations to run.
	 * @param problem
	 *            The problem instance. Currently, this code assumes the problem
	 *            instance is a BitVectorIndividual.
	 */
	public PBIL(PropertyReader props, Problem problem, BufferedWriter out) {

		// do all the setup from the abstract super
		super(props, problem, out);

		// initialize some pbil-specific parameters
		try {

			// samples
			samples = props.getIntProperty("pbil.samples", samples);
			if (samples <= 0)
				throw new PropertyException("pbil.samples should be > 0!");

			// learn rate
			learn_rate = props.getDoubleProperty("pbil.learn_rate", learn_rate);
			if (learn_rate <= 0.0)
				throw new PropertyException(
						"pbil.learning rate should be > 0.0!");

			// selection size
			selection_size = props.getIntProperty("pbil.selection_size",
					selection_size);
			if (selection_size <= 0)
				throw new PropertyException(
						"pbil.selection size should be > 0!");
			if (selection_size >= samples)
				throw new PropertyException(
						"pbil.selection_size should be < pbil.samples!");

			// genome size
			genome_size = props.getIntProperty("pbil.genome_size", genome_size);
			if (genome_size <= 0)
				throw new PropertyException("pbil.genome_size should be > 0!");

			// generations
			generations = props.getIntProperty("pbil.generations", generations);
			if (generations <= 0)
				throw new PropertyException("pbil.generations should be > 0!");

		} catch (Exception e) {
			System.err.println("Could not continue due to error parsing "
					+ "parameters. Please check log for details. Exiting...");
			System.exit(1);
		}

	}

	/**
	 * Run the PBIL Algorithm.
	 * 
	 * @see Algorithm#execute()
	 */
	protected void execute() {
		logger.info("Starting PBIL\n--------------------------------");

		int i, j;
		while (runStats.gen_num <= generations) {
			// generate samples
			for (i = 0; i < samples; i++) {
				// generate sample vector based on p[]
				sv[i].replaceUsingProbabilities(p);
				// evaluate sample vector based on problem
				sv[i].evaluate(problem);
				// run stats
				runStats.endEval(sv[i]);
			}
			// sort samples (note, the "natural order" given by the comparator
			// in Individual is to arrange highest fitness individuals in front
			// of lower fitness individuals)
			Arrays.sort(sv);

			// print pvector if requested
			if (runStats.stat.contains(RunStatistics.StatFlag.pvector))
				System.out.println(Arrays.toString(p));

			// go through the top (selection_size) elements
			for (i = 0; i < selection_size; i++) {
				for (j = 0; j < genome_size; j++) {
					p[j] = p[j] * (1.0 - learn_rate)
							+ (sv[i].genome[j] ? 1.0 : 0.0) * learn_rate;
				}
			}

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

		// initialize solution vector
		sv = new BitVectorIndividual[samples];
		for (i = 0; i < samples; i++) {
			sv[i] = new BitVectorIndividual(genome_size, rand);
			sv[i].replaceUsingProbabilities(p);
		}
	}

}
