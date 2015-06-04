
package controllers;


import models.dto.Response;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


public class BaseController extends Controller
{

    public static Result index(String name)
    {
        return sendErrorResponse(Response.Status.INVALID_SERVICE_REQUESTED);
    }

    public static Result sendErrorResponse(Response.Status status)
    {
        ObjectNode result = Json.newObject();
        result.put(Response.Params.STATUS_CODE.getValue(), status.getErrorCode());
        result.put(Response.Params.ERROR_MSG.getValue(), status.getErrorMsg());
        return ok(result.toString());
    }

    public static Result sendErrorResponse(Response.Status status, String value)
    {
        ObjectNode result = Json.newObject();
        result.put(Response.Params.STATUS_CODE.getValue(), status.getErrorCode());
        result.put(Response.Params.ERROR_MSG.getValue(), status.getErrorMsg() + value);
        return ok(result.toString());
    }

    public static Result sendResponse(Response.Status status, String value)
    {
        ObjectNode result = Json.newObject();
        result.put(Response.Params.STATUS_CODE.getValue(), status.getErrorCode());
        result.put(Response.Params.ERROR_MSG.getValue(), status.getErrorMsg());
        result.put("quotationId", value);
        return ok(result.toString());
    }
}
