package workspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zxybazh on 5/16/15.
 */
public class myfeedback {
    public static Boolean valid(Statement stmt, int fid) {
        String sql = "select * from feedback where fid = "+ Integer.toString(fid) + ";";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Validate feedback id error");
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
    public static Integer username_bid_fid(Statement stmt, String cname, int bid) {
        String sql = "select fid from feedback, customer where customer.cid = feedback.cid and " +
                "login_name = \'"+ bookshop.polish(cname) + "\' and bid = ";
        sql+= Integer.toString(bid) + ";";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Validate username and bid error");
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

