
package dao.buzz;


import java.util.List;
import javax.persistence.Query;
import models.db.buzz.BuzzOfvcard;
import constant.BuzzDbQueries;
import dao.common.MasterPersistenceManager;


public class BuzzOfvcardDao extends MasterPersistenceManager<BuzzOfvcard>
{
    private static BuzzOfvcardDao instance = new BuzzOfvcardDao();

    public static BuzzOfvcardDao getInstance()
    {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public BuzzOfvcard selectBuzzOfvcard(String userName)
    {
        Query query = createNamedQuery(BuzzDbQueries.BuzzOfvcard_getUserName);
        query.setParameter("userName", userName);
        query.setMaxResults(1);

        List<BuzzOfvcard> result = query.getResultList();
        return result != null && !result.isEmpty() ? result.get(0) : null;
    }

    public BuzzOfvcard insertBuzzOfvcard(BuzzOfvcard BuzzOfvcardObject)
    {
        return BuzzOfvcardDao.getInstance().addOrUpdate(BuzzOfvcardObject);
    }

    public BuzzOfvcard updateBuzzOfvcard(BuzzOfvcard BuzzOfvcardObject)
    {
        return BuzzOfvcardDao.getInstance().update(BuzzOfvcardObject);
    }

    public void deleteBuzzOfvcard(BuzzOfvcard BuzzOfvcardObject)
    {
        BuzzOfvcardDao.getInstance().delete(BuzzOfvcardObject);
    }
}
