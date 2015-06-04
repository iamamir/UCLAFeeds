package dao.common;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import constant.Constants;
import constant.Constants.DB_MODE;
import core.db.ConnectOpt;
import core.db.DBConnectManager;

public abstract class MasterPersistenceManager<F extends Serializable> extends AbstractPersistenceManager<F> {

	/**
	 * 
	 */
	protected MasterPersistenceManager() {
	}
	
	public void create(F persistable) {
		add(persistable, new ConnectOpt());
	};
	
	public void add(F persistable) {
		add(persistable, new ConnectOpt());
	};

	public F addOrUpdate(F persistable) {
		return addOrUpdate(persistable, new ConnectOpt());
	};

	public void delete(F persistable) {
		delete(persistable, new ConnectOpt());
	};

	public F update (F persistable) {
		return update(persistable, new ConnectOpt());
	};
	
	public F findById(Serializable id) {
		return findById(id, new ConnectOpt());
	}
	
	public List<F> findByCriteria(String where) {
		return findByCriteria(where, new ConnectOpt());
	}
	
	public List<F> findAll() {
		return findAll(new ConnectOpt());
	}
	
	public List<F> maxResults(String criteria, int start, int max)  {
		return maxResults(criteria, start, max, new ConnectOpt());
	}
	
	public long count(String criteria) {
		return count(criteria, new ConnectOpt());
	}
	public Query createNativeQuery(String query) throws PersistenceException {
		return createNativeQuery(query, new ConnectOpt().setMode(DB_MODE.READ));
	}
	
	public Query createNamedQuery(String query) throws PersistenceException {
		return createNamedQuery(query, new ConnectOpt().setMode(DB_MODE.READ));
	}
	
	@Override
	protected EntityManager getSession(ConnectOpt opt) {
		EntityManager em = null;
		if ( opt.getMode() == DB_MODE.WRITE ) {
			em = DBConnectManager.writeEm(Constants.DB_MASTER);
		} else if ( opt.getMode() == DB_MODE.READ ) {
			em = DBConnectManager.readEm(Constants.DB_MASTER);
		} else {
			em = DBConnectManager.em(Constants.DB_MASTER);
		}
		return em;
	}

}
