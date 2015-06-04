/**
 *
 */

package dao.common;


import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import models.db.common.ShardedEntity;
import constant.Constants;
import constant.Constants.DB_MODE;
import core.db.ConnectOpt;
import core.db.DBConnectManager;
import dao.shard.ShardManager;


public abstract class ShardPersistenceManager<F extends Serializable> extends AbstractPersistenceManager<F>
{

    @Deprecated
    public long getCountByUserId(int userId)
    {
        return count("where user_id = " + userId, new ConnectOpt().setShardKey(userId));
    }

    @Deprecated
    public void deleteByUserId(int userId)
    {
        delete("where user_id = " + userId, new ConnectOpt().setShardKey(userId));
    }

    public void create(F persistable)
    {
        /*if (persistable instanceof ShardedEntity ) {
        	// Will remove this "if" after all sharded entry is implemented
        	add(persistable, new ConnectOpt());
        } else {*/
        // FIXME: Seriously. fixme.
        // TODO: Seriously. fixme.
        add(persistable, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
        //}
    }

    public void create(F persistable, ConnectOpt opt)
    {
        /*if (persistable instanceof ShardedEntity ) {
        	// Will remove this "if" after all sharded entry is implemented
        	add(persistable, new ConnectOpt());
        } else {*/
        // FIXME: Seriously. fixme.
        // TODO: Seriously. fixme.
        opt.setShardKey(((ShardedEntity) persistable).getShardKey());
        add(persistable, opt);
        //}
    }

    public void add(F persistable)
    {
        /*if (persistable instanceof ShardedEntity ) {
        	// Will remove this "if" after all sharded entry is implemented
        	add(persistable, new ConnectOpt());
        } else {*/
        // FIXME: Seriously. fixme.
        // TODO: Seriously. fixme.
        add(persistable, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
        //}
    }

    public void add(F persistable, ConnectOpt opt)
    {
        /*if (persistable instanceof ShardedEntity ) {
        	// Will remove this "if" after all sharded entry is implemented

        	Logger.debug("Sharded Entry worked");*/
        opt.setShardKey(((ShardedEntity) persistable).getShardKey());
        //}
        super.add(persistable, opt);
    }

    public F addOrUpdate(F persistable)
    {
        /*if (persistable instanceof ShardedEntity ) {
        	// Will remove this "if" after all sharded entry is implemented
        	return addOrUpdate(persistable, new ConnectOpt());
        } else {*/
        // FIXME: Seriously. fixme.
        // TODO: Seriously. fixme.
        return addOrUpdate(persistable, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
        //}
    }

    public F addOrUpdate(F persistable, ConnectOpt opt)
    {
        /*if (persistable instanceof ShardedEntity ) {
        	// Will remove this "if" after all sharded entry is implemented

        	Logger.debug("Sharded Entry worked");*/
        opt.setShardKey(((ShardedEntity) persistable).getShardKey());
        //}
        return super.addOrUpdate(persistable, opt);
    }

    public void delete(F persistable)
    {
        /*if (persistable instanceof ShardedEntity ) {
        	// Will remove this "if" after all sharded entry is implemented
        	delete(persistable, new ConnectOpt());
        } else {*/
        // FIXME: Seriously. fixme.
        // TODO: Seriously. fixme.
        delete(persistable, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
        //}
    }

    public void delete(F persistable, ConnectOpt opt)
    {
        /*if (persistable instanceof ShardedEntity ) {
        	// Will remove this "if" after all sharded entry is implemented

        	Logger.debug("Sharded Entry worked");*/
        opt.setShardKey(((ShardedEntity) persistable).getShardKey());
        //}
        super.delete(persistable, opt);
    }

    public F update(F persistable)
    {
        /*	if (persistable instanceof ShardedEntity ) {
        		// Will remove this "if" after all sharded entry is implemented
        		return update(persistable, new ConnectOpt());
        	} else {*/
        // FIXME: Seriously. fixme.
        // TODO: Seriously. fixme.
        return update(persistable, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
        //}
    }

    public F update(F persistable, ConnectOpt opt)
    {
        /*		if (persistable instanceof ShardedEntity ) {
        			// Will remove this "if" after all sharded entry is implemented

        			Logger.debug("Sharded Entry worked");*/
        opt.setShardKey(((ShardedEntity) persistable).getShardKey());
        //}
        return super.update(persistable, opt);
    }

    public Query createNativeQuery(String query) throws PersistenceException
    {
        return createNativeQuery(query, new ConnectOpt().setMode(DB_MODE.READ).setShardId(ShardManager.getShardForCurrentThread()));
    }

    public Query createNamedQuery(String query) throws PersistenceException
    {
        return createNamedQuery(query, new ConnectOpt().setMode(DB_MODE.READ).setShardId(ShardManager.getShardForCurrentThread()));
    }

    @Deprecated
    public F findById(Serializable id)
    {
        return findById(id, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
    }

    @Deprecated
    public List<F> findByCriteria(String where)
    {
        return findByCriteria(where, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
    }

    @Deprecated
    public F uniqueResult(String where)
    {
        return uniqueResult(where, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
    }

    @Deprecated
    public List<F> findAll(ConnectOpt opt)
    {
        return findAll(new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
    }

    @Deprecated
    public List<F> maxResults(String criteria, int start, int max)
    {
        return maxResults(criteria, start, max, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
    }

    @Deprecated
    public long count(String criteria)
    {
        return count(criteria, new ConnectOpt().setShardId(ShardManager.getShardForCurrentThread()));
    }

    /**
     * For user-dependent queries
     *
     * @param userId
     * @param where
     * @return
     */
    public F findById(int userId, Serializable id)
    {
        return findById(id, new ConnectOpt().setShardKey(userId));
    }

    /**
     * For user-dependent queries
     *
     * @param userId
     * @param where
     * @return
     */
    public List<F> findByCriteria(int userId, String where)
    {
        return super.findByCriteria(where, new ConnectOpt().setShardKey(userId));
    }

    @Override
    protected EntityManager getSession(ConnectOpt opt)
    {
        EntityManager em = null;
        long shardId = opt.getShardId();
        String shardName = Constants.DB_SHARD + shardId;
        if (opt.getMode() == DB_MODE.WRITE)
        {
            em = DBConnectManager.writeEm(shardName);
        }
        else if (opt.getMode() == DB_MODE.READ)
        {
            em = DBConnectManager.readEm(shardName);
        }
        else
        {
            em = DBConnectManager.em(shardName);
        }
        return em;
    }

}
