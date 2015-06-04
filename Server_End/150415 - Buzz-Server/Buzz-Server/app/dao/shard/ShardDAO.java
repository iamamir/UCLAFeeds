
package dao.shard;


import java.util.List;
import javax.persistence.Query;
import models.db.shard.Shard;
import dao.common.NonShardedDao;


public class ShardDAO extends NonShardedDao
{

    private static ShardDAO instance = new ShardDAO();

    public static ShardDAO getInstance()
    {
        return instance;
    }

    public List<Shard> selectEnabledShards(long time)
    {
        Query q = createNamedQuery("Shard.getEnabledShards");
        q.setParameter("startedAt", time);
        return q.getResultList();
    }
}
