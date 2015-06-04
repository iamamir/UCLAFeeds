package controllers.common;

import java.util.Map;

import models.dto.Request;
import models.dto.Response;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import controllers.BaseController;
import dao.shard.ShardManager;
import dao.shard.ShardManager.AccessMode;

public class ShardAwareAction extends Action<ShardAware> {

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
		Map<String, String[]> qs = ctx.request().queryString();
		String[] values = qs.get(Request.Params.USER_ID.getValue());
		long userId = extractUserId(values);
		if (userId > 0) {
			long shardId = ShardManager.getShardIdByUserId(userId);
			if (shardId > 0) {
				return ShardManager.withTransaction(
						shardId,
						AccessMode.buildMode(configuration.readOnly()),
					new play.libs.F.Function0<Result>() {
						public Result apply() throws Throwable {
							return delegate.call(ctx);
						}
					}
				);
			}
		}
		return BaseController.sendErrorResponse(Response.Status.USER_NOT_FOUND);
	}

}
