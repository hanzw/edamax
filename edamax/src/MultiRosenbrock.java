
public class MultiRosenbrock extends Problem 
    {

	public MultiRosenbrock() {
		genomeMax = 1.0;
		genomeMin = -1.0;
	}
	
	public MultiRosenbrock(PropertyReader props) {
		super(props);
		// TODO Auto-generated constructor stub
	}
	
	double rosenbrock(final double[] realVals) {
		double retval = 0.0;
		for (int i = 1; i < realVals.length; i++) {
			retval += 100.0 * (realVals[i - 1] * realVals[i - 1] - realVals[i])
					* (realVals[i - 1] * realVals[i - 1] - realVals[i])
					+ (1.0 - realVals[i - 1]) * (1.0 - realVals[i - 1]);
		}
		retval = (realVals.length -1 ) * 101.0 - retval;
		return retval;
	}

	public double evaluate(Individual ind) {
		double[] realVals;

		DoubleVectorIndividual locInd;
		locInd = (DoubleVectorIndividual) ind;

		realVals = (double[]) locInd.getGenome();

		double functionValue = rosenbrock(realVals);

		return functionValue;
	}

	@Override
	public boolean isIdeal(Individual t) {
		boolean retVal = false;
		double optimal;
		double indVal;
		indVal = evaluate(t);
		DoubleVectorIndividual tConv;
		tConv = (DoubleVectorIndividual)t;
		optimal = (((double[])tConv.getGenome()).length -1) * 101.0;
		if( optimal - indVal < 1e-6 ) retVal = true;
		return retVal;
	}

}
