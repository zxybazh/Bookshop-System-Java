package workspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zxybazh on 5/14/15.
 */
public class mylogin {
    public static int log_in(Statement stmt, String cname, String password) throws Exception {
        password = mymd5.getMD5(password);
        String query;
		ResultSet rs;

		query="select cid from customer where login_name = \'" + bookshop.polish(cname) + "\' and password = \'" + password + "\';";
		try{
			rs = stmt.executeQuery(query);

			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
        } catch(Exception e) {
            bookshop.alert("login process error");
            if (bookshop.debug) System.err.println("Unable to execute query:"+query+"\n");
			if (bookshop.debug) System.err.println(e.getMessage());
			throw(e);
		}
    }
    public static Boolean check(Statement stmt, int cid, String password) {
        String sql = "select * from customer where cid = "+Integer.toString(cid)+" and password = \'"
                +bookshop.polish(password)+"\';";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            bookshop.alert("check password error");
            if (bookshop.debug) System.err.println(e.getMessage());
            return null;
        }
        try {
            return rs.next();
        } catch (SQLException e) {
            bookshop.alert("check password sql error");
            if (bookshop.debug) System.err.println(e.getMessage());
            return null;
        }
    }
}
