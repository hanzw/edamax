import java.util.Formatter;

import util.MersenneTwisterFast;


public class DoubleVectorIndividual extends Individual {

	public double[] genome;
	public int genome_size = -1;

	double genomeMin = -1.0;
	double genomeMax = 1.0;
	
	/**
	 * @return the genomeMin
	 */
	public double getGenomeMin() {
		return genomeMin;
	}

	/**
	 * @param genomeMin the genomeMin to set
	 */
	public void setGenomeMin(double genomeMin) {
		this.genomeMin = genomeMin;
	}

	/**
	 * @return the genomeMax
	 */
	public double getGenomeMax() {
		return genomeMax;
	}

	/**
	 * @param genomeMax the genomeMax to set
	 */
	public void setGenomeMax(double genomeMax) {
		this.genomeMax = genomeMax;
	}

	public DoubleVectorIndividual(MersenneTwisterFast rand) {
		super(rand);
		// TODO Auto-generated constructor stub
	}

	public DoubleVectorIndividual(int genome_size, MersenneTwisterFast rand) {
		super(rand);
		this.genome_size = genome_size;
		this.evaluated = false;
		this.ideal = false;
		genome = new double[genome_size];
	}

	/**
	 * Replace genome with coin flips computed from a given probability
	 * distribution (given as a probability vector)
	 * 
	 * @param p
	 *            The probability vector.
	 */
	public void replaceUsingProbabilities(MixGaussVariable[] m) {
		double rVal;
		rVal = (genomeMax - genomeMin) * rand.nextDouble() + genomeMin;
		for (int i = 0; i < genome_size; i++) {
			genome[i] = m[i].drawForP( rVal );
		}
		this.evaluated = false;
	}
	
	/**
	 * Repalce genome with random coin flips.
	 */
	public void replaceRandomly() {
		double rVal;

		for (int i = 0; i < genome_size; i++) {
			rVal = (genomeMax - genomeMin) * rand.nextDouble() + genomeMin;
			genome[i] = rVal;
		}
		this.evaluated = false;
	}

	/**
	 * Make a copy of the BitVectorIndividual.
	 */
	public Object clone() {
		DoubleVectorIndividual tmp = (DoubleVectorIndividual) (super.clone());
		tmp.genome = (double[])genome.clone();
		tmp.genome_size = genome_size;
		return tmp;
	}

	/**
	 * Check whether two DoubleVectors are equal
	 */
	public boolean equals(Object ind) {
		if (!(this.getClass().equals(ind.getClass())))
			return false;
		DoubleVectorIndividual i = (DoubleVectorIndividual) ind;
		if (genome.length != i.genome.length)
			return false;
		for (int j = 0; j < genome.length; j++)
			if (Double.compare(genome[j], i.genome[j])!=0)
				return false;
		return true;
	}

	public int compareTo(Object o) {
		double tmp = ((DoubleVectorIndividual) o).fitness;
		if (fitness > tmp)
			return -1;
		else if (fitness < tmp)
			return 1;
		else
			return 0;
	}


	public Object getGenome() {
		return genome;
	}

	public void setGenome(Object gen) {
		genome = (double[]) gen;
	}

	public long genomeLength() {
		return genome.length;
	}
	
	public String toString() {
		Formatter pprint = new Formatter();
		String str = "[";
		for(int i = 0; i< genome.length; i++) {
			pprint.format( "%6.4f:", genome[i]);
//			System.out.println( "\tgenome_length: "+genome.length+"\ti: "+i+" genome[i]: "+genome[i]);
//			str += genome[i]?"1":"0";
		}
		str += pprint.toString();
		if(str.length() >= 3)
			str = str.substring(0, str.length()-1) + "]";
		return str;
	}

}
