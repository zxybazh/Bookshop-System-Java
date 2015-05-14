package workspace;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * Created by zxybazh on 5/14/15.
 */
public class mylogin {
    public static int log_in(Statement stmt, String cname, String password) throws Exception {
        password = mymd5.getMD5(password);
        String query;
		ResultSet rs;

		query="select cid from customer where login_name = \'" + cname + "\' and password = \'" + password + "\';";
		try{
			rs = stmt.executeQuery(query);

			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
        } catch(Exception e) {
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
    }
}
