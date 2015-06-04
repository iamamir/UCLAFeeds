package controllers.common;

import java.util.HashMap;
import java.util.Map;

import models.dto.Request;
import models.dto.Response;

import org.codehaus.jackson.node.ObjectNode;

import play.Play;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import constant.Constants;
import core.logic.security.Authenticator;

public class AuthenticatedAction extends Action<Authenticated> {
	
	@Override
	public Result call(final Context ctx) throws Throwable {
		if (Constants.useAuthentication == false || !Play.isProd()) {
			return delegate.call(ctx);
		}
		Map<String, String[]> qs = ctx.request().queryString();
		Map<String, String> uniqueQs = removeDuplicates(qs);
		if (uniqueQs != null) {
			String requestHash = uniqueQs.get(Request.Params.HASH.getValue());
			String generatedHash = Authenticator.generateHash(uniqueQs);
			if (requestHash != null && generatedHash != null && requestHash.equals(generatedHash)) {
				return delegate.call(ctx);
			}
		}
		ObjectNode response = buildErrorResponse();
		return ok(response);
	}
	
	private Map<String, String> removeDuplicates(Map<String, String[]> requestMap) {
		if (requestMap != null) {
			Map<String, String> result = new HashMap<String, String>();
			for (String key : requestMap.keySet()) {
				String[] values = requestMap.get(key);
				String value = values[values.length - 1];	// pick the latest value
				result.put(key, value);
			}
			return result;
		}
		else {
			return null;
		}
	}
	
	private ObjectNode buildErrorResponse() {
		ObjectNode json = Json.newObject();
		json.put(Response.Params.STATUS_CODE.getValue(), Response.Status.UNAUTHORIZED_ACCESS.getErrorCode());
		json.put(Response.Params.ERROR_MSG.getValue(), Response.Status.UNAUTHORIZED_ACCESS.getErrorMsg());
		return json;
	}

}
