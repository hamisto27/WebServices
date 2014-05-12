package controllers;

import play.libs.F.*;
import play.mvc.*;
import play.mvc.Http.*;

import play.libs.Scala;


public class HttpsAction extends play.mvc.Action.Simple {

    private static final String SSL_HEADER = "x-forwarded-proto";
    private static final play.api.Configuration conf = null;
    private static String httpsPort;

    @Override
    public Promise<SimpleResult> call(Context ctx) throws Throwable {

        Promise<SimpleResult> ret = null;

        //redirect if it's not secure
        if (!isHttpsRequest(ctx.request())) {
            String url = redirectHostHttps(ctx) + ctx.request().uri();
            ret = Promise.pure(redirect(url));
        }
        else {
            // Let request proceed.
            ret = delegate.call(ctx);
        }

        return ret;
    }

    public static String redirectHostHttps( Context ctx) {

        String[] pieces = ctx.request().host().split(":");
        String ret = "https://" + pieces[0];

        // In Dev mode we want to append the port.  
        // In Prod mode, no need to append the port as we use the standard https port, 443.
        if( isDev() ) {
            ret += ":" + getHttpsPort();
        }

        return ret;
    }

    public static boolean isHttpsRequest( Request request ) {
        return (request.getHeader(SSL_HEADER) != null
            && request.getHeader(SSL_HEADER).contains("https")) 
            || isOverHttpsPort(request.host());  
    }

    public static boolean isOverHttpsPort( String host ) {
        boolean ret = false;
        String[] hostParts = host.split(":");

        if( hostParts.length > 1 ) {
            ret = hostParts[1].equalsIgnoreCase(getHttpsPort());
        }

        return ret;
    }

    private synchronized static String getHttpsPort() {
        if( httpsPort == null ) 
            httpsPort = "9443";
        return httpsPort;  
    }

    /**
     * Returns `true` if the current application is `DEV` mode.
     */
    private static boolean isDev() {
        return play.api.Play.isDev(play.api.Play.current());
    }

    /**
     * Retrieves a configuration value as a <code>String</code>.
     *
     * @param key configuration key (relative to configuration root key)
     * @return a configuration value or <code>null</code>
     */
    @SuppressWarnings("unused")
	private static String getString(String key) {
        return Scala.orNull(conf.getString(key, scala.Option.<scala.collection.immutable.Set<java.lang.String>>empty()));
    }
}