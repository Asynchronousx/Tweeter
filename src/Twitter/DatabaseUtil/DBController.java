package Twitter.DatabaseUtil;

import Twitter.Util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

//A database controller that perform search and update queries.
//Every class that want to perform an action on a database, should use this class interface.
public class DBController {

    @Attributes
    //Statement to query and result set returned from it.
    private static Statement statement;
    private static ResultSet resultSet;

    @Methods
    //Search query perform a search into the database. It returns the result set of that query.
    public static ResultSet searchQuery(Connection conn, String query) {

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

        return resultSet;

    }

    //Update query perform an update query (INSERT/DELETE) into the database. It returns the operation outcome.
    public static Boolean updateQuery(Connection conn, String query) {
        try {
            statement = conn.createStatement();
            statement.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

        return true;

    }

}
