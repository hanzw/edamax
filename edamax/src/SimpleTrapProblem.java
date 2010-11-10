/**
 * A simple concatenated traps problem as noted in (Pelikan - GECCO 2007
 * Tutorial Slides, p.18)
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class SimpleTrapProblem extends Problem {

	int trapsize = 5;

	public SimpleTrapProblem(PropertyReader props) {
		super(props);

		try {
			trapsize = props.getIntProperty("problem.trapsize", trapsize);
			if (trapsize <= 0) {
				throw new PropertyException("problem.trapsize should be > 0!");
			}
		} catch (Exception e) {
			System.err.println("Could not continue due to error parsing "
					+ "parameters. Please check log for details. Exiting...");
			System.exit(1);
		}
		
	}

	public double evaluate(Individual t) {
		int sum, trapsum, i, j;
		BitVectorIndividual q = (BitVectorIndividual) t;
		// concatenate traps
		sum = 0;
		for (i = 0; i + (trapsize - 1) < q.genome_size; i += trapsize) {
			trapsum = 0;
			for (j = i; j < i + trapsize; j++) {
				if (q.genome[j])
					trapsum++;
			}
			if (trapsum == trapsize) {
				sum += trapsize;
			} else {
				sum += (trapsize - 1) - trapsum;
			}
		}
		// add remaining bits
		while (i < q.genome_size) {
			sum += q.genome[i] ? 1 : 0;
			i++;
		}
		return sum;
	}

	public boolean isIdeal(Individual t) {
		BitVectorIndividual q = (BitVectorIndividual) t;
		if (q.evaluated && q.fitness == q.genome.length)
			return true;
		else if (evaluate(q) == q.genome.length)
			return true;
		else
			return false;
	}

}
