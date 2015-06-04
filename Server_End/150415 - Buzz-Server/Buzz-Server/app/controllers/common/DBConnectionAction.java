package controllers.common;

import java.util.Map;

import models.dto.Request;
import models.dto.Response;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import controllers.BaseController;
import core.db.DBConnectManager;
import dao.shard.ShardManager;

/**
 * Wraps an action in am JPA transaction.
 */
public class DBConnectionAction extends Action<DBConnection> {
	
	private long extractUserId(String[] values) {
		if (values != null && values.length > 0) {
			String strUserId = values[values.length - 1];
			if (strUserId != null && strUserId.length() > 0) {
				return Long.parseLong(strUserId);
			}
			else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}
	
	@Override
    public Result call(final Context ctx) throws Throwable {
		Logger.debug("DBConnectionAction Started");
		boolean writeMode = configuration.write();
    	
		// Transaction should start here...
		try {
			DBConnectManager.setup(writeMode);
			
			Map<String, String[]> qs = ctx.request().queryString();
			String[] values = qs.get(Request.Params.USER_ID.getValue());
			long userId = extractUserId(values);
			if (userId > 0) {			
				long shardId = ShardManager.getShardIdByUserId(userId);
				if (shardId > 0) {
					ShardManager.setShardInfo(shardId);
				}
				else {
					return BaseController.sendErrorResponse(Response.Status.USER_NOT_FOUND);
				}
			}
			
			Result result = delegate.call(ctx);
			
			DBConnectManager.commit();
			
			Logger.debug("DBConnectionAction End");
			return result;
		} catch(Throwable t) {
			Logger.debug("Catch: " + t.getMessage(), t);
			DBConnectManager.rollback();
			throw t;
		} finally {
			DBConnectManager.close();
		}
    }
    
}