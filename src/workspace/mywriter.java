package workspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class mywriter {
    public static Boolean valid(Statement stmt, int aid) {
        String sql = "select * from author where aid = "+ Integer.toString(aid) + ";";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Validate author id error");
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
    public static Integer aname_aid(Statement stmt, String author) {
        String sql = "select * from author where aname = \'" + bookshop.polish(author) + "\';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Find the specific author error");
            if (bookshop.debug) System.err.println(e.getMessage());
            return null;
        }
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            bookshop.alert("rs.next() error at write");
            if (bookshop.debug) e.printStackTrace();
            return null;
        }
        return -1;
    }
    public static int fetch_aid(Statement stmt, String author) {
        String sql = "select * from author where aname = \'" + bookshop.polish(author) + "\';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Find the specific author error");
            if (bookshop.debug) System.err.println(e.getMessage());
            return -1;
        }
        try {
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            bookshop.alert("rs.next() error at write");
            if (bookshop.debug) e.printStackTrace();
            return -1;
        }
        sql = "insert into author (aname)values(\'" + bookshop.polish(author) +"\');";
        try {
            stmt.execute(sql);
        } catch (Exception e) {
            bookshop.alert("insert author infomation error");
            if (bookshop.debug) e.printStackTrace();
            return -1;
        }

        sql = "select * from author where aname = \'" + bookshop.polish(author) + "\';";
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("Find the specific author error");
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
    public static boolean iwrite (Statement stmt, int aid, int bid) {
        String sql = "Insert into iwrite values(" + Integer.toString(aid) + ", " + Integer.toString(bid) + ");";
        try {
            stmt.execute(sql);
        } catch (Exception e) {
            bookshop.alert("insert iwrite infomation error");
            if (bookshop.debug) e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean coauthor(Statement stmt, int bid, int aid1, int aid2) {
        if (aid1 > aid2) {
            int tmp = aid1;
            aid1 = aid2;
            aid2 = tmp;
        }
        String sql = "Insert into coauthor values(" + Integer.toString(bid) + ", " + Integer.toString(aid1) + ", ";
        sql += Integer.toString(aid2) + ");";
        //if (bookshop.debug) bookshop.print(sql);
        try {
            stmt.execute(sql);
        } catch (Exception e) {
            bookshop.alert("insert coauthor information error");
            if (bookshop.debug) e.printStackTrace();
            return false;
        }
        return true;
    }
}

