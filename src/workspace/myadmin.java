package workspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zxybazh on 5/15/15.
 */
public class myadmin {
    public static Boolean Ques(Statement stmt, int cid) {
        String sql = "select admin from customer where cid = " + Integer.toString(cid) + " and admin = true;";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Query administrator authority error");
            if (bookshop.debug) e.printStackTrace();
            return null;
        }
        try {
            return rs.next();
        } catch (SQLException e) {
            bookshop.alert("Query administrator authority error");
            if (bookshop.debug) e.printStackTrace();
            return null;
        }
    }
}
