import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.MersenneTwisterFast;


public class MultiRastriginTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testEvaluate() {
		int bigM = 1500;
		MultiRastrigin multiRas = new MultiRastrigin();
		int vecSize = 4;
		double best = Double.NEGATIVE_INFINITY;
		
		MersenneTwisterFast rand = new MersenneTwisterFast();
		DoubleVectorIndividual ind = new DoubleVectorIndividual(vecSize, rand);
		ind.setGenomeMin( multiRas.getGenomeMin() );
		ind.setGenomeMax( multiRas.getGenomeMax() );
		for( int i = 0; i < bigM ; i++) {
			double ret;
			ind.replaceRandomly();
			ret = multiRas.evaluate(ind);
			if( ret > best) {
				best = ret;
				System.out.println( "paramM: "+vecSize+" ret: "+ret+" "+ind.toString());
			}
		}
		
		double ret;
		double[] floop = { 0.0, 0.0, 0.0, 0.0 };
		ind.setGenome(floop);
		ret = multiRas.evaluate(ind);
		System.out.println( "paramM: "+vecSize+" ret: "+ret+" "+ind.toString());
		/*
		floop = new double[] { 0.0, 0.9217, 0.0, 0.0 };
		ind.setGenome(floop);
		ret = multiRas.evaluate(ind);
		System.out.println( "paramM: "+vecSize+" ret: "+ret+" "+ind.toString());

		floop = new double[] { 0.0, 0.0, 0.9217, 0.0 };
		ind.setGenome(floop);
		ret = multiRas.evaluate(ind);
		System.out.println( "paramM: "+vecSize+" ret: "+ret+" "+ind.toString());
		*/

	}

	@Test
	public final void testIsIdeal() {
		fail("Not yet implemented"); // TODO
	}

}
