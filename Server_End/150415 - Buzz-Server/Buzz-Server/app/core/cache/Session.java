package core.cache;

import play.cache.Cache;
import util.Util;

public class Session {

	private static final int defaultTimeOut = 5 * 60;	// seconds
	
	public static enum Namespace {
		USERS_BY_LEVEL ("search_users_by_lv"),
		MISSION_SAVE_RESULT ("mission_save_result"),
		MISSION_STAGE_BOSS_RESULT ("mission_stage_boss_result"),
		MISSION_ENEMY_RESULT ("mission_enemy_result"),
		MISSION_BATTLE_RESULT ("mission_battle_result"),
		MISSION_BATTLE_ASSETS_POINT ("mission_battle_assets_point"),
		USER_SHARD_MAP("user_shard_map");
		
		private String value;
		
		private Namespace(String value) {
			this.value = value;
		}
		
		public String value() {
			return this.value;
		}

	}
	
	public static Object read(long userId, String key) {
		if (!Session.isValidKey(key)) {
			return null;
		}
		String cacheKey = userId + ":" + key;
		return Cache.get(cacheKey);
	}

	public static void writeWithKey(long userId, String key, Object value, int expiry) {
		String cacheKey = userId + ":" + key;
		Cache.set(cacheKey, value, expiry);
	}
	
	public static void writeWithKey(long userId, String key, Object value) {
		writeWithKey(userId, key, value, defaultTimeOut);
	}
	
	public static void writeWithRandomKey(long userId, Object value) {
		int randomNumber = (int)Util.getRandomNumberInRange(1, 100000);
		writeWithKey(userId, String.valueOf(randomNumber), value, defaultTimeOut);
	}
	
	public static void store(String key, Object value) {
		Cache.set(key, value, defaultTimeOut);
	}
	
	public static Object retrieve(String key) {
		
		if (!Session.isValidKey(key)) {
			return null;
		}
		return Cache.get(key);
	}
	
	public static void delete(long userId, String key) {
		if (!Session.isValidKey(key)) {
			//TODO: Uncomment the following code when it's support is available in the underlying framework (not available in 2.0)
			//Cache.remove(key)
		}
	}
	
	private static boolean isValidKey(String key) {
		return (key != null && key.length() <= 255);
	}
	
	
}
