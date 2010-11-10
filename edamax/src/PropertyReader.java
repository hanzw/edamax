import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class PropertyReader extends Properties {

	Logger logger;
	Properties sysprops;

	public PropertyReader() {
		super();
		this.logger = Logger.getLogger("EC");
		this.sysprops = System.getProperties();
	}

	/**
	 * Override of load function to overwrite the inputstream's properties with
	 * system properties when relevant
	 */
	public void load(InputStream inStream) throws IOException {
		super.load(inStream);

		// find overriding keys
		Set sys = new HashSet(sysprops.keySet());
		Set loc = new HashSet(this.keySet());
		sys.retainAll(loc);
		Iterator it = sys.iterator();
		while (it.hasNext()) {
			String tmp = (String) it.next();
			this.setProperty(tmp, sysprops.getProperty(tmp));
		}
	}

	/**
	 * Get a property from the properties file.
	 * 
	 * @param name
	 *            The name of the parameter
	 * @param def
	 *            The default value of the parameter
	 * @return The value of the parameter, if it exists, or def if it doesn't.
	 * @throws Exception
	 */
	public String getStringProperty(String name, String def) throws Exception {
		String val = def;
		try {
			if (this.containsKey(name)) {
				val = this.getProperty(name).trim();
			} else {
				logger.warning(name + " not specified! Using default: " + def);
			}
		} catch (Exception e) {
			System.err.println("Error reading property: " + name);
			System.err.println(e.getMessage());
			throw e;
		}
		return val;
	}

	/**
	 * Get a property from the properties file.
	 * 
	 * @param name
	 *            The name of the parameter
	 * @param def
	 *            The default value of the parameter
	 * @return The value of the parameter, if it exists, or def if it doesn't.
	 * @throws Exception
	 */
	public double getDoubleProperty(String name, double def) throws Exception {
		double val = def;
		try {
			if (this.containsKey(name)) {
				val = Double.parseDouble(this.getProperty(name).trim());
			} else {
				logger.warning(name + " not specified! Using default: " + def);
			}
		} catch (Exception e) {
			System.err.println("Error reading property: " + name);
			System.err.println(e.getMessage());
			throw e;
		}
		return val;
	}

	/**
	 * Get a property from the properties file.
	 * 
	 * @param name
	 *            The name of the parameter
	 * @param def
	 *            The default value of the parameter
	 * @return The value of the parameter, if it exists, or def if it doesn't.
	 * @throws Exception
	 */
	public int getIntProperty(String name, int def) throws Exception {
		int val = def;
		try {
			if (this.containsKey(name)) {
				val = Integer.parseInt(this.getProperty(name).trim());
			} else {
				logger.warning(name + " not specified! Using default: " + def);
			}
		} catch (Exception e) {
			System.err.println("Error reading property: " + name);
			System.err.println(e.getMessage());
			throw e;
		}
		return val;
	}

	/**
	 * Get a property from the properties file.
	 * 
	 * @param name
	 *            The name of the parameter
	 * @param def
	 *            The default value of the parameter
	 * @return The value of the parameter, if it exists, or def if it doesn't.
	 * @throws Exception
	 */
	public boolean getBooleanProperty(String name, boolean def)
			throws Exception {
		boolean val = def;
		try {
			if (this.containsKey(name)) {
				val = Boolean.parseBoolean(this.getProperty(name).trim());
			} else {
				logger.warning(name + " not specified! Using default: " + def);
			}
		} catch (Exception e) {
			System.err.println("Error reading property: " + name);
			System.err.println(e.getMessage());
			throw e;
		}
		return val;
	}

	/**
	 * Get a property from the properties file.
	 * 
	 * @param name
	 *            The name of the parameter
	 * @param def
	 *            The default value of the parameter
	 * @return The value of the parameter, if it exists, or def if it doesn't.
	 * @throws Exception
	 */
	public long getLongProperty(String name, long def) throws Exception {
		long val = def;
		try {
			if (this.containsKey(name)) {
				val = Long.parseLong(this.getProperty(name).trim());
			} else {
				logger.warning(name + " not specified! Using default: " + def);
			}
		} catch (Exception e) {
			System.err.println("Error reading property: " + name);
			System.err.println(e.getMessage());
			throw e;
		}
		return val;
	}

}
