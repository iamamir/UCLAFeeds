/*package dao.user;

import java.util.List;

import javax.persistence.Query;

import models.db.user.UserResult;
import core.db.ConnectOpt;
import dao.common.ShardPersistenceManager;


public class UserResultDAO extends ShardPersistenceManager<UserResult> {

	private static UserResultDAO instance = new UserResultDAO();

	public static UserResultDAO getInstance() {
		return instance;
	}

	public Object[] getSumOfUserResultsByUserId(long userId, long shardId) {
		Query q = createNamedQuery("UserResult.getSumOfUserResultsByUserId", new ConnectOpt().setShardId(shardId));
		q.setParameter("userId", userId);
    	q.setMaxResults(1);
		List<Object[]> userResults = q.getResultList();
		return userResults!=null && !userResults.isEmpty() ? userResults.get(0) : null;
	}
}*/