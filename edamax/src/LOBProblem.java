/**
 * Leading Ones Blocks Problem (LOB(b)) as defined by Jansen and Wiegand (2003),
 * The Cooperative Coevolutionary (1+1) EA (Technical Report), page 14. The LOB
 * function value equals the number of blocks of size b that have all bits set
 * to 1 (scanning x from left to right).
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class LOBProblem extends Problem {

	int blocksize = 5;

	public LOBProblem(PropertyReader props) {
		super(props);

		try {
			blocksize = props.getIntProperty("problem.blocksize", blocksize);
			if (blocksize <= 0)
				throw new PropertyException("problem.blocksize should be > 0!");
		} catch (Exception e) {
			System.err.println("Could not continue due to error parsing "
					+ "parameters. Please check log for details. Exiting...");
			System.exit(1);
		}
	}

	public double evaluate(Individual t) {
		int sum = 0;
		boolean multiple = true;
		int i = 0, j = 0;
		BitVectorIndividual q = (BitVectorIndividual) t;
		for (i = 1; i <= q.genome_size / blocksize; i++) {
			multiple = true;
			for (j = 0; j < blocksize * i; j++) {
				multiple = multiple && q.genome[j];
			}
			sum += multiple?1:0;
		}
		// handle remainder block
		int rem = q.genome_size % blocksize;
		if(rem != 0.0) {
			int count = 0;
			while (j < q.genome_size) {
				if (q.genome[j])
					count++;
				j++;
			}
			if (count == rem)
				sum += 1;
		}
		return (double)sum;
	}

	public boolean isIdeal(Individual t) {
		BitVectorIndividual q = (BitVectorIndividual) t;
		if (q.evaluated && q.fitness == q.genome.length)
			return true;
		else if (evaluate(q) >= Math.floor(q.genome_size/blocksize))
			return true;
		else
			return false;
	}

}
