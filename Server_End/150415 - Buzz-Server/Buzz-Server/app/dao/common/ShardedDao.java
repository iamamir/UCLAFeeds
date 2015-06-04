package dao.common;

import javax.persistence.Query;

import models.db.common.ShardedEntity;
import dao.shard.ShardManager;
import dao.shard.ShardManager.AccessMode;

public class ShardedDao extends GenericDao {
	
	public static Query createNamedQuery(String name) {
		return createNamedQuery(name, AccessMode.READ_ONLY);
	}
	
	public static Query createNamedQuery(String name, AccessMode accessMode) {
		long shardId = ShardManager.getShardForCurrentThread();
		if (shardId == 0 || accessMode == AccessMode.READ_WRITE) {
		return GenericDao.createNamedQuery(name, null);
	}
		else {
			return GenericDao.createNamedQuery(name, ShardManager.getShardDS(accessMode, shardId));
		}
	}
	
	public static Query createNativeQuery(String name) {
		return createNativeQuery(name, AccessMode.READ_ONLY);
	}
	
	public static Query createNativeQuery(String name, AccessMode accessMode) {
		long shardId = ShardManager.getShardForCurrentThread();
		if (shardId == 0 || accessMode == AccessMode.READ_WRITE) {
		return GenericDao.createNativeQuery(name, null);
	}
		else {
			return GenericDao.createNativeQuery(name, ShardManager.getShardDS(accessMode, shardId));	
		}
	}
	
	public static void create(ShardedEntity entity) {
		GenericDao.create(entity, null);
	}
	
	public static void delete(ShardedEntity entity) {
		GenericDao.delete(entity, null);
	}
	
	public static void update(ShardedEntity entity) {
		GenericDao.update(entity, null);
	}
	
	public static void flush() {
		GenericDao.flush(null);
	}
	
	public static void clear() {
		GenericDao.clear(null);
	}

}
