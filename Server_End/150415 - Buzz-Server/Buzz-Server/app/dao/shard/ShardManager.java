
package dao.shard;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.db.shard.Shard;
import models.db.shard.UserShard;
import play.db.jpa.JPA;
import util.Util;
import com.google.common.collect.Lists;
import core.cache.LocalCache;
import core.cache.LocalCache.Namespace;


public class ShardManager
{

    private static ThreadLocal<Long> shardInfo = new ThreadLocal<Long>();

    public static class ShardInfo
    {
        private long shardId;
        private long userId;

        public ShardInfo()
        {

        }

        public ShardInfo(long userId, long shardId)
        {
            this.userId = userId;
            this.shardId = shardId;
        }

        public long getShardId()
        {
            return shardId;
        }

        public void setShardId(long shardId)
        {
            this.shardId = shardId;
        }

        public long getUserId()
        {
            return userId;
        }

        public void setUserId(long userId)
        {
            this.userId = userId;
        }

    };

    public static void setShardInfo(Long shardId)
    {
        shardInfo.set(new Long(shardId));
    }

    public static <T> T withTransaction(long shardId, AccessMode accessMode, play.libs.F.Function0<T> block) throws Throwable
    {
        try
        {
            shardInfo.set(new Long(shardId));
            String name = ShardManager.getShardDS(accessMode, shardId);
            return JPA.withTransaction(name, accessMode == AccessMode.READ_ONLY, block);
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
        finally
        {
            shardInfo.set(null);
        }
    }

    public static enum AccessMode
    {
        READ_ONLY("_ro"),
        READ_WRITE("_rw");

        String value;
        private final String DEFAULT_VALUE = "R";

        private AccessMode(String value)
        {
            this.value = value;
            if (this.value == null || (this.value != null && this.value.length() <= 0))
            {
                this.value = this.DEFAULT_VALUE;
            }
        }

        public String value()
        {
            return this.value;
        }

        public static AccessMode buildMode(boolean readOnly)
        {
            return readOnly ? READ_ONLY : READ_WRITE;
        }
    }

    public static long getShardForCurrentThread()
    {
        Long shardId = shardInfo.get();
        if (shardId == null)
        {
            return 0;
        }
        else
        {
            return shardInfo.get().longValue();
        }
    }

    public static String getShardDS(AccessMode accessMode, long shardId)
    {
        StringBuilder sb = new StringBuilder("shard");
        sb.append(shardId);
        if (accessMode == AccessMode.READ_ONLY)
        {
            sb.append(accessMode.value());
        }
        return sb.toString();
    }

    public static long getShardIdByUserId(long userId)
    {
        if (userId > 0)
        {
            Map<Long, Long> shardsMap = getShardIdsMapByUserIds(Lists.newArrayList(new Long(userId)));
            if (shardsMap != null)
            {
                Long shardId = shardsMap.get(userId);
                return shardId == null ? 0 : shardId.longValue();
            }
        }
        return 0;
    }

    public static Map<Long, Long> getShardIdsMapByUserIds(List<Long> userIds)
    {
        if (userIds == null || userIds.isEmpty())
        {
            return null;
        }
        try
        {
            Map<Long, ? extends Object> cacheResult = LocalCache.cacheableMulti(Namespace.USER_SHARD_MAP.value(), userIds,
                    new play.libs.F.Function<List<Long>, Map<Long, ? extends Object>>() {
                        public Map<Long, ? extends Object> apply(List<Long> userIds)
                        {
                            Map<Long, Long> shardMap = new HashMap<Long, Long>();
                            List<UserShard> userShards = UserShardDAO.getInstance().selectByUserIds(userIds);
                            if (userShards == null)
                            {
                                return null;
                            }
                            for (UserShard us : userShards)
                            {
                                shardMap.put(us.getUserId(), us.getShardId());
                            }
                            return shardMap;
                        }
                    });
            if (cacheResult != null)
            {
                return (Map<Long, Long>) cacheResult;
            }
            else
            {
                return null;
            }
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
    }

    public static Map<Long, List<Long>> getListofUserIdsByShardIdsMap(List<Long> userIds)
    {
        Map<Long, Long> shardIdsByUserIds = getShardIdsMapByUserIds(userIds);
        if (shardIdsByUserIds == null)
        {
            return null;
        }
        else
        {
            Map<Long, List<Long>> result = new HashMap<Long, List<Long>>();
            for (Long key : shardIdsByUserIds.keySet())
            {
                long shardId = shardIdsByUserIds.get(key);
                if (!result.containsKey(shardId))
                {
                    List<Long> newList = new ArrayList<Long>();
                    result.put(shardId, newList);
                }
                List<Long> listOfUserIds = result.get(shardId);
                if (listOfUserIds != null)
                {
                    listOfUserIds.add(key);
                }
            }
            return result;
        }
    }

    public static Shard getRandomShard()
    {
        List<Shard> shards = ShardDAO.getInstance().selectEnabledShards(Util.currentTimeInSecs());
        if (shards != null && shards.size() > 0)
        {
            Map<Integer, Long> weights = new HashMap<Integer, Long>();
            for (int i = 0; i < shards.size(); i++)
            {
                weights.put(i, shards.get(i).getWeight());
            }
            Integer index = Util.getLot(weights, null);
            if (index != null)
            {
                return shards.get(index);
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public List<Long> lotShardIds(int num)
    {
        long now = System.currentTimeMillis() / 1000;
        List<Shard> allActiveshards = ShardDAO.getInstance().selectEnabledShards(now);
        Map<Integer, Long> shardLots = new HashMap<Integer, Long>();
        for (int i = 0; i < allActiveshards.size(); i++)
        {
            shardLots.put(i, allActiveshards.get(i).getId());
        }
        if (allActiveshards.size() < num)
        {
            return (List<Long>) shardLots.values();
        }
        List<Long> shardIds = new ArrayList<Long>();
        for (int i = 1; i <= num; i++)
        {
            Integer id = Util.getLot(shardLots, null);
            if (id != null)
            {
                Long shardId = shardLots.get(id);
                shardIds.add(shardId);
                shardLots.remove(id);
            }
        }
        return shardIds;
    }

}
