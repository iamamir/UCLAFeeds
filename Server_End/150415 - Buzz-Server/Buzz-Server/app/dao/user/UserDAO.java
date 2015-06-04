/*package dao.user;

import java.util.List;

import javax.persistence.Query;

import models.db.user.User;
import core.db.ConnectOpt;
import dao.common.ShardPersistenceManager;


public class UserDAO extends ShardPersistenceManager<User> {

	private static UserDAO instance = new UserDAO();

	public static UserDAO getInstance() {
		return instance;
	}

	public User selectUserByUserId(String userId) {
		Query q = createNamedQuery("UserLogin.getUserByUserId");
		q.setParameter("userId", userId);
    	q.setMaxResults(1);
		List<User> users = q.getResultList();
		return users!=null && !users.isEmpty() ? users.get(0) : null;
	}

	public User selectUserByUserIdAndPassword(long userId, String password, long shardId) {
		Query q = createNamedQuery("User.getUserByUserIdAndPassword", new ConnectOpt().setShardId(shardId));
		q.setParameter("userId", userId);
		q.setParameter("password", password);
    	q.setMaxResults(1);
		List<User> users = q.getResultList();
		return users!=null && !users.isEmpty() ? users.get(0) : null;
	}

	public User select(long userId) {
		return select(userId, 0);
	}
	*//**
	 * @param userId
	 * @returns the user specified by userId
	 *//*
	public  User select(long userId, long shardId) {
		Query q = null;
		if (shardId > 0) {
			q = createNamedQuery("User.getUser", new ConnectOpt().setShardId(shardId));
		}
		else {
			q = createNamedQuery("User.getUser", new ConnectOpt().setShardKey(userId));
		}
		q.setParameter("userId", userId);
		q.setMaxResults(1);
		List<User> users = q.getResultList();
		return users!=null && !users.isEmpty() ? users.get(0) : null;
	}

	public  User selectByMobageId(String mobageId) {
		Query q = createNamedQuery("User.getUserByMobageId");
		q.setParameter("mobageId", mobageId);
		q.setMaxResults(1);
		List<User> users = q.getResultList();
		return users!=null && !users.isEmpty() ? users.get(0) : null;
	}


	public  List<Long> getUserIdsByLevel(long userLevel) {
		Query q = createNamedQuery("User.getUsersIdsByLevel");
		q.setParameter("userLevel", userLevel);
		List<Long> ids = q.getResultList();
		return ids!=null && ids.size() > 0 ? ids : null;
	}


	public  List<Long> getUserIdsByLevel(long fromUserLevel,long toUserLevel) {
		Query q = createNamedQuery("User.getUsersIdsByLevel");
		q.setParameter("fromUserLevel", fromUserLevel);
		q.setParameter("toUserLevel", toUserLevel);
		q.setParameter("recLimit", 10);
		List<Long> ids = q.getResultList();
		return ids!=null && ids.size() > 0 ? ids : null;
	}


	public  List<User> getUsersByIds(List<Long> userIds,long shardId) {
		Query q = createNamedQuery("User.getUsersByIds",new ConnectOpt().setShardId(shardId));
		q.setParameter("userIds", userIds);
		List<User> users = q.getResultList();
		return users!=null && !users.isEmpty() ? users : null;
	}

	public  List<User> getUsersByIds(List<Long> userIds,int limit,long shardId) {
		Query q = createNamedQuery("User.getUsersByIds",new ConnectOpt().setShardId(shardId));
		q.setParameter("userIds", userIds);
		q.setMaxResults(limit);
		List<User> users = q.getResultList();
		return users!=null && !users.isEmpty() ? users : null;
	}

	public  List<User> getSortedUsersExceptFriends(List<Long> friendsIds, int limit,long shardId) {
		Query q = createNamedQuery("User.getSortedUsersExceptFriends",new ConnectOpt().setShardId(shardId));
		q.setParameter("userIds", friendsIds);
		q.setMaxResults(limit);
		List<User> users = q.getResultList();
		return users!=null && !users.isEmpty() ? users : null;
	}
	*//**
	 * *
	 * @param user
	 * @param shardId
	 * @return
	 *//*
	public  Boolean updateUserWithTransaction(final User user,long shardId){
		//String datasource = ShardManager.getShardDS(AccessMode.READ_WRITE, shardId);

				try {
					return ShardManager.withTransaction(
							shardId,
							AccessMode.READ_WRITE,
							new play.libs.F.Function0<Boolean>() {
								public Boolean apply() throws Throwable {
									update(user, new ConnectOpt());
									return true;
								}
							}
					);
				}
				catch(Throwable t) {
					throw new RuntimeException(t);
				}
	}

	public  List<User> getSuggestedUsers(long fromUserLevel,long toUserLevel,List<Long> levels, long limit,long shardId) {
		Query q = createNamedQuery("User.getUsersByLevel",new ConnectOpt().setShardId(shardId));
		q.setParameter("fromUserLevel", fromUserLevel);
		q.setParameter("toUserLevel", toUserLevel);
		q.setParameter("levels", levels);
		//q.setParameter("recLimit", limit);
		q.setMaxResults((int)limit);
		List<User> users = q.getResultList();
		return users!=null && users.size() > 0 ? users : null;
	}

	public void insertUser(UserLogin userLogin) {
		JPA.em().persist(userLogin);
	}


}*/