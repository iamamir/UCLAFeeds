package core.db;

import javax.persistence.LockModeType;

import constant.Constants.DB_MODE;
import dao.shard.ShardManager;

public class ConnectOpt {
	private long shardId;
	private long shardKey;
	private DB_MODE mode;
	private LockModeType lock;

	public ConnectOpt() {
		this.lock = LockModeType.NONE;
		this.mode = DB_MODE.AUTO;
		this.shardId = 0;
		this.shardKey = 0;
	}

	/*
	 * Setters
	 */
	public ConnectOpt setMode(DB_MODE mode) {
		this.mode = mode;
		return this;
	}

	public ConnectOpt setLock(LockModeType lock) {
		this.lock = lock;
		return this;
	}

	public ConnectOpt setShardId(long shardId ) {
		this.shardId = shardId;
		return this;
	}

	public ConnectOpt setShardKey(long userId) {
		this.shardKey = userId;
		return this;
	}

	/*
	 * Getters
	 */


	public DB_MODE getMode() {
		return this.mode;
	}

	public LockModeType getLock() {
		if( this.mode == DB_MODE.WRITE) {
			return this.lock;
		}
		return LockModeType.NONE;
	}

	public long getShardId() {
		if( this.shardId > 0) {
			return this.shardId;
		}
		if ( this.shardKey > 0 ) {
			long shardId = ShardManager.getShardIdByUserId(this.shardKey);
			if(shardId>0){
				return shardId;
			}
			else {
				shardId = ShardManager.getShardForCurrentThread();
				if (shardId > 0) {
					return shardId;
				}
			}
		}
		throw new RuntimeException("Invalid shard sepcification");
	}

}
