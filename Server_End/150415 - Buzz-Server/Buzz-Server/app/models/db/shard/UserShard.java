package models.db.shard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import models.db.common.NonShardedEntity;

@Entity
@Table(name = "user_shard")
@NamedQueries({
	@NamedQuery(name = "UserShard.getShardByUserId", query = "SELECT s FROM UserShard s WHERE s.userId = :userId"),
	@NamedQuery(name = "UserShard.getShardByLoginId", query = "SELECT s FROM UserShard s WHERE s.loginId = :loginId"),
	@NamedQuery(name = "UserShard.getUserIdByLoginId", query = "SELECT s.userId FROM UserShard s WHERE s.loginId = :loginId"),
	@NamedQuery(name = "UserShard.getShardsByUserIds", query = "SELECT s FROM UserShard s WHERE s.userId IN :userIds"),
	@NamedQuery(name = "UserShard.getAllShardUserIds", query = "SELECT s FROM UserShard s"),
	@NamedQuery(name = "UserShard.getAllUserShardOrderedByPoints", query = "SELECT s FROM UserShard s ORDER BY s.point desc"),
	@NamedQuery(name = "UserShard.getUserByFriendCode", query = "SELECT s FROM UserShard s WHERE s.friendCode=:friendCode "),
	@NamedQuery(name = "UserShard.getAllShards", query = "SELECT DISTINCT s.shardId FROM UserShard s")
})
public class UserShard extends NonShardedEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id", length = 10)
	long userId;
	
	@Column(name = "shard_id", length = 10)
	long shardId;
	
	@Column(name = "login_id", length = 50)
	String loginId;

	@Column(name = "friend_code", length = 255)
	String friendCode;
	
	@Column(name = "point", length = 10)
	long point;
	
	@Column(name = "rank", length = 10)
	long rank;
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getShardId() {
		return shardId;
	}

	public void setShardId(long shardId) {
		this.shardId = shardId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getFriendCode() {
		return friendCode;
	}

	public void setFriendCode(String friendCode) {
		this.friendCode = friendCode;
	}

	public long getPoint() {
		return point;
	}

	public void setPoint(long point) {
		this.point = point;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

}
