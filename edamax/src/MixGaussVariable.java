import java.lang.reflect.Array;
import java.util.Vector;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.*;


public class MixGaussVariable {
	int numKern = 2;
	double[] mean, stdDev, wt;
	double[] rhoPrev;
	double min = 0.0, max = 1.0;
	long totCount = 0l;

	// why? because PHI is golden...
	public static final double PHI = 1.618033988749894848;
	
	public double probForX( double x ) {
		double retval = 0.0;
		NormalDistributionImpl norm;

		for( int i = 0; i < numKern; i++ ) {
			norm = new NormalDistributionImpl(mean[i], stdDev[i] );
			retval += wt[i] * norm.density(x);
		}
		return retval;
	}

	public double cumProbForX( double x ) {
		double retval = 0.0;
		NormalDistributionImpl norm;

		for( int i = 0; i < numKern; i++ ) {
			norm = new NormalDistributionImpl(mean[i], stdDev[i] );

 			try {
				retval += wt[i] * norm.cumulativeProbability( x );
			} catch (MathException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return retval;
	}

	public double drawForP( double p ) {
		final double eps = 1e-9;
		double retval = 0.0;
		double lowPicket, curTest, incSize;
		// quick incremental search - can be improved mightily
	
		incSize = (max - min) / (numKern + 1.0); // as a starting point
		retval = lowPicket = min;
		while( incSize / Math.abs(max) > eps ) {
			curTest = lowPicket + incSize;
			double locProb;
			locProb = cumProbForX( curTest );
			if( locProb > p ) {
				incSize /= PHI;
			} else {
				retval = lowPicket + incSize;
				incSize = retval - lowPicket;
				lowPicket = retval;
			}
		}
		return retval;		
	}
	
	private void adder( double x) {
		double mindist = max - min;
		double thresh = (max - min) / (double)(PHI*numKern);
		if( numKern >= 10 ) return;
		
		
		for( int i = 0; i < numKern; i++ ) {
			double d = Math.abs( x - mean[i]) ; 
			if( d < mindist ) {
				mindist = d;
			}
		}
		if( mindist > thresh ) {
			double tmp = 0.0;
			tmp = probForX(x);
			if( tmp > 1.0 / (4.0 * numKern)) {
//				System.out.println( "\t\taborting kernel add - adequately covered: "+x+" prob: "+tmp );
				return;
			}

			// add a new kernel
			double[] newmean, newsd, newwt, newrho;
			newmean = new double[numKern+1];
			System.arraycopy(mean, 0, newmean, 0, mean.length);
			newmean[numKern] = x;
			mean = newmean;

			newsd = new double[numKern+1];
			System.arraycopy(stdDev, 0, newsd, 0, stdDev.length);
			newsd[numKern] = mindist;
			stdDev = newsd;
			
			newwt = new double[numKern+1];
			System.arraycopy(wt, 0, newwt, 0, wt.length);
			newwt[numKern] = 1.0/(2.0*numKern);
			wt = newwt;
			// normalize weights
			double tmpAccum = 0.0;
			for( int i = 0; i < numKern+1; i++) tmpAccum += wt[i];
			for( int i = 0; i < numKern+1; i++) wt[i] /= tmpAccum;

			newrho = new double[numKern+1];
			System.arraycopy(rhoPrev, 0, newrho, 0, rhoPrev.length);
			newrho[numKern] = 10.0*Double.MIN_VALUE;
			rhoPrev = newrho;

			/*
			System.out.println("\t\tNew Kernel: x: "+ x +
					" probx: "+ probForX(x)+
					" newnk: "+ (numKern+1) +
					" newsd: "+ stdDev[numKern]+
					" newwt: "+ wt[numKern]);
			*/
			numKern++;
		}
	}
	
	private void subtracter() {
		int numRed = 0;
		double thresh = 1e-4;
		
		if( numKern < 2 ) return;
		
		for( int j = 0; j < numKern; j++) {
			if( wt[j] < thresh) numRed++;
		}
		if( numRed < 1 ) return;
		
		double[] newmean, newsd, newwt, newrho;
		newmean = new double[numKern-numRed];
		newsd = new double[numKern-numRed];
		newwt = new double[numKern-numRed];
		newrho = new double[numKern-numRed];
		
		int ctr = 0;
		for( int i = 0; i < numKern; i++) {
			if( wt[i] < thresh) continue;
			newmean[ctr] = mean[i];
			newsd[ctr] = stdDev[i];
			newwt[ctr] = wt[i];
			newrho[ctr] = rhoPrev[i];
			ctr++;
		}

		mean = newmean;
		stdDev = newsd;	
		wt = newwt;
		rhoPrev = newrho;
		
		// normalize weights
		double tmpAccum = 0.0;
		for( int i = 0; i < numKern-numRed; i++) tmpAccum += wt[i];
		for( int i = 0; i < numKern-numRed; i++) wt[i] /= tmpAccum;
		
		numKern -= numRed;		
		
		System.out.println("\t\tRemoved Kernels: "+ numRed + " nk: "+numKern);
	}
	
	public void updMixForSingle( double x ) {
		double pi, mu, sig;
		double px;
		double t = 10.0, tInv = 1.0/t;
		double[] rho;

		// NOTE: numKern may change
		subtracter();
		adder(x);

		totCount++;
		t = 10.0 + (double) numKern;
		tInv = 1.0/(t+1.0);
		rho = new double[numKern];
		px = probForX(x);		
		// going to update each of the distributions in the current mix
		for( int j = 0; j < numKern; j++ ) {
			double  pxPij=0.0;
			NormalDistributionImpl n = new NormalDistributionImpl(mean[j], stdDev[j]);
			// pi[j,t] * p(x|pi[j,t]) / p(x)
			pxPij = n.density(x);

			if( px < (10.0*Double.MIN_VALUE) ) {
				px += 10.0*Double.MIN_VALUE;
				pxPij += 10.0*Double.MIN_VALUE;
			}
			rho[j] = wt[j] * pxPij / px;
			pi = wt[j] + tInv * (rho[j] - wt[j]);
			
			double piInv = (1.0)/pi;
			mu = mean[j] + tInv * rho[j] * (x-mean[j]);
			
			double oldSig = stdDev[j];
			sig = oldSig + tInv * rho[j] * ((x-mean[j])*(x-mean[j]) - oldSig);
			
//			System.out.print("\t\tpx: "+px+" pxPij: "+pxPij+" mu: "+mean[j]+" sd: "+stdDev[j]);
//			System.out.println( "\tsamp: "+x+" tInv: "+tInv+" rho: "+rho[j]+" pi: "+pi+" mu: "+mu+" sig: "+sig);
			
			// assuming these aren't badly broken....
			mean[j] = mu;
			stdDev[j] = sig;
			wt[j] = pi;
		}
		for( int j = 0; j < numKern; j++) {
			rhoPrev[j] = rho[j];
		}
	}
	
	public void updMixForSamples( int count, double[] x ) {
		for( int i = 0; i < count; i++ )
			updMixForSingle(x[i]);
	}
	
	public MixGaussVariable() {
		min = 0.0; max = 1.0;
		mean = new double[ numKern ];
		for( int i = 0; i < numKern; i++ ) {
			mean[i] = (max - min) * (i+1.0) / (numKern+1.0) + min;
		}
		stdDev = new double[ numKern ];
		for( int i = 0; i < numKern; i++ ) {
			stdDev[i] = (max - min) / ( 2 * (numKern+1.0) );
		}
		wt = new double[ numKern ];
		for( int i = 0; i < numKern; i++ ) {
			wt[i] = 1.0 / (numKern);
		}
		rhoPrev = new double[ numKern ];
	}

	public MixGaussVariable( double small, double large, int nK, double[] mu, double[] sig, double[] weight) {
		min = small; 
		max = large;
		numKern = nK;
		mean = mu.clone();
		stdDev = sig.clone();
		wt = weight.clone();
		rhoPrev = new double[ numKern ];
	}
	
	public String toString() {
		String retVal = "";
		return retVal;
	}

}
