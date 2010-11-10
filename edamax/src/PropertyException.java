import java.util.logging.Logger;

/**
 * An exception encountered while parsing parameters.
 * 
 * @author Christopher Vo (cvo1@gmu.edu)
 */
public class PropertyException extends Exception {
	
	private static final long serialVersionUID = 1L;
	Logger logger;

	public PropertyException() {
		logger = Logger.getLogger("EC");
	}

	public PropertyException(String message) {
		super(message);
		logger = Logger.getLogger("EC");
		logger.severe(message);
	}

	public PropertyException(Throwable cause) {
		super(cause);
		logger = Logger.getLogger("EC");
	}
	
	public PropertyException(String message, Throwable cause) {
		super(message, cause);
		logger = Logger.getLogger("EC");
		logger.severe(message);
	}

}
