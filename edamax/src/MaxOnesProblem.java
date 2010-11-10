/**
 * The Max Ones problem. The fitness is the number of 1's in the bit vector.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class MaxOnesProblem extends Problem {
	
	int count, i;
	
	public MaxOnesProblem(PropertyReader props) {
		super(props);
	}

	public double evaluate(Individual t) {
		BitVectorIndividual q = (BitVectorIndividual) t;
		count = 0;
		for (i = 0; i < q.genome.length; i++) {
			if (q.genome[i])
				count++;
		}
		return count;
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
