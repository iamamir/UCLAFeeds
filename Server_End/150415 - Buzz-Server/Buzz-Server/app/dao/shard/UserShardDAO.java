
package dao.shard;


import java.util.List;
import javax.persistence.Query;
import models.db.shard.UserShard;
import dao.common.MasterPersistenceManager;


public class UserShardDAO extends MasterPersistenceManager<UserShard>
{

    private static UserShardDAO instance = new UserShardDAO();

    public static UserShardDAO getInstance()
    {
        return instance;
    }

    public UserShard selectByUserId(long userId)
    {
        Query q = createNamedQuery("UserShard.getShardByUserId");
        q.setParameter("userId", userId);
        q.setMaxResults(1);
        List<UserShard> shards = q.getResultList();
        return shards != null && !shards.isEmpty() ? shards.get(0) : null;
    }

    public UserShard selectByLoginId(String loginId)
    {
        Query q = createNamedQuery("UserShard.getShardByLoginId");
        q.setParameter("loginId", loginId);
        q.setMaxResults(1);
        List<UserShard> shards = q.getResultList();
        return shards != null && !shards.isEmpty() ? shards.get(0) : null;
    }

    public long selectUserIdByLoginId(String loginId)
    {
        Query q = createNamedQuery("UserShard.getUserIdByLoginId");
        q.setParameter("loginId", loginId);
        q.setMaxResults(1);
        List<Long> userIds = q.getResultList();
        return userIds != null && !userIds.isEmpty() ? userIds.get(0) : -1;
    }

    public List<UserShard> selectByUserIds(List<Long> userId)
    {
        Query q = createNamedQuery("UserShard.getShardsByUserIds");
        q.setParameter("userIds", userId);
        return q.getResultList();
    }

    public List<UserShard> getAllShardUserIds()
    {
        Query q = createNamedQuery("UserShard.getAllShardUserIds");
        List<UserShard> userShardIds = q.getResultList();
        return userShardIds != null && !userShardIds.isEmpty() ? userShardIds : null;
    }

    public List<UserShard> getAllUserShardOrderedByPoints()
    {
        Query q = createNamedQuery("UserShard.getAllUserShardOrderedByPoints");
        List<UserShard> userShardIds = q.getResultList();
        return userShardIds != null && !userShardIds.isEmpty() ? userShardIds : null;
    }

    public List<UserShard> getTopTenUserShardOrderedByPoints()
    {
        Query q = createNamedQuery("UserShard.getAllUserShardOrderedByPoints");
        q.setMaxResults(10);
        List<UserShard> userShardIds = q.getResultList();
        return userShardIds != null && !userShardIds.isEmpty() ? userShardIds : null;
    }

    public UserShard getUserByFriendCode(String friendCode)
    {
        Query q = createNamedQuery("UserShard.getUserByFriendCode");
        q.setParameter("friendCode", friendCode);
        q.setMaxResults(1);
        List<UserShard> shards = q.getResultList();
        return shards != null && !shards.isEmpty() ? shards.get(0) : null;
    }

    public List<Long> getAllShards()
    {
        Query q = createNamedQuery("UserShard.getAllShards");
        List<Long> shards = q.getResultList();
        return shards != null && !shards.isEmpty() ? shards : null;
    }

    public void getAllShards(boolean bool)
    {
        List<UserShard> userShards = UserShardDAO.getInstance().getAllShardUserIds();

        for (UserShard userShard : userShards)
        {
            userShard.setShardId((userShard.getShardId() == 1) ? 2 : 1);

            UserShardDAO.getInstance().update(userShard);
        }
    }

}
