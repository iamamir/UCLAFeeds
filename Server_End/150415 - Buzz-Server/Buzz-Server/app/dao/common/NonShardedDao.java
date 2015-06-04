package dao.common;

import javax.persistence.Query;

import models.db.common.DbEntity;
import dao.shard.ShardManager.AccessMode;

public class NonShardedDao extends GenericDao {

	private final static String defaultDS_RW = "default";
	private final static String defaultDS_RO = "default_ro";
	
	public static Query createNamedQuery(String name) {
		return NonShardedDao.createNamedQuery(name, AccessMode.READ_ONLY);
	}
	
	public static Query createNamedQuery(String name, AccessMode accessMode) {
		if (accessMode == AccessMode.READ_ONLY) {
			return GenericDao.createNamedQuery(name, NonShardedDao.defaultDS_RO);
		}
		else {
			return GenericDao.createNamedQuery(name, NonShardedDao.defaultDS_RW);
		}
	}
	
	public static Query createNativeQuery(String name) {
		return NonShardedDao.createNativeQuery(name, AccessMode.READ_ONLY);
	}
	
	public static Query createNativeQuery(String name, AccessMode accessMode) {
		if (accessMode == AccessMode.READ_ONLY) {
			return GenericDao.createNativeQuery(name, NonShardedDao.defaultDS_RO);
		}
		else {
			return GenericDao.createNativeQuery(name, NonShardedDao.defaultDS_RW);
		}
	}
	
	public static void create(DbEntity entity) {
		GenericDao.create(entity, NonShardedDao.defaultDS_RW);
	}
	
	public static void delete(DbEntity entity) {
		GenericDao.delete(entity, NonShardedDao.defaultDS_RW);
	}
	
	public static void update(DbEntity entity) {
		GenericDao.update(entity, NonShardedDao.defaultDS_RW);
	}
	
	public static void flush() {
		GenericDao.flush(NonShardedDao.defaultDS_RW);
	}
	
	public static void clear() {
		GenericDao.clear(NonShardedDao.defaultDS_RW);
	}
}
