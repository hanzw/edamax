import java.util.logging.Logger;

/**
 * A generalization of a problem.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public abstract class Problem {
	
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

	PropertyReader props;
	Logger logger;
	
	public Problem() {
		// empty default
	}
	
	public Problem(PropertyReader props) {
		this.props = props;
		this.logger = Logger.getLogger("EC");
	}

	public abstract double evaluate(Individual t);

	public abstract boolean isIdeal(Individual t);
	
}
