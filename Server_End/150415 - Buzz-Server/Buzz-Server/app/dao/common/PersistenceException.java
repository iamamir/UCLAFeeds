/**
 * 
 */
package dao.common;

/**
 * @author ali.muhammad
 *
 */
public class PersistenceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8015221385475769003L;

	/**
	 * 
	 */
	public PersistenceException() {
	}

	/**
	 * @param arg0
	 */
	public PersistenceException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public PersistenceException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PersistenceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
