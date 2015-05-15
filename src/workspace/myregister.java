package workspace;

import java.sql.SQLException;
import java.sql.Statement;

public class myregister {
    public static void register(Statement stmt, String cname, String password,
                                String full_name, String addr, long phone) throws SQLException {
        String sql = "Insert into customer values(null, \'" + bookshop.polish(cname) + "\', \'"+ bookshop.polish(full_name);
        sql += "\', \'" + mymd5.getMD5(password) + "\', \'" + bookshop.polish(addr);
        sql += "\', " + String.valueOf(phone) + ", False);";
        //System.out.println(sql);
        try {
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println(">_< Register failed at SQL process, detailed information is below.");
            if (bookshop.debug) e.printStackTrace();
        }
    }
}
