
package constant;


public final class Constants
{

    public static String salt = null;

    public static boolean useAuthentication = true;

    public static enum DB_MODE
    {
        AUTO,
        WRITE,
        READ,
    }

    public static final String DB_MASTER = "openfire";
    public static final String DB_SHARD = "shard";
}
