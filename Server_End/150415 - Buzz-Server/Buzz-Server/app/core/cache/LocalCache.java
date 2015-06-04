
package core.cache;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LocalCache
{

    private static Map<String, Map<Long, ? extends Object>> POOL = new HashMap<String, Map<Long, ? extends Object>>();

    public enum Namespace
    {
        USER_SHARD_MAP("user_shard_map"),
        MISSION_CARD_MAP("mission_card_map"),
        USER_LEVEL("user_level"),
        CARD("card"),
        GACHA("gacha"),
        ITEM("item"),
        CARD_LEVEL("card_level"),
        GACHA_LOT_CARD_ID_RATE("GachaLot:card_id_rate"),
        MISSION_LOT_BY_MISSION_ID("mission_lot_by_mission_id"),
        USER_SHARDS("user_shards"),
        GACHA_CARD_ID("Gacha:cardIds");

        private String namespace;

        private Namespace(String namespace)
        {
            this.namespace = namespace;
        }

        public String value()
        {
            return this.namespace;
        }
    }

    public static void delete(String namespace, Long key)
    {
        Map<Long, Object> pool = (HashMap<Long, Object>) POOL.get(namespace);
        if (pool != null)
        {
            pool.remove(key);
        }
    }

    public static void deleteMulti(String namespace, List<Long> keys)
    {
        if (keys != null && keys.size() > 0)
        {
            Map<Long, Object> pool = (HashMap<Long, Object>) POOL.get(namespace);
            if (pool != null)
            {
                for (Long key : keys)
                {
                    pool.remove(key);
                }
            }
        }
    }

    public static void deleteNamespace(String namespace)
    {
        POOL.remove(namespace);
    }

    public static Map<Long, Object> getMulti(String namespace, List<Long> keys)
    {
        if (namespace != null && keys != null && keys.size() > 0)
        {
            Map<Long, Object> pool = (HashMap<Long, Object>) POOL.get(namespace);
            if (pool != null)
            {
                Map<Long, Object> result = new HashMap<Long, Object>();
                for (Long key : keys)
                {
                    if (pool.containsKey(key))
                    {
                        result.put(key, pool.get(key));
                    }
                }
                return result;
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

    public static Object get(String namespace, Long key)
    {
        Map<Long, Object> result = getMulti(namespace, Arrays.asList(key));
        if (result != null)
        {
            return result.get(key);
        }
        else
        {
            return null;
        }
    }

    public static void setMulti(String namespace, Map<Long, ? extends Object> hash)
    {
        if (namespace != null && hash != null)
        {
            if (POOL.containsKey(namespace) == false)
            {
                POOL.put(namespace, hash);
            }
            else
            {
                Map<Long, Object> pool = (HashMap<Long, Object>) POOL.get(namespace);
                if (pool != null)
                {
                    pool.putAll(hash);
                }
            }
        }
    }

    public static void set(String namespace, Long key, Object value)
    {
        Map<Long, Object> map = new HashMap<Long, Object>();
        map.put(key, value);
        setMulti(namespace, map);
    }

    public static Object cacheable(String namespace, Long key, play.libs.F.Function<Long, Object> block) throws Throwable
    {
        Object value = get(namespace, key);
        if (value == null)
        {
            value = block.apply(key);
            set(namespace, key, value);
        }
        return value;
    }

    public static Map<Long, ? extends Object> cacheableMulti(String namespace, List<Long> keys, play.libs.F.Function<List<Long>, Map<Long, ? extends Object>> block) throws Throwable
    {
        Map<Long, Object> onCache = getMulti(namespace, keys);
        List<Long> notCached = new ArrayList<Long>();
        if (onCache != null)
        {
            for (Long key : keys)
            {
                if (onCache.containsKey(key) == false)
                {
                    notCached.add(key);
                }
            }
        }
        else
        {
            onCache = new HashMap<Long, Object>();
            notCached.addAll(keys);
        }
        if (notCached.size() > 0)
        {
            Map<Long, ? extends Object> result = block.apply(notCached);
            setMulti(namespace, result);
            onCache.putAll(result);
        }
        return onCache;
    }
}
