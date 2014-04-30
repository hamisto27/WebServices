package annotations;

import play.*;
import play.mvc.*;


public class BaseController extends Controller {

    protected static <T> T bodyRequest(Class<T> t) {
        return t.cast(ctx().args.get("ObjectRequest"));
    }
    
    
}