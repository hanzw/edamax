import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.MersenneTwisterFast;


public class MultiRosenbrockTest {

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
		int bigM = 150000;
		MultiRosenbrock multiRos = new MultiRosenbrock();
		int vecSize = 4;
		double best = Double.NEGATIVE_INFINITY;
		
		multiRos.setGenomeMax( 5.12 );
		multiRos.setGenomeMin( -5.12 );
		MersenneTwisterFast rand = new MersenneTwisterFast();
		DoubleVectorIndividual ind = new DoubleVectorIndividual(vecSize, rand);
		ind.setGenomeMin( multiRos.getGenomeMin() );
		ind.setGenomeMax( multiRos.getGenomeMax() );
		for( int i = 0; i < bigM ; i++) {
			double ret;
			ind.replaceRandomly();
			ret = multiRos.evaluate(ind);
			if( ret > best) {
				best = ret;
				System.out.println( "paramM: "+vecSize+" ret: "+ret+" "+ind.toString());
			}
		}
		
		double ret;
		double[] floop = { 1.0, 1.0, 1.0, 1.0 };
		ind.setGenome(floop);
		ret = multiRos.evaluate(ind);
		System.out.println( "paramM: "+vecSize+" ret: "+ret+" "+ind.toString());

		floop = new double[] { -1.0, -1.0, -1.0, -1.0 };
		ind.setGenome(floop);
		ret = multiRos.evaluate(ind);
		System.out.println( "paramM: "+vecSize+" ret: "+ret+" "+ind.toString());

		floop = new double[] { 2.0, 2.0, 2.0, 2.0 };
		ind.setGenome(floop);
		ret = multiRos.evaluate(ind);
		System.out.println( "paramM: "+vecSize+" ret: "+ret+" "+ind.toString());

	}

	@Test
	public final void testIsIdeal() {
		fail("Not yet implemented"); // TODO
	}

}
