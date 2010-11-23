import java.io.BufferedWriter;
import java.util.*;

/**
 * The Population-Based Incremental Learning (PBIL) Algorithm (Baluja, 1994)
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class PBIL_RV extends Algorithm {

	// PBIL-specific parameters and defaults
	int samples = 200; // the number of samples to grab each iteration
	double learn_rate = 0.005; // strength of the update function
	int selection_size = 2; // how large the selection pool is
	int genome_size = 4; // the length of the genome
	int generations = 300; // the number of generations to run PBIL.

	// Probability vector
	MixGaussVariable p[];

	// The list of samples.
	DoubleVectorIndividual[] sv;

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
	public PBIL_RV(PropertyReader props, Problem problem, BufferedWriter out) {

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

		while (runStats.gen_num <= generations) {
			// generate samples
			for (int i = 0; i < samples; i++) {
				// generate sample vector based on p[]
				sv[i].replaceUsingProbabilities(p);
				// evaluate sample vector based on problem
				sv[i].evaluate(problem);
				// run stats
				runStats.endEval(sv[i]);
			}
			
			// update distributions of probability to reflect new samples
			
			updateDistribution(sv, p);

			runStats.endGen();
		}
		runStats.summary();

	}

	protected void updateDistribution(Individual[] evalVect, MixGaussVariable[] probs) {
		// sort samples (note, the "natural order" given by the comparator
		// in Individual is to arrange highest fitness individuals in front
		// of lower fitness individuals)
		DoubleVectorIndividual[] sv;
		sv = (DoubleVectorIndividual[]) evalVect;
		Arrays.sort(sv);

		// print pvector if requested
		if (runStats.stat.contains(RunStatistics.StatFlag.pvector))
			System.out.println(Arrays.toString(probs));

		// go through the top (selection_size) elements
		for (int i = 0; i < selection_size; i++) {
			for (int j = 0; j < genome_size; j++) {
				probs[j].updMixForSingle(sv[i].genome[j]);
//				probs[j] = probs[j] * (1.0 - learn_rate)
//						+ (sv[i].genome[j] ? 1.0 : 0.0) * learn_rate;
			}
		}
	}

	/**
	 * Reset the algorithm (perhaps for another run)
	 */
	protected void initialize() {

		// initialize probability vector
		int i;
		p = new MixGaussVariable[genome_size];
		for (i = 0; i < genome_size; i++)
			p[i] = new MixGaussVariable( problem.getGenomeMin(), 
					problem.getGenomeMax(), 2, 
					new double[] {-0.5, 0.5}, new double[] {0.5, 0.5}, new double[] {0.5,0.5});

		// initialize solution vector
		sv = new DoubleVectorIndividual[samples];
		for (i = 0; i < samples; i++) {
			sv[i] = new DoubleVectorIndividual(genome_size, rand);
			sv[i].replaceUsingProbabilities(p);
		}
	}

}
