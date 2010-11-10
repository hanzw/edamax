/**
 * Leading Ones Problem. Counts the number of leading ones in the vector.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class LeadingOnesProblem extends Problem {

	public LeadingOnesProblem(PropertyReader props) {
		super(props);
	}

	public double evaluate(Individual t) {
		int i = 0;
		BitVectorIndividual q = (BitVectorIndividual) t;
		while (i < q.genome_size && q.genome[i])
			i++;
		return i;
	}

	public boolean isIdeal(Individual t) {
		BitVectorIndividual q = (BitVectorIndividual) t;
		if (q.isEvaluated() && q.fitness == q.genome.length)
			return true;
		else if (evaluate(q) == q.genome.length)
			return true;
		else
			return false;
	}

}
