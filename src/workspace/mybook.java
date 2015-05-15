package workspace;

import java.security.PublicKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zxybazh on 5/16/15.
 */
public class mybook {
    public static Boolean valid(Statement stmt, int bid) {
        String sql = "select * from book where bid = "+ Integer.toString(bid) + ";";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Validate book id error");
            if (bookshop.debug) System.err.println(e.getMessage());
            return null;
        }
        try {
            return rs.next();
        } catch (SQLException e) {
            bookshop.alert("rs.next() error at sql");
            if (bookshop.debug) e.printStackTrace();
            return null;
        }
    }
}
