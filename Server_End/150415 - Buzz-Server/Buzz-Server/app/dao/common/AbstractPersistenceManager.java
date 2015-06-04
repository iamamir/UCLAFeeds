package dao.common;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import play.Logger;
import core.db.ConnectOpt;

enum Action {
	ADD, DELETE, UPDATE, ADD_OR_UPDATE
};

public abstract class AbstractPersistenceManager<F extends Serializable>
		implements PersistenceManager<F> {
	// Tablename holder..?
	Class<F> persistentClass;

	/**
	 * looks up the table where this object is related to.
	 */
	@SuppressWarnings("unchecked")
	public AbstractPersistenceManager() {

		Type t = getClass().getGenericSuperclass();
		Type arg;
		if (t instanceof ParameterizedType) {
			arg = ((ParameterizedType) t).getActualTypeArguments()[0];
		} else if (t instanceof Class) {
			arg = ((ParameterizedType) ((Class<?>) t).getGenericSuperclass())
					.getActualTypeArguments()[0];
		} else {
			throw new RuntimeException("Can not handle type construction for '"
					+ getClass() + "'!");
		}

		if (arg instanceof Class) {
			this.persistentClass = (Class<F>) arg;
		} else if (arg instanceof ParameterizedType) {
			this.persistentClass = (Class<F>) ((ParameterizedType) arg)
					.getRawType();
		} else {
			throw new RuntimeException(
					"Problem determining generic class for '" + getClass()
							+ "'! ");
		}
	}

	/**
	 * 
	 */
	private F doAction(Action action, F object, ConnectOpt opt)
			throws PersistenceException {
		F result = null;
		try {
			EntityManager em = getSession(opt);
			Logger.debug("getCanonicalName: "
					+ this.getClass().getCanonicalName());

			switch (action) {
			case ADD_OR_UPDATE:
				result = em.merge(object);
				break;
			case ADD:
				em.persist(object);
				break;
			case DELETE:
				if (em.getTransaction().isActive()) {
					em.remove(em.contains(object) ? object : em.merge(object));
				} else {
					em.getTransaction().begin();
					em.remove(object);
					em.getTransaction().commit();
				}
				break;
			case UPDATE:
				result = em.merge(object);
				break;
			}
			// em.flush();
			// em.clear();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
		return result;
	}

	public void create(F persistable, ConnectOpt opt)
			throws PersistenceException {
		doAction(Action.ADD, persistable, opt);
	};

	public void add(F persistable, ConnectOpt opt) throws PersistenceException {
		doAction(Action.ADD, persistable, opt);
	};

	public F addOrUpdate(F persistable, ConnectOpt opt)
			throws PersistenceException {
		return doAction(Action.ADD_OR_UPDATE, persistable, opt);
	};

	public void delete(F persistable, ConnectOpt opt)
			throws PersistenceException {
		doAction(Action.DELETE, persistable, opt);
	};

	public F update(F persistable, ConnectOpt opt) throws PersistenceException {
		return doAction(Action.UPDATE, persistable, opt);
	};

	/**
	 * Should not be used for production. It looks testing feature...
	 */
	public int delete(String criteria, ConnectOpt opt)
			throws PersistenceException {
		int delete = 0;
		Class<F> clazz = getPersistentClass();
		if (criteria == null || criteria.trim().isEmpty()) {
			throw new PersistenceException("Criteria cannot be empty");
		}
		try {
			EntityManager em = getSession(opt);
			Logger.debug("getCanonicalName: "
					+ this.getClass().getCanonicalName());
			delete = em.createQuery(
					"delete from " + clazz.getCanonicalName() + " " + criteria)
					.executeUpdate();
			// em.flush();
			// em.clear();
			return delete;
		} catch (Throwable e) {
			Logger.debug("Errored", e);
			throw new PersistenceException(e);
		}
	}

	@Override
	public F findById(Serializable id, ConnectOpt opt)
			throws PersistenceException {
		try {
			EntityManager em = getSession(opt);
			LockModeType lockMode = opt.getLock();
			F obj = (F) em.find(getPersistentClass(), id, lockMode);
			return obj;
		} catch (Exception e) {
			Logger.debug("Errored", e);
			throw new PersistenceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public F uniqueResult(String where, ConnectOpt opt)
			throws PersistenceException {
		try {
			EntityManager em = getSession(opt);
			String name = getPersistentClass().getSimpleName().toLowerCase();
			Query query = em.createQuery("select " + name + " from "
					+ getPersistentClass().getCanonicalName() + " as " + name
					+ " " + where);
			F instance = (F) query.getSingleResult();
			return instance;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	public List<F> findAll(ConnectOpt opt) throws PersistenceException {
		return findByCriteria("", opt);
	}

	@SuppressWarnings("unchecked")
	public List<F> findByCriteria(String where, ConnectOpt opt)
			throws PersistenceException {
		try {
			EntityManager em = getSession(opt);
			String name = getPersistentClass().getSimpleName().toLowerCase();
			Query query = em.createQuery("select " + name + " from "
					+ getPersistentClass().getCanonicalName() + " as " + name
					+ "  " + where);
			List<?> list = (List<?>) query.getResultList();
			return (List<F>) list;
		} catch (Throwable e) {
			throw new PersistenceException(e);
		}
	}

	public Query createNativeQuery(String query) throws PersistenceException {
		EntityManager em = getSession(new ConnectOpt());
		return em.createNativeQuery(query);
	}

	public Query createNamedQuery(String query) throws PersistenceException {
		EntityManager em = getSession(new ConnectOpt());
		return em.createNamedQuery(query);
	}

	public Query createNativeQuery(String query, ConnectOpt opt)
			throws PersistenceException {
		EntityManager em = getSession(opt);
		return em.createNativeQuery(query);
	}

	public Query createNamedQuery(String query, ConnectOpt opt)
			throws PersistenceException {
		EntityManager em = getSession(opt);
		return em.createNamedQuery(query);
	}

	public Query createQuery(String query, ConnectOpt opt)
			throws PersistenceException {
		EntityManager em = getSession(opt);
		return em.createQuery(query);
	}

	/**
	 * Orders needed?
	 * 
	 * @param criteria
	 * @param start
	 * @param max
	 * @param opt
	 * @return
	 * @throws PersistenceException
	 */
	@SuppressWarnings("unchecked")
	public List<F> maxResults(String criteria, int start, int max,
			ConnectOpt opt) throws PersistenceException {
		try {
			EntityManager em = getSession(opt);
			String name = getPersistentClass().getSimpleName().toLowerCase();
			Query query = em.createQuery("select " + name + " from "
					+ getPersistentClass().getCanonicalName() + " as " + name
					+ "  " + criteria);
			query.setFirstResult(start);
			query.setMaxResults(max);
			List<?> list = (List<?>) query.getResultList();
			return (List<F>) list;
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	// @Override
	// public List<F> maxResults(String where, int start, int max, long[] count)
	// throws PersistenceException {
	// count[0] = count(where);
	// return maxResults(where, start, max);
	// }

	public long count(String criteria, ConnectOpt opt)
			throws PersistenceException {
		try {
			EntityManager em = getSession(opt);
			Query query = em
					.createQuery("select count(*) FROM "
							+ getPersistentClass().getCanonicalName() + "  "
							+ criteria);
			List<?> list = query.getResultList();
			return list.size() > 0 ? (Long) list.get(0) : 0;
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	protected abstract EntityManager getSession(ConnectOpt opt);

	// protected abstract EntityManager getReadSession();
	// protected abstract EntityManager getWriteSession();
	// protected abstract EntityManager getCurrentSession();

	protected Class<F> getPersistentClass() {
		return persistentClass;
	}

}
