package ua.training.top;

public class Profiles {
    public static final String
            POSTGRES_DB = "postgres",
            H2_DB = "h2db",
            HSQL_DB = "hsqldb",
            HEROKU = "heroku";

    //  Get DB profile depending of DB driver in classpath
    public static String getActiveDbProfile() {
        if (isClassExists("org.postgresql.Driver")) {
            return POSTGRES_DB;
        } else if (isClassExists("org.hsqldb.jdbcDriver")) {
            return HSQL_DB;
        } else if (isClassExists("org.h2.Driver")) {
            return H2_DB;
        } else {
            throw new IllegalStateException("Could not find DB driver");
        }
    }

    private static boolean isClassExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
