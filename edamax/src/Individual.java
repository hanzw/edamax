import util.MersenneTwisterFast;

/**
 * A generalization of an Individual with a fitness.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public abstract class Individual implements Comparable, Cloneable {

	public double fitness;
	public boolean evaluated;
	public boolean ideal;
	MersenneTwisterFast rand;

	public Individual(MersenneTwisterFast rand) {
		this.rand = rand;
	}

	/**
	 * Evaluate oneself, and determine whether ideal. Set flags to indicate
	 * whether onself is evaluated and ideal.
	 */
	public void evaluate(Problem p) {
		fitness = p.evaluate(this);
		evaluated = true;
		ideal = p.isIdeal(this);
	}

	/**
	 * Return whether oneself is evaluated.
	 * 
	 * @return True if evaluated. False if something has changed in the
	 *         individual and this change has not yet been evaluated.
	 */
	public boolean isEvaluated() {
		return this.evaluated;
	}

	/**
	 * Return whether oneself has ideal fitness
	 * 
	 * @return True if the individual has an ideal fitness. False otherwise.
	 */
	public boolean isIdeal() {
		return this.ideal;
	}

	/**
	 * Comparison function. "Natural Ordering" places high fitness ahead of low
	 * fitness so that when Arrays.sort() is run on an array of individuals, the
	 * highest fitness ends up before lower fitness.
	 * 
	 * @return -1 if oneself is GREATER than o, 0 if oneself is equal to o, 1 if
	 *         oneself is LESS than o.
	 */
	public int compareTo(Object o) {
		double tmp = ((BitVectorIndividual) o).fitness;
		if (fitness > tmp)
			return -1;
		else if (fitness < tmp)
			return 1;
		else
			return 0;
	}

	/**
	 * Make a copy of the Individual.
	 */
	public Object clone() {
		Individual tmp = null;
		try {
			tmp = (Individual) super.clone();
			tmp.fitness = fitness;
			tmp.evaluated = evaluated;
			tmp.ideal = ideal;
			tmp.rand = rand;
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return tmp;
	}

	/**
	 * Get the fitness of the individual
	 * 
	 * @return The fitness of the individual.
	 */
	public double getFitness() {
		return fitness;
	}

}
