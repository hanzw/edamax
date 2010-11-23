public class MultiMTQ extends Problem {

	double[][] paramX;
	double[] paramH;
	double[] paramS;
	int paramN = 6;
	int paramM = 4;

	public MultiMTQ() {
		// a default problem
		
		paramM = 4;
		paramN = 4;
		paramX = new double[][] { { 0.25, 0.5, 0.75, 0.89 },
				{ 0.125, 0.5, 0.75, 0.89 }, 
				{ 0.25, 0.5, 0.64, 0.75 },
				{ 0.075, 0.25, 0.5, 0.75 },
				{ 0.25, 0.5, 0.75, 0.89 }, 
				{ 0.5, 0.75, 0.89, 0.99 } };
		paramH = new double[] { 150.0, 100.0, 25.0, 50.0 };
		paramS = new double[] { 8.0, 30.0, 14.0, 20.0 };
	}
	
	public MultiMTQ(PropertyReader props) {
		
		super(props);

		paramM = 4;
		paramN = 4;
		paramX = new double[][] { { 0.25, 0.5, 0.75, 0.89 },
				{ 0.125, 0.5, 0.75, 0.89 }, 
				{ 0.25, 0.5, 0.64, 0.75 },
				{ 0.075, 0.25, 0.5, 0.75 },
				{ 0.25, 0.5, 0.75, 0.89 }, 
				{ 0.5, 0.75, 0.89, 0.99 } };
		paramH = new double[] { 150.0, 100.0, 25.0, 50.0 };
		paramS = new double[] { 8.0, 30.0, 14.0, 20.0 };

		// TODO Auto-generated constructor stub
	}

	double mMQT(final double[] realVals) {
		double retval = 0.0;
		double[] tmpVals = new double[paramM];
		for (int j = 0; j < paramM; j++) {
			tmpVals[j] = 0.0;
			double moTmp = 0.0;
			for (int i = 1; i < paramN; i++) {
				moTmp += (realVals[i] - paramX[j][i])
						* (realVals[i] - paramX[j][i]) / paramS[i];
			}
			tmpVals[j] = paramH[j] * (1.0 - moTmp);
		}

		for (int j = 0; j < paramM; j++) {
			if (tmpVals[j] > retval) {
				retval = tmpVals[j];
			}
		}

		return retval;
	}

	public double evaluate(Individual ind) {
		double[] realVals;
		DoubleVectorIndividual locInd;
		locInd = (DoubleVectorIndividual) ind;

		realVals = (double[]) locInd.getGenome();

		double functionValue = mMQT(realVals);

		return functionValue;
	}

	@Override
	public boolean isIdeal(Individual t) {
		boolean retVal = false;
		double ideal = Double.NEGATIVE_INFINITY;
		for( int i = 0; i < paramH.length; i++) {
			if( ideal < paramH[i]) {
				ideal = paramH[i];
			}
		}
		double foo = evaluate(t);
		if( (ideal - foo) < 1e-6 ) retVal = true;  
		// TODO Auto-generated method stub
		return retVal;
	}

}
