package dao.common;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import models.db.common.DbEntity;
import play.db.jpa.JPA;

public abstract class GenericDao {
	
	protected static Query createNamedQuery(String name, String datasource) {
		EntityManager em = (datasource == null) ? JPA.em() : JPA.em(datasource);
		return em.createNamedQuery(name);
	}
	
	protected static Query createNativeQuery(String name, String datasource) {
		EntityManager em = (datasource == null) ? JPA.em() : JPA.em(datasource);
		return em.createNativeQuery(name);
	}
	
	protected static void create(DbEntity entity, String datasource) {
		EntityManager em = (datasource == null) ? JPA.em() : JPA.em(datasource);
		EntityTransaction tx = null;
		if (datasource != null) {
			 tx = em.getTransaction();
			 tx.begin();
		}
		em.persist(entity);
		if(tx != null) {
            if(tx.getRollbackOnly()) {
                tx.rollback();
            } else {
                tx.commit();
            }
        }
	}
	
	protected static void delete(DbEntity entity, String datasource) {
		EntityManager em = (datasource == null) ? JPA.em() : JPA.em(datasource);
		EntityTransaction tx = null;
		if (datasource != null) {
			 tx = em.getTransaction();
			 tx.begin();
		}
		em.remove(entity);
		if(tx != null) {
            if(tx.getRollbackOnly()) {
                tx.rollback();
            } else {
                tx.commit();
            }
        }
	}
	
	protected static void update(DbEntity entity, String datasource) {
		EntityManager em = (datasource == null) ? JPA.em() : JPA.em(datasource);
		EntityTransaction tx = null;
		if (datasource != null) {
			 tx = em.getTransaction();
			 tx.begin();
		}
		em.merge(entity);
		if(tx != null) {
            if(tx.getRollbackOnly()) {
                tx.rollback();
            } else {
                tx.commit();
            }
        }
	}
	
	protected static void flush(String datasource) {
		EntityManager em = (datasource == null) ? JPA.em() : JPA.em(datasource);
		EntityTransaction tx = null;
		if (datasource != null) {
			 tx = em.getTransaction();
			 tx.begin();
		}
		em.flush();
		if(tx != null) {
            if(tx.getRollbackOnly()) {
                tx.rollback();
            } else {
                tx.commit();
            }
        }
	}
	
	protected static void clear(String datasource) {
		EntityManager em = (datasource == null) ? JPA.em() : JPA.em(datasource);
		EntityTransaction tx = null;
		if (datasource != null) {
			 tx = em.getTransaction();
			 tx.begin();
		}
		em.clear();
		if(tx != null) {
            if(tx.getRollbackOnly()) {
                tx.rollback();
            } else {
                tx.commit();
            }
        }
	}

	
}
