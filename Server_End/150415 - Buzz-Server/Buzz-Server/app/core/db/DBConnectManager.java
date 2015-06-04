
package core.db;


import java.util.HashMap;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import play.Logger;
import play.db.jpa.JPA;


/**
 * Extended JPA Helper
 *
 * @author mori.yuichiro
 *
 */
public class DBConnectManager
{
    static ThreadLocal<HashMap<String, EntityManager>> openEntityManager = new ThreadLocal<HashMap<String, EntityManager>>();
    static ThreadLocal<HashMap<String, EntityTransaction>> openTransaction = new ThreadLocal<HashMap<String, EntityTransaction>>();
    static ThreadLocal<Boolean> writeMode = new ThreadLocal<Boolean>();

    public static void setup(Boolean writeOn)
    {
        Logger.debug("setup");
        openEntityManager.set(new HashMap<String, EntityManager>());
        openTransaction.set(new HashMap<String, EntityTransaction>());
        writeMode.set(writeOn);
    }

    public static void close()
    {
        Logger.debug("close");

        HashMap<String, EntityManager> map = openEntityManager.get();
        for (Entry<String, EntityManager> entry : map.entrySet())
        {
            Logger.debug("close:" + entry.getKey());
            entry.getValue().close();
        }

        openEntityManager.set(null);
        writeMode.set(null);
        openTransaction.set(null);
    }

    /**
     * Returns EM for read-only
     *
     * @param key
     * @return
     */
    public static EntityManager readEm(String key)
    {
        Logger.debug("em: " + key);
        HashMap<String, EntityManager> openedEms = openEntityManager.get();

        String dsKey = key + "_ro";
        Logger.debug("Checking:" + dsKey);
        if (openedEms.containsKey(dsKey))
        {
            // There is opened connection
            Logger.debug("Found: returning: " + dsKey);
            return (EntityManager) openedEms.get(dsKey);
        }

        EntityManager em = JPA.em(dsKey);
        openedEms.put(dsKey, em);

        Logger.debug("Opened: returning: " + dsKey);
        return em;
    }

    /**
     * Returns EM for write
     *
     * @param key
     * @return
     */
    public static EntityManager writeEm(String key)
    {
        Logger.debug("em: " + key);
        HashMap<String, EntityManager> openedEms = openEntityManager.get();

        String dsKey = key;// + WRITE_DATASOURCE_SUFFIX;
        Logger.debug("Checking:" + dsKey);
        if (openedEms.containsKey(dsKey))
        {
            // There is opened connection
            Logger.debug("Found: returning: " + dsKey);
            return (EntityManager) openedEms.get(dsKey);
        }

        EntityManager em = JPA.em(dsKey);
        openedEms.put(dsKey, em);

        EntityTransaction tx = em.getTransaction();

        // Transaction Start
        tx.begin();

        HashMap<String, EntityTransaction> openedTxs = openTransaction.get();
        openedTxs.put(dsKey, tx);

        Logger.debug("Opened: returning: " + dsKey);
        return em;
    }

    /**
     *
     * @param key
     * @return
     */
    public static EntityManager em(String key)
    {
        Boolean writeOn = writeMode.get();
        if (writeOn == null)
        {
            writeOn = Boolean.FALSE;
        }
        EntityManager em = null;
        if (writeOn)
        {
            em = writeEm(key);
        }
        else
        {
            em = readEm(key);
        }

        return em;
    }

    public static void commit()
    {
        HashMap<String, EntityTransaction> map = openTransaction.get();

        for (Entry<String, EntityTransaction> entry : map.entrySet())
        {
            Logger.debug("commit:" + entry.getKey());
            entry.getValue().commit();
        }
    }

    public static void rollback()
    {
        HashMap<String, EntityTransaction> map = openTransaction.get();

        for (Entry<String, EntityTransaction> entry : map.entrySet())
        {
            Logger.debug("rollback:" + entry.getKey());
            entry.getValue().rollback();
        }
    }

}
