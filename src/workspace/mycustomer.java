package workspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zxybazh on 5/16/15.
 */
public class mycustomer {
    public static Boolean valid(Statement stmt, int cid) {
        String sql = "select * from customer where cid = "+ Integer.toString(cid) + ";";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Validate customer id error");
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
    public static Integer username_cid(Statement stmt, String cname) {
        String sql = "select cid from customer where login_name = \'"+ bookshop.polish(cname) + "\';";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Validate user name error");
            if (bookshop.debug) System.err.println(e.getMessage());
            return null;
        }
        try {
            if (rs.next()) return rs.getInt(1);
            else return -1;
        } catch (SQLException e) {
            bookshop.alert("rs.next() error at sql");
            if (bookshop.debug) e.printStackTrace();
            return null;
        }
    }
}

