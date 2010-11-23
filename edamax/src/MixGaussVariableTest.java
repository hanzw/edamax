import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.MersenneTwisterFast;

/**
 * 
 */

/**
 * @author uxmal
 *
 */
public class MixGaussVariableTest {

	static double[] min = {-3.0, 0.0 };
	static double[] max = {3.0, 1.0 };
	static NormalDistributionImpl[] normImpl = new NormalDistributionImpl[4];
	static MersenneTwisterFast rand;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rand = new MersenneTwisterFast( 1724l );
		normImpl[0] = new NormalDistributionImpl( 0.0, 1.0 );
		normImpl[1] = new NormalDistributionImpl( 0.5, 0.5 );
		normImpl[2] = new NormalDistributionImpl( 0.5, 0.0125 );
		normImpl[3] = new NormalDistributionImpl( -0.5, 0.0125 );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link MixGaussVariable#cumProbForX(double)}.
	 * @throws MathException 
	 */
	@Test
	public final void testProbForX() throws MathException {
		double[] mu = { normImpl[0].getMean() };
		double[] s = { normImpl[0].getStandardDeviation() };
		double[] wt = {1.0};
		double expect = 0.5;
		MixGaussVariable mix1 = new MixGaussVariable( min[0], max[0], 1, mu, s, wt ); 
		double targ = normImpl[0].inverseCumulativeProbability( expect );
		System.out.print( "expect: "+ expect + " targ: "+targ);
		double test;
		test = mix1.cumProbForX( targ );
		System.out.println( " test: "+test);
		assertEquals( expect, test, 1e-5);
		expect = 0.95;
		targ = normImpl[0].inverseCumulativeProbability( expect );
		System.out.print( "expect: "+ expect + " targ: "+targ);
		test = mix1.cumProbForX( targ );
		System.out.println( " test: "+test);
		assertEquals( expect, test, 1e-5);
		
		mu = new double[] { normImpl[0].getMean(), normImpl[1].getMean() };
		s = new double[] { normImpl[0].getStandardDeviation(), normImpl[1].getStandardDeviation()};
		wt = new double[] { 0.8, 0.2 };
		mix1 = new MixGaussVariable( min[0], max[0], 2, mu, s, wt );
		targ = rand.nextDouble() * (max[0] - min[0]) + min[0];

		double expect1, expect2;
		expect1 = normImpl[0].cumulativeProbability(targ);
		expect2 = normImpl[1].cumulativeProbability(targ);
		expect = wt[0]*expect1 + wt[1]*expect2;
		System.out.print( "expect: "+ expect + " targ: "+targ);
		test = mix1.cumProbForX( targ );
		System.out.println( " test: "+test);
		assertEquals( expect, test, 1e-4);
	}

	/**
	 * Test method for {@link MixGaussVariable#drawForP(double)}.
	 * @throws MathException 
	 */
	@Test
	public final void testDrawForP() throws MathException {
		double[] mu = { normImpl[0].getMean() };
		double[] s = { normImpl[0].getStandardDeviation() };
		double[] wt = {1.0};
		MixGaussVariable mix1 = new MixGaussVariable( min[0], max[0], 1, mu, s, wt ); 
		double targ = 0.0;
		double expect = normImpl[0].cumulativeProbability(targ);
//		System.out.print( "expect: "+ expect + " targ: "+targ);
		double test;
		test = mix1.drawForP( expect );
//		System.out.println( " test: "+test);
		assertEquals( targ, test, 1e-4);
		targ = -0.1;
		expect = normImpl[0].cumulativeProbability(targ);
//		System.out.print( "expect: "+ expect + " targ: "+targ);
		test = mix1.drawForP( expect );
//		System.out.println( " test: "+test);
		assertEquals( targ, test, 1e-4);

//		expect: 0.9754613326044956 targ: 1.8709886221167888 test: 0.9754613326044956
		mu = new double[] { normImpl[0].getMean(), normImpl[1].getMean() };
		s = new double[] { normImpl[0].getStandardDeviation(), normImpl[1].getStandardDeviation()};
		wt = new double[] { 0.8, 0.2 };
		mix1 = new MixGaussVariable( min[0], max[0], 2, mu, s, wt );

		expect = 0.9748506310408709;
		targ = 1.8709886221167888;
//		System.out.print( "expect: "+ expect + " targ: "+targ);
		test = mix1.drawForP( expect );
//		System.out.println( " test: "+test);
		assertEquals( targ, test, 1e-4);
	}

	/**
	 * Test method for {@link MixGaussVariable#updMixForSingle(double)}.
	 * @throws MathException 
	 */
	@Test
	public final void testUpdMixForSingle() throws MathException {
		double[] mu = { normImpl[0].getMean() };
		double[] s = { normImpl[0].getStandardDeviation() };
		double[] wt = {1.0};
		MixGaussVariable mix1 = new MixGaussVariable( min[0], max[0], 1, mu, s, wt ); 
		double targ = 0.0;
		System.out.println("iterated single value:");
		for( int i = 0; i < 10; i++ ) {
			mix1.updMixForSingle( targ );
			System.out.println( "\ttarg: "+targ+" mu: "+mix1.mean[0]+" sd: "+mix1.stdDev[0]+" wt: "+mix1.wt[0]);
		}
		
		mix1 = new MixGaussVariable( min[0], max[0], 1, mu, s, wt ); 
		System.out.println("iterated single normal samples:");
		for( int i = 0; i < 701; i++ ) {
			targ = normImpl[0].inverseCumulativeProbability( rand.nextDouble());
			mix1.updMixForSingle( targ );
			if( i % 70 == 0)
				for( int j = 0; j < mix1.numKern; j++)
					System.out.println( "\ttarg: "+targ+" mu: "+mix1.mean[j]+" sd: "+mix1.stdDev[j]+" wt: "+mix1.wt[j]);
		}

		mix1 = new MixGaussVariable( min[0], max[0], 1, mu, s, wt ); 
		System.out.println("iterated uniform samples:");
		for( int i = 0; i < 701; i++ ) {
//			targ = normImpl[1].inverseCumulativeProbability( rand.nextDouble());
			targ = rand.nextDouble() * (max[0]-min[0]) + min[0];
			mix1.updMixForSingle( targ );
			if( i % 70 == 0)
				for( int j = 0; j < mix1.numKern; j++)
					System.out.println( "\ttarg: "+targ+" mu: "+mix1.mean[j]+" sd: "+mix1.stdDev[j]+" wt: "+mix1.wt[j]);
		}

		mu = new double[] { -0.5, 0.5 };
		s = new double[] { 0.75, 0.75 };
		wt = new double[] {0.7, 0.3};
		mix1 = new MixGaussVariable( min[0], max[0], 2, mu, s, wt ); 
		System.out.println("iterated single (different) normal samples against dual 1:");
		for( int i = 0; i < 701; i++ ) {
			targ = normImpl[0].inverseCumulativeProbability( rand.nextDouble());
			mix1.updMixForSingle( targ );
			if( i % 70 == 0)
				for( int j = 0; j < mix1.numKern; j++)
				System.out.println( "\ttarg: "+targ+" mu: "+mix1.mean[j]+" sd: "+mix1.stdDev[j]+" wt: "+mix1.wt[j]);
		}

		mu = new double[] { -0.5, 0.5 };
		s = new double[] { 0.5, 0.5 };
		wt = new double[] {0.5, 0.5};
		mix1 = new MixGaussVariable( min[0], max[0], 2, mu, s, wt ); 
		System.out.println("iterated single (different) normal samples against dual 2:");
		for( int i = 0; i < 701; i++ ) {
			targ = normImpl[3].inverseCumulativeProbability( rand.nextDouble());
//			targ = -0.5;
			mix1.updMixForSingle( targ );
			targ = normImpl[2].inverseCumulativeProbability( rand.nextDouble());
//			targ = 0.5;
			mix1.updMixForSingle( targ );
			/*
			if( i % 70 == 0)
				for( int j = 0; j < mix1.numKern; j++)
				System.out.println( "\ttarg: "+targ+" mu: "+mix1.mean[j]+" sd: "+mix1.stdDev[j]+" wt: "+mix1.wt[j]);
			*/
		}
}

	/**
	 * Test method for {@link MixGaussVariable#updMixForSamples(int, double[])}.
	 */
	@Test
	public final void testUpdMixForSamples() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link MixGaussVariable#MixGaussVariable()}.
	 */
	@Test
	public final void testMixGaussVariable() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link MixGaussVariable#MixGaussVariable(double, double, int, double[], double[])}.
	 */
	@Test
	public final void testMixGaussVariableDoubleDoubleIntDoubleArrayDoubleArray() {
		fail("Not yet implemented"); // TODO
	}

}
