import static org.junit.Assert.*;

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
public class MultiMTQTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	 * Test method for {@link MultiMTQ#evaluate(Individual)}.
	 */
	@Test
	public final void testEvaluate() {
		int bigM = 150000;
		MultiMTQ mtq = new MultiMTQ();
		double best = Double.NEGATIVE_INFINITY;
		double ret;
		MersenneTwisterFast rand = new MersenneTwisterFast();
		DoubleVectorIndividual ind = new DoubleVectorIndividual(mtq.paramM, rand);
		for( int i = 0; i < bigM ; i++) {
			ind.replaceRandomly();
			ret = mtq.evaluate(ind);
			if( ret > best) {
				best = ret;
				System.out.println( "mtq.paramM: "+mtq.paramM+" ret: "+ret+" "+ind.toString());
			}
		}
		double[] floop = { 0.25, 0.5, 0.75, 0.89 };
		ind.setGenome(floop);
		ret = mtq.evaluate(ind);
		System.out.println( "paramM: "+mtq.paramM+" ret: "+ret+" "+ind.toString());
	}

	/**
	 * Test method for {@link MultiMTQ#isIdeal(Individual)}.
	 */
	@Test
	public final void testIsIdeal() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link MultiMTQ#MultiMTQ(PropertyReader)}.
	 */
	@Test
	public final void testMultiMTQ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link MultiMTQ#mMQT(double[])}.
	 */
	@Test
	public final void testMMQT() {
		fail("Not yet implemented"); // TODO
	}

}
