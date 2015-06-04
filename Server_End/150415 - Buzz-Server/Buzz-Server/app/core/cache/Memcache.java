package core.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.cache.Cache;

public class Memcache {
	
	private static final int defaultTimeOut = 5 * 60;	// seconds
	
	public static Object get(String namespace, String key) {
		if (!Memcache.isValidKey(key)) {
			return null;
		}
		String cacheKey = Memcache.makeKey(namespace, key);
		return Cache.get(cacheKey);
	}
	
	public static Map<String, Object> getMulti(String namespace, List<String> keys) {
		if (keys != null && keys.size() > 0) {
			Map<String, Object> result = new HashMap<String, Object>();
			for (String key : keys) {
				result.put(key, Memcache.get(namespace, key));
			}
			return result;
		}
		else {
			return null;
		}
	}

	public static void set(String namespace, String key, Object value, int expiry) {
		String cacheKey = Memcache.makeKey(namespace, key);
		Cache.set(cacheKey, value, expiry);
	}
	
	public static void set(String namespace, String key, Object value) {
		set(namespace, key, value, defaultTimeOut);
	}
	
	public static void delete(long userId, String key) {
		if (!Memcache.isValidKey(key)) {
			//TODO: Uncomment the following code when it's support is available in the underlying framework (not available in 2.0)
			//Cache.remove(key)
		}
	}
	
	private static boolean isValidKey(String key) {
		return (key != null && key.length() <= 255);
	}
	
	private static String makeKey(String namespace, String key) {
		return namespace + ":" + key;
	}
	
}
