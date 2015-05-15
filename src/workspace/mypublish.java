package workspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class mypublish {
    public static int get_pid(Statement stmt, String publisher) {
        String sql = "select * from publisher where pname = \'" + bookshop.polish(publisher) + "\';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Find the specific publisher error");
            if (bookshop.debug) System.err.println(e.getMessage());
            return -1;
        }
        try {
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            bookshop.alert("rs.next() error at publish");
            if (bookshop.debug) e.printStackTrace();
            return -1;
        }
        sql = "insert into publisher (pname)values(\'" + bookshop.polish(publisher) +"\');";
        try {
            stmt.execute(sql);
        } catch (Exception e) {
            bookshop.alert("insert publisher infomation error");
            if (bookshop.debug) e.printStackTrace();
            return -1;
        }

        sql = "select * from publisher where pname = \'" + bookshop.polish(publisher) + "\';";
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Find the specific publisher error");
            if (bookshop.debug) System.err.println(e.getMessage());
            return -1;
        }
        try {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            bookshop.alert("rs.next() error at publish");
            if (bookshop.debug) e.printStackTrace();
            return -1;
        }
    }
    public static boolean publish (Statement stmt, int pid, int bid, int year) {
        String sql = "Insert into publish values(" + Integer.toString(pid) + ", " + Integer.toString(bid) + ", "
                + Integer.toString(year) + ");";
        try {
            stmt.execute(sql);
        } catch (Exception e) {
            bookshop.alert("insert publish infomation error");
            if (bookshop.debug) e.printStackTrace();
            return false;
        }
        return true;
    }
}