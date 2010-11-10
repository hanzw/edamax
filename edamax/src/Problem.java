import java.util.logging.Logger;

/**
 * A generalization of a problem.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public abstract class Problem {
	
	PropertyReader props;
	Logger logger;
	
	public Problem(PropertyReader props) {
		this.props = props;
		this.logger = Logger.getLogger("EC");
	}

	public abstract double evaluate(Individual t);

	public abstract boolean isIdeal(Individual t);
	
}
