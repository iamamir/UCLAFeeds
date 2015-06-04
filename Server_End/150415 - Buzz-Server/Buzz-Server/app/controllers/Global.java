
package controllers;


import play.GlobalSettings;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;


public class Global extends GlobalSettings
{

    // For CORS
    private class ActionWrapper extends Action.Simple
    {
        public ActionWrapper(Action<?> action)
        {
            this.delegate = action;
        }

        @Override
        public Result call(Http.Context ctx) throws java.lang.Throwable
        {
            Result result = this.delegate.call(ctx);
            Http.Response response = ctx.response();
            response.setHeader("Access-Control-Allow-Origin", "*");
            return result;
        }
    }

    @Override
    public Action<?> onRequest(Http.Request request, java.lang.reflect.Method actionMethod)
    {
        return new ActionWrapper(super.onRequest(request, actionMethod));
    }

}
