public class MultiRastrigin extends Problem {

	double paramA;

	public MultiRastrigin() {
		genomeMax = 5.12;
		genomeMin = -5.12;
		paramA = 10;
	}

	public MultiRastrigin(PropertyReader props) {
		super(props);
		paramA = 10;
		// TODO Auto-generated constructor stub
	}

	double rastrigin(final double[] realVals) {
		double retval = 0.0;

		for (int i = 0; i < realVals.length; i++) {
			double tmpVal = 0.0;
			tmpVal = realVals[i] * realVals[i] + paramA
					* (1.0 - Math.cos(2.0 * Math.PI * realVals[i]));
			retval += tmpVal;
//			System.out.println( "\t\ti: "+i+" tmpVal: " + tmpVal+ " retval: "+retval);
		}
		retval = realVals.length * (paramA + 1.0) - retval;
		return retval;
	}

	public double evaluate(Individual ind) {
		double[] realVals;

		DoubleVectorIndividual locInd;
		locInd = (DoubleVectorIndividual) ind;

		realVals = (double[]) locInd.getGenome();

		double functionValue = rastrigin(realVals);

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
		optimal = ((double[])tConv.getGenome()).length * (paramA + 1.0);
		if( optimal - indVal < 1e-6 ) retVal = true;
		return retVal;
	}

}
