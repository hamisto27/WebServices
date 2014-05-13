package annotations;

import play.mvc.Controller;



public class BaseController extends Controller {

    protected static <T> T bodyRequest(Class<T> t) {
        return t.cast(ctx().args.get("ObjectRequest"));
    }
    
    
}