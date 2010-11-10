import util.MersenneTwisterFast;

/**
 * A bit vector individual. The genome is a finite-length bit vector.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class BitVectorIndividual extends Individual {

	public boolean[] genome;
	public int genome_size;

	public BitVectorIndividual(int genome_size, MersenneTwisterFast rand) {
		super(rand);
		this.genome_size = genome_size;
		this.evaluated = false;
		this.ideal = false;
		genome = new boolean[genome_size];
	}

	/**
	 * Replace genome with coin flips computed from a given probability
	 * distribution (given as a probability vector)
	 * 
	 * @param p
	 *            The probability vector.
	 */
	public void replaceUsingProbabilities(double[] p) {
		for (int i = 0; i < genome_size; i++) {
			genome[i] = rand.nextBoolean(p[i]);
		}
		this.evaluated = false;
	}
	
	/**
	 * Repalce genome with random coin flips.
	 */
	public void replaceRandomly() {
		for (int i = 0; i < genome_size; i++) {
			genome[i] = rand.nextBoolean();
		}
		this.evaluated = false;
	}

	/**
	 * Make a copy of the BitVectorIndividual.
	 */
	public Object clone() {
		BitVectorIndividual tmp = (BitVectorIndividual) (super.clone());
		tmp.genome = (boolean[])genome.clone();
		tmp.genome_size = genome_size;
		return tmp;
	}

	/**
	 * Check whether two BitVectorIndividuals are equal
	 */
	public boolean equals(Object ind) {
		if (!(this.getClass().equals(ind.getClass())))
			return false;
		BitVectorIndividual i = (BitVectorIndividual) ind;
		if (genome.length != i.genome.length)
			return false;
		for (int j = 0; j < genome.length; j++)
			if (genome[j] != i.genome[j])
				return false;
		return true;
	}

	public Object getGenome() {
		return genome;
	}

	public void setGenome(Object gen) {
		genome = (boolean[]) gen;
	}

	public long genomeLength() {
		return genome.length;
	}
	
	public String toString() {
		String str = "[";
		for(int i = 0; i< genome.length; i++) {
			str += genome[i]?"1":"0";
		}
		if(str.length() >= 3)
			str = str.substring(0, str.length()-2) + "]";
		return str;
	}

}
