/**
 * 
 */
package dao.common;

import java.io.Serializable;
import java.util.List;

import core.db.ConnectOpt;




/**
 * @author ali.muhammad
 * 
 */
public interface PersistenceManager<F extends Serializable> {

	/* Create/Update/Delete */
	void add(F persistable, ConnectOpt opt) throws PersistenceException;

	F addOrUpdate(F persistable, ConnectOpt opt) throws PersistenceException;

	F update(F persistable, ConnectOpt opt) throws PersistenceException;

	void delete(F persistable, ConnectOpt opt) throws PersistenceException;

	public int delete(String criteria, ConnectOpt opt) throws PersistenceException;

	/* Read */
	
	F findById(Serializable Id, ConnectOpt opt) throws PersistenceException;
	
	List<F> findAll(ConnectOpt opt) throws PersistenceException;

	List<F> findByCriteria(String where, ConnectOpt opt) throws PersistenceException;

	List<F> maxResults(String where, int start, int max, ConnectOpt opt) throws PersistenceException;

//	List<F> maxResults(String where, int start, int max, long[] count, ConnectOpt opt) throws PersistenceException;

	public F uniqueResult(String criteria, ConnectOpt opt) throws PersistenceException;

	long count(String criteria, ConnectOpt opt) throws PersistenceException;
}
