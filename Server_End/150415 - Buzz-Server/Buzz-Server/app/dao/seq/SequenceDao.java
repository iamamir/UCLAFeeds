package dao.seq;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.Table;

import play.db.jpa.JPA;

public class SequenceDao {

private static SequenceDao instance = new SequenceDao();
	
	public static SequenceDao getInstance() {
		return instance;
	}
	
	private  String getTableName(Class<?> entityClass) {
		Table dbTable = entityClass.getAnnotation(Table.class);
		String tableName = dbTable.name();
		return tableName;
	}
	
	public  long getNext(Class<?> entityClass) {
		EntityManager em = null;
        EntityTransaction tx = null;
        try {
            
        	 em = JPA.em("default");
             
             tx = em.getTransaction();
             tx.begin();
             
             String tableName = getTableName(entityClass);
     		String query = "UPDATE seq_" + tableName + " SET id = LAST_INSERT_ID(id + 1)";
     		Query q = em.createNativeQuery(query);
     		q.executeUpdate();
     		q = em.createNativeQuery("SELECT LAST_INSERT_ID()");
     		BigInteger seq = (BigInteger)q.getSingleResult();
     		
     		long result = seq.longValue();
     		
             if(tx != null) {
                 if(tx.getRollbackOnly()) {
                     tx.rollback();
                 } else {
                     tx.commit();
                 }
             }
             
             return result;
            
        } catch(Throwable t) {
            if(tx != null) {
                try { tx.rollback(); } catch(Throwable e) {}
            }
            throw new RuntimeException(t);
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }        
}
