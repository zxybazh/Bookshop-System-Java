package workspace;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.CORBA.INTERNAL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;

public class bookshop {
    final public static boolean debug = true;
    // TODO AT LAST change the debug state
    private static boolean logged_in = false;
    private static boolean leave = false;
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static String cname = "";
    private static int cid = 0;
    private static myconnector con = null;

    private static void Hello_world() {
        myfun.print_title();
        myfun.print_face();
    }
    private static void display_unlogged_in() {
        System.out.println("\tCurrently you are logged out.");
        System.out.println("\tWhat would you like to do next?");
        System.out.println(" 1. Log in as a registered customer");
        System.out.println(" 2. Register a new account");
        System.out.println(" 3. Exit peacefully without hesitation");
    }

    private static void display_admin() {
        System.out.println("\tCurrently you are logged in as administrator "+cname+".");
        System.out.println("\tWhat would you like to do next?");
        System.out.println(" 1. Log in as a customer");
        System.out.println(" 2. Log out");
        System.out.println(" 3. Change user\'s authority");
        System.out.println(" 4. Add new book");
        System.out.println(" 5. Add some copies to some book");
        System.out.println(" 6. Show statistics of this semester");
        System.out.println(" 7. Give user awards");
        System.out.println(" 8. Exit peacefully without hesitation");

    }

    private static void display_customer() {
        System.out.println("\tCurrently you are logged in as customer "+cname+".");
        System.out.println("\tWhat would you like to do next?");
        System.out.println(" 1. Log in as an administrator");
        System.out.println(" 2. Log out");
        System.out.println(" 3. Change my profile");
        System.out.println(" 4. Order some books");
        System.out.println(" 5. Feedback my books");
        System.out.println(" 6. Rate feedback");
        System.out.println(" 7. Book browse");
        System.out.println(" 8. Trust evaluation");
        System.out.println(" 9. Show feedback");
        System.out.println("10. Buying suggestion");
        System.out.println("11. Two degrees of separation");
        System.out.println("12. Exit peacefully without hesitation");
    }

    private static void print_split() {
        System.out.println();
        System.out.println("**********Wexley's Book Shop**********");
        System.out.println("**************************************");
        System.out.println();
    }

    private static boolean choose(String a, char c1, char c2) {
        String tmp = null;
        do {
            System.out.println(a + " [" + c1 + '/' + c2 + ']');
            try {
                tmp = in.readLine();
            } catch (Exception e) {
                System.out.println(">_< Parse input error!!!");
                if (debug) System.err.println(e.getMessage());
                return false;
            }
        } while (tmp.equals("") || (tmp.charAt(0) != c1 && tmp.charAt(0) != c2));
        return tmp.charAt(0) == c2;
    }
    public static void alert(String s) {
        System.out.println(">_< " + s + "!!!");
    }
    public static String polish(String a) {
        String b = "";
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == '\'') {
                b += '\\';
            }
            b += a.charAt(i);
        }
        return b;
    }
    /*
    private static void runsql(String sql) {
        try {
            con.stmt.execut(sql);
        } catch (Exception e) {
            System.out.println(">_< Error while excecuting "+sql);
            if (debug) e.printStackTrace();
        }
    }

    private static ResultSet querysql(String sql) {
        try {
            con.stmt.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(">_< Error while querying "+sql);
            if (debug) e.printStackTrace();
        }
    }
    */
    public static void main(String[] args) {

        if (!debug) Hello_world();
        try {
            con = new myconnector();

            System.out.println("Connection established XD!!");
            System.out.println("0v0 Welcome to Wexley's Bookshop!!!");
            print_split();

            ResultSet rs = null;
            boolean flag, flag1, flag2;

            while (!leave) {
                while (!logged_in) {
                    display_unlogged_in();
                    String choice = "";
                    int c = 0;
                    while ((choice = in.readLine()) == null || choice.length() == 0) ;
                    try {
                        c = Integer.parseInt(choice);
                    } catch (Exception e) {
                        continue;
                    }
                    if (c > 3 || c < 1) continue;
                    if (c == 1) {
                        System.out.println("Please Input your username:");
                        cname = in.readLine();
                        System.out.println("Please Input your password:");
                        String password = in.readLine();
                        try {
                            cid = mylogin.log_in(con.stmt, cname, password);
                        } catch (Exception e) {
                            System.out.println(">_< Log in process is broken, detailed information is below:");
                            if (debug) e.printStackTrace();
                            continue;
                        }
                        if (cid > 0) {
                            System.out.print("Log in succeed !! Now you are logged_in as " + cname);
                            System.out.println(", and your customer id is " + cid);
                            logged_in = true;
                            break;
                        } else {
                            System.out.println(">_< Log in failed, wrong username or password, please try again!");
                            continue;
                        }
                    } else if (c == 2) {
                        System.out.println("Please Input your username(3~30 characters)");
                        flag = false;
                        flag1 = false;
                        flag2 = false;
                        do {
                            if (flag) System.out.println(">_< Sorry, you username has been picked !!");
                            if (flag1) System.out.println(">_< Sorry, your username is tooooo short !!");
                            if (flag2) System.out.println(">_< Sorry, your username is tooooo long !!");
                            flag = flag1 = flag2 = false;

                            cname = in.readLine();
                            if (cname.length() < 3) {
                                flag1 = true;
                                continue;
                            }
                            if (cname.length() > 30) {
                                flag2 = true;
                                continue;
                            }
                            try {
                                rs = con.stmt.executeQuery("select * from customer where login_name = \'"
                                        + polish(cname) + "\';");
                            } catch (Exception e) {
                                System.out.println(">_< Query username duplicate error at sql");
                                if (debug) e.printStackTrace();
                                break;
                            }
                            if (rs.next()) flag = true;
                        } while (flag1 || flag2 || flag);

                        String password, tmp, full_name, addr;
                        long phone = 0;

                        flag = false;
                        flag1 = false;
                        flag2 = false;
                        System.out.println("Please Input your password(3~30 characters)");
                        do {
                            if (flag) System.out.println(">_< Different password input, please try again.");
                            flag = true;
                            do {
                                if (flag1) System.out.println("Password tooooooo weak!!! Please choose another!!");
                                if (flag2) System.out.println("Password tooooooo long!!! Please choose another!!");
                                password = in.readLine();
                                flag1 = password.length() < 3;
                                flag2 = password.length() > 30;
                            } while (flag1 || flag2);
                            System.out.println("Please Input your password again to confirm");
                            tmp = in.readLine();
                        } while (!tmp.equals(password));


                        do {
                            System.out.println("Please Input your full name(can be blank, 3-30 characters)");
                            full_name = in.readLine();
                            if (full_name.equals("")) break;
                        } while (full_name.length() < 3 || full_name.length() > 30);

                        do {
                            System.out.println("Please Input your address(can be blank)");
                            addr = in.readLine();
                            if (addr.equals("")) break;
                        } while (addr.length() < 3 || addr.length() > 30);

                        do {
                            System.out.println("Please Input your phone number(all numbers, 11 bits)");
                            tmp = in.readLine();
                            flag = false;
                            phone = 0;
                            for (int i = 0; i < tmp.length(); i++) {
                                if (tmp.charAt(i) < '0' || tmp.charAt(i) > '9') {
                                    flag = true;
                                    break;
                                }
                                phone = phone * 10l + (long) (tmp.charAt(i) - '0');
                            }
                        } while (tmp.length() != 11 || flag);

                        try {
                            myregister.register(con.stmt, cname, password, full_name, addr, phone);
                        } catch (Exception e) {
                            System.out.println(">_< Register failed, check the message for more");
                            e.printStackTrace();
                            continue;
                        }
                        System.out.println("0w0 Registered Successfully !! Please Log in now :)");
                        continue;
                    } else {
                        leave = true;
                        break;
                    }
                }
                if (!leave) {
                    print_split();
                    System.out.println("Now loading Book Management System...");
                    boolean admin = false;
                    String sql = "select admin from customer where cid = " + Integer.toString(cid) + " and admin = True;";
                    flag = false;
                    try {
                        rs = con.stmt.executeQuery(sql);
                    } catch (Exception e) {
                        System.out.println(">_< Query administration error at sql!");
                        if (debug) e.printStackTrace();
                        flag = true;
                    }
                    if (!flag && rs.next()) {
                        String tmp;
                        admin = !choose("Log in as administrator?", 'y', 'n');
                    } else admin = false;

                    while (!leave && admin) {
                        display_admin();
                        String choice = "";
                        int c = 0;
                        while ((choice = in.readLine()) == null || choice.length() == 0);
                        try {
                            c = Integer.parseInt(choice);
                        } catch (Exception e) {
                            continue;
                        }
                        if (c > 8 || c < 1) continue;
                        if (c == 1) {
                            admin = false; break;
                        } else if (c == 2) {
                            logged_in = admin = false; break;
                        } else if (c == 3) {
                            flag2 = choose("Input the one's customer id or user name ?", 'c', 'u');

                            int k = 0; flag = false;

                            if (!flag2) {
                                do {
                                    System.out.println("Please input the one's customer id");
                                    choice = in.readLine();
                                    try {
                                        k = Integer.parseInt(choice);
                                    } catch (Exception e) {
                                        System.out.println("Parse cid to integer error");
                                        if (debug) e.printStackTrace();
                                        continue;
                                    }
                                    try {
                                        rs = con.stmt.executeQuery("select * from customer where cid = "
                                                + Integer.toString(k)+";");
                                    } catch (Exception e) {
                                        System.out.println(">_< Find the user with cid error");
                                        if (debug) e.printStackTrace();
                                        continue;
                                    }
                                    if (rs.next()) break;
                                    do {
                                        System.out.println("NO such user, continue?[y/n]");
                                        choice = in.readLine();
                                    } while (choice.equals("") || (choice.charAt(0) != 'y' && choice.charAt(0) != 'n'));
                                    if (choice.charAt(0) == 'y') continue; else {
                                        flag = true; break;
                                    }
                                } while (true);
                            } else {
                                do {
                                    System.out.println("Please input the one's user name");
                                    choice = in.readLine();
                                    try {
                                        rs = con.stmt.executeQuery(
                                                "select cid from customer where login_name = \'" +polish(choice)+"\';");
                                    } catch (Exception e) {
                                        System.out.println(">_< Find the user with user name error");
                                        if (debug) e.printStackTrace();
                                        continue;
                                    }
                                    if (rs.next()) {
                                        k = rs.getInt(1);
                                        break;
                                    } else {
                                        do {
                                            System.out.println("NO such user, continue?[y/n]");
                                            choice = in.readLine();
                                        } while (choice.equals("") || (choice.charAt(0) != 'y' && choice.charAt(0) != 'n'));
                                        if (choice.charAt(0) == 'y') continue; else {
                                            flag = true; break;
                                        }
                                    }
                                } while (true);
                            }
                            if (flag) continue;
                            if (k == cid) {
                                System.out.println("You are not allowed to change your own authority.");
                                continue;
                            }

                            flag = choose("\tYou can change in two ways\n 1. Make the user customer\n 2. Make the user administrator", '1', '2');

                            sql = "update customer set admin =";
                            if (flag) sql += "true";
                                else sql += "false";
                            sql += " where cid = "+Integer.toString(k)+";";

                            try {
                                //System.out.println(sql);
                                con.stmt.execute(sql);
                            } catch (Exception e) {
                                System.out.println(">_< change authority error");
                                if (debug) e.printStackTrace();
                                continue;
                            }
                            System.out.println("0w0 Changed authority successfully!!!");
                        } else if (c == 4) {
                            String isbn = null; flag1 = false;
                            do {
                                if (flag1) System.out.println("Please input the 10 bit isbn in the correct way:");
                                    else System.out.println("Please input the 10 bit isbn for the new book:");
                                choice = in.readLine();
                                flag = false; flag1 = true;
                                if (choice.length() != 10) {
                                    flag = true; continue;
                                }
                                choice = choice.toUpperCase();
                                for (int i = 0; i < choice.length(); i++) {
                                    if ((choice.charAt(i) < '0' || choice.charAt(i) > '9') && choice.charAt(i) != 'X') {
                                        flag = true; break;
                                    }
                                }
                            } while (flag);

                            isbn = choice;
                            sql = "select * from book where isbn = "+choice+";";
                            try {
                                rs = con.stmt.executeQuery(sql);
                            } catch (Exception e) {
                                System.out.println("Find duplicate book error!");
                                if (debug) e.printStackTrace();
                                continue;
                            }

                            if (rs.next()) {
                                System.out.println(">_< Duplicate Book Exists !!! Please add the copy number!!!");
                                continue;
                            }

                            Boolean format = null;
                            do {
                                System.out.println("Please input the cover format of the book[soft/hard/unknown]");
                                choice = in.readLine();
                                if (choice.equals("soft")) {
                                    format = true; break;
                                } else if (choice.equals("hard")) {
                                    format = false; break;
                                } else if (choice.equals("unknown")) {
                                    format = null; break;
                                }
                            } while (true);

                            String w1, w2, w3;
                            do {
                                System.out.println("Please input the key words of the book in less than 100 characters");
                                w1 = in.readLine();
                            } while (w1.length() > 100);

                            do {
                                System.out.println("Please input the title words of the book in less than 100 characters");
                                w2 = in.readLine();
                            } while (w2.length() > 100);

                            do {
                                System.out.println("Please input the subjects of the book in less than 100 characters");
                                w3 = in.readLine();
                            } while (w3.length() > 100);

                            double price = 0;

                            flag = false;
                            do {
                                System.out.println("Please input the price per book:");
                                choice = in.readLine();
                                try {
                                    price = Double.parseDouble(choice);
                                } catch (Exception e) {
                                    System.out.println(">_< Parse book price error!!!");
                                    if (debug) System.err.println(e.getMessage());
                                    flag = true; break;
                                }
                            } while (price <= 0);
                            if (flag) continue;

                            int num = 0; flag = false;
                            do {
                                System.out.println("Pleas input the number of copies of the book:");
                                choice = in.readLine();
                                try {
                                    num = Integer.parseInt(choice);
                                } catch (Exception e) {
                                    System.out.println(">_< Parce book number error!!!");
                                    if (debug) System.err.println(e.getMessage());
                                    flag = true;
                                    break;
                                }
                            } while (num < 0);
                            if (flag) continue;

                            sql = "insert into book values(null, "+isbn+", ";
                            if (format != null) sql += format.toString(); else sql += "null";
                            sql += ", \'" + polish(w1) + "\', \'" + polish(w2) + "\', \'" + polish(w3);
                            sql += "\', " + Double.toString(price);
                            sql += ", " + Integer.toString(num) + ");";
                            //System.out.println(sql);

                            try {
                                con.stmt.execute(sql);
                            } catch (Exception e) {
                                System.out.println(">_< Add book information to SQL error!!");
                                if (debug) e.printStackTrace();
                                continue;
                            }
                            System.out.println("Add book information successfully !!!");
                            try {
                                rs = con.stmt.executeQuery("select bid from book where isbn=\'" + isbn + "\';");
                            } catch (Exception e) {
                                alert("Find book id error");
                                if (debug) e.printStackTrace();
                                continue;
                            }
                            rs.next(); int bid = rs.getInt(1);
                            //assume insert successfully

                            System.out.println("Please input the publisher of the book");
                            flag = false;
                            do {
                                if (flag) System.out.println("Publisher name too long");
                                flag =true; choice = in.readLine();
                            } while (choice.length() > 40);
                            int pid = mypublish.get_pid(con.stmt, choice);
                            if (pid == -1) continue;
                            System.out.println("Please input the publish year of the book");
                            int year = 0;
                            choice = in.readLine();
                            try {
                                year = Integer.parseInt(choice);
                            } catch (Exception e) {
                                alert("Parse publish year error");
                                if (debug) System.err.println(e.getMessage());
                                continue;
                            }
                            if (!mypublish.publish(con.stmt, pid, bid, year)) continue;


                            System.out.println("Please input the writer(s) of the book one by one");
                            flag = flag2 = false;
                            ArrayList<Integer> tmp = new ArrayList<>();
                            do {
                                flag1 = false;
                                do {
                                    if (flag1) alert("Author name too long"); flag1 = true;
                                    choice = in.readLine();
                                } while (choice.length() > 40);
                                int aid = 0;
                                aid = mywriter.get_aid(con.stmt, choice);
                                if (aid == -1 || !mywriter.iwrite(con.stmt, aid, bid)) {
                                    flag2= true; break;
                                }
                                flag = choose("Do you want to add another author?", 'y', 'n');
                                tmp.add(aid);
                            } while (!flag);
                            if (flag2) continue;

                            flag = false;
                            for (int i = 0; i < tmp.size(); i++) {
                                for (int j = 0; j < i; j++) {
                                    if (!mywriter.coauthor(con.stmt, bid, tmp.indexOf(i), tmp.indexOf(j))) {
                                        flag = true; break;
                                    }
                                }
                                if (flag) break;
                            }
                            if (flag) continue;

                        } else if (c == 5) {
                            flag1 = choose("Please Input the book id or isbn of the book", 'b', 'i');
                            int bid = 0;
                            if (!flag1) {
                                choice = in.readLine();
                                try {
                                    bid = Integer.parseInt(choice);
                                } catch(Exception e) {
                                    System.out.println(">_< Parse book id error!!!");
                                    if (debug) System.err.println(e.getMessage());
                                    continue;
                                }
                                sql = "select * from book where bid = " + Integer.toString(bid) + ";";
                                try {
                                    rs = con.stmt.executeQuery(sql);
                                } catch (Exception e) {
                                    alert("Check book id existence error");
                                    if (debug) System.err.println(e.getMessage());
                                    continue;
                                }
                                if (rs.next()) {
                                    System.out.println("Find the specific book successfully");
                                } else {
                                    alert("Can\'t find the specific book"); continue;
                                }
                            } else {
                                choice = in.readLine();
                                choice = choice.toUpperCase();
                                if (choice.length() != 10) {
                                    alert("ISBN length incorrect"); continue;
                                }
                                flag1 = false;
                                for (int i = 0; i < choice.length(); i++) {
                                    if ((choice.charAt(i) < '0' || choice.charAt(i) > '9') && choice.charAt(i) != 'X') {
                                        flag1 = true;
                                    }
                                }
                                if (flag1) {
                                    alert("ISBN format error"); continue;
                                }
                                sql = "select bid from book where isbn = " + choice + ";" ;
                                try {
                                    rs = con.stmt.executeQuery(sql);
                                } catch (Exception e) {
                                    alert("Find the book by isbn error");
                                    if (debug) System.err.println(e.getMessage());
                                    continue;
                                }
                                if (rs.next()) {
                                    bid = rs.getInt(1);
                                    System.out.println("Find the specific book successfully");
                                } else {
                                    alert("Can\'t find the book by isbn"); continue;
                                }
                            }
                            int num = 0; flag = false;
                            do {
                                System.out.println("How many copies you want to add?");
                                choice = in.readLine();
                                try {
                                    num = Integer.parseInt(choice);
                                } catch (Exception e) {
                                    alert("Parse add copy number error");
                                    if (debug) System.err.println(e.getMessage());
                                    num = -1; flag = true; break;
                                }
                            } while (num <= 0);
                            if (flag) continue;
                            sql = "update book set number_of_copies = number_of_copies + " + choice + " " +
                                    "where bid = "+ Integer.toString(bid)+";";
                            try {
                                con.stmt.execute(sql);
                            } catch (Exception e) {
                                alert("Add copy number error at sql");
                                if (debug) System.err.println(e.getMessage());
                                continue;
                            }
                            System.out.println("0w0 Add book copy number successfully!!!");
                        } else if (c == 6) {
                            do {
                                System.out.println("\tWhat statistic do you want to know ?");
                                System.out.println(" 1. the list of the m(default m = 10) most popular books");
                                System.out.println(" 2. the list of the m(default m = 10) most popular authors");
                                System.out.println(" 3. the list of the m(default m = 10) most popular publishers");

                                choice = in.readLine();
                            } while (choice.equals("") || (choice.charAt(0) < '1' || choice.charAt(0) > '3'));

                            String tmp;
                            System.out.println("Please input m(blank for default 10)");
                            tmp = in.readLine(); int m = 0;
                            if (tmp.equals("")) m = 10; else {
                                try {
                                    m = Integer.parseInt(tmp);
                                } catch (Exception e) {
                                    alert("Parse statistic number m error");
                                    if (debug) System.err.println(e.getMessage());
                                    continue;
                                }
                            }
                            if (choice.charAt(0) == '1') {
                                System.out.println("The list of the m most popular books");
                                tmp = "(select sum(amount) from buy where buy.bid = book.bid ";
                                tmp+= "and to_days(now())-to_days(buy_date)<=180)";
                                sql = "select bid,isbn,"+tmp+"as amount,title_words from book order by amount desc ";
                                sql+= "limit 0, " + Integer.toString(m) + ";";

                                //System.out.println(sql);

                                try {
                                    rs = con.stmt.executeQuery(sql);
                                } catch (Exception e) {
                                    alert("Qurey top m book error");
                                    if (debug) e.printStackTrace();
                                    continue;
                                }

                                System.out.println("book id\t\t\tisbn\t\t\tamount\t\ttitle");

                                while (rs.next()) {
                                    System.out.printf("%d\t\t\t%s\t\t\t", rs.getInt(1), rs.getString(2));
                                    if (rs.getString(3) == null) {
                                        System.out.print(0);
                                    } else System.out.print(rs.getInt(3));
                                    System.out.println("\t\t\t"+rs.getString(4));
                                }
                            } else if (choice.charAt(0) == '2') {
                                System.out.println("The list of the m most popular authors");
                                tmp = "(select sum(amount) from buy, iwrite ";
                                tmp+= "where buy.bid = iwrite.bid and iwrite.aid=author.aid ";
                                tmp+= "and to_days(now())-to_days(buy_date)<=180)";
                                sql = "select aid, "+tmp+" as amount, aname from author order by amount desc ";
                                sql+= "limit 0, " + Integer.toString(m) + ";";

                                //System.out.println(sql);

                                try {
                                    rs = con.stmt.executeQuery(sql);
                                } catch (Exception e) {
                                    alert("Qurey top m author error");
                                    if (debug) e.printStackTrace();
                                    continue;
                                }

                                System.out.println("author id\tamount\t\t\tname");

                                while (rs.next()) {
                                    if (rs.getString(2) != null)
                                        System.out.printf("%d\t\t\t%d\t\t\t", rs.getInt(1), rs.getInt(2));
                                    else System.out.printf("%d\t\t\t0\t\t\t", rs.getInt(1));
                                    System.out.println(rs.getString(3));
                                }
                            } else {
                                System.out.println("The list of the m most popular publishers");
                                tmp = "(select sum(amount) from buy, publish ";
                                tmp+= "where buy.bid = publish.bid and publish.pid=publisher.pid ";
                                tmp+= "and to_days(now())-to_days(buy_date)<=180)";
                                sql = "select pid, "+tmp+" as amount, pname from publisher order by amount desc ";
                                sql+= "limit 0, " + Integer.toString(m) + ";";

                                //System.out.println(sql);

                                try {
                                    rs = con.stmt.executeQuery(sql);
                                } catch (Exception e) {
                                    alert("Qurey top m publisher error");
                                    if (debug) e.printStackTrace();
                                    continue;
                                }

                                System.out.println("publisher id\tamount\t\t\tpublisher name");

                                while (rs.next()) {
                                     if (rs.getString(2) != null)
                                        System.out.printf("%d\t\t\t%d\t\t\t", rs.getInt(1), rs.getInt(2));
                                    else System.out.printf("%d\t\t\t0\t\t\t", rs.getInt(1));
                                    System.out.println(rs.getString(3));
                                }
                            }
                            System.out.println("Press Enter to continue...");
                            choice = in.readLine();
                        } else if (c == 7) {
                            do {
                                System.out.println("\tWhat user award do you want to give ?");
                                System.out.println(" 1. the top m (default 10) trusted user.");
                                System.out.println(" 2. the top m (default 10) helpful user.");
                                choice = in.readLine();
                            } while (choice.equals("") || (choice.charAt(0) < '1' || choice.charAt(0) > '2'));

                            String tmp;
                            System.out.println("Please input m(blank for default 10)");
                            tmp = in.readLine();
                            int m = 0;
                            if (tmp.equals("")) m = 10;
                            else {
                                try {
                                    m = Integer.parseInt(tmp);
                                } catch (Exception e) {
                                    alert("Parse statistic number m error");
                                    if (debug) System.err.println(e.getMessage());
                                    continue;
                                }
                            }

                            if (choice.charAt(0) == '1') {
                                System.out.println("Here's the top m most trusted user");
                                tmp = "((select count(*) from judge where cid2 = customer.cid and trust = true)-(";
                                tmp += "select count(*) from judge where cid2 = customer.cid and trust = false))";
                                sql = "select customer.cid, " + tmp + " as trust, customer.login_name from customer ";
                                sql += "order by trust desc limit 0, " + Integer.toString(m) + ";";
                                //System.out.println(sql);

                                try {
                                    rs = con.stmt.executeQuery(sql);
                                } catch (Exception e) {
                                    alert("Qurey top m trusted user error");
                                    if (debug) e.printStackTrace();
                                    continue;
                                }

                                System.out.println("user id\ttrust\t\t\tuser name");

                                while (rs.next()) {
                                    System.out.printf("%d\t\t\t%d\t\t\t", rs.getInt(1), rs.getInt(2));
                                    System.out.println(rs.getString(3));
                                }
                            } else {
                                System.out.println("Here's the top m most helpful user");
                                tmp = "(select sum(rate.score) from rate, feedback where rate.fid = feedback.fid ";
                                tmp += "and feedback.cid = customer.cid)";
                                sql = "select customer.cid, " + tmp + " as point, customer.login_name from customer ";
                                sql += "order by point desc limit 0, " + Integer.toString(m) + ";";

                                System.out.println(sql);

                                try {
                                    rs = con.stmt.executeQuery(sql);
                                } catch (Exception e) {
                                    alert("Qurey top m helpful user error");
                                    if (debug) e.printStackTrace();
                                    continue;
                                }

                                System.out.println("user id\tpoint\t\t\tuser name");

                                while (rs.next()) {
                                    System.out.printf("%d\t\t\t%d\t\t\t", rs.getInt(1), rs.getInt(2));
                                    System.out.println(rs.getString(3));
                                }
                            }
                            System.out.println("Press Enter to continue...");
                            choice = in.readLine();
                        } else {
                            leave = true; break;
                        }
                    }

                    while (!leave && logged_in) {
                        display_customer();
                        String choice = in.readLine(); int c = 0;
                        try {
                            c = Integer.parseInt(choice);
                        } catch (Exception e) {
                            alert("Parse customer's choice error");
                            if (debug) System.err.println(e.getMessage());
                            continue;
                        }
                        if (c < 1 || c > 12) continue;
                        if (c == 1) {
                            Boolean tmp = myadmin.Ques(con.stmt, cid);
                            if (tmp == null) continue;
                            if (tmp == false) {
                                alert("No administrator authority");
                                continue;
                            }
                            admin = true; break;
                        } else if (c == 2) {
                            logged_in = false; admin = false; break;
                        } else if (c == 3) {
                            do {
                                do {
                                    System.out.println("\tNow what do you want to change?");
                                    System.out.println(" 1. My full name");
                                    System.out.println(" 2. Password");
                                    System.out.println(" 3. My address");
                                    System.out.println(" 4. My phone number");
                                    System.out.println(" 5. Leave profile change with smile :)");
                                    choice = in.readLine();
                                } while (choice.equals("") && (choice.charAt(0) < '1' || choice.charAt(0) > '5'));
                                int k = choice.charAt(0) - '0';
                                if (k == 1) {
                                    flag = false;
                                    do {
                                        if (!flag) System.out.println("Please input your full name(3~30 character)");
                                            else alert("Full name length not correct");
                                        flag = true;
                                        choice = in.readLine();
                                    } while (choice.length() > 30 || choice.length() < 3);
                                    sql = "update customer set full_name = \'"+ polish(choice) + "\' where cid = " +
                                            Integer.toString(cid) + ";";
                                    try {
                                        con.stmt.execute(sql);
                                    } catch (Exception e) {
                                        alert("change full name error");
                                        if (debug) System.err.println(e.getMessage());
                                        continue;
                                    }
                                    System.out.println(">_< Changed full name successfully!!!");
                                } else if (k == 2) {
                                    flag = false; flag1 = false;
                                    Boolean tmp;
                                    do {
                                        if (!flag1) System.out.println("Please input your old password");
                                        else System.out.println("old password wrong");
                                        flag1 = true;
                                        choice = in.readLine();
                                        tmp = mylogin.check(con.stmt, cid, mymd5.getMD5(choice));
                                        if (tmp == null) {
                                            flag = true; break;
                                        }
                                    } while (tmp == false);
                                    if (flag) continue;

                                    String password, cur;

                                    do {
                                        System.out.println("Please input your new password(3~30 characters)");
                                        cur = in.readLine();
                                        System.out.println("Please confirm your new password");
                                        password = in.readLine();
                                    } while (!cur.equals(password) || cur.length() < 3 || cur.length() > 30);
                                    sql = "update customer set password = \'"+mymd5.getMD5(cur)+"\' where cid = "+
                                            Integer.toString(cid) + ";";
                                    try {
                                        con.stmt.execute(sql);
                                    } catch (Exception e) {
                                        alert("update password error");
                                        if (debug) System.err.println(e.getMessage());
                                        continue;
                                    }
                                    System.out.println("0w0 Update password successfully!!!");
                                } else if (k == 3) {
                                    flag = false;
                                    do {
                                        if (!flag)
                                            System.out.println("Please input your address(less than 100 character)");
                                        else alert("address length not correct");
                                        flag = true;
                                        choice = in.readLine();
                                    } while (choice.length() > 100);
                                    sql = "update customer set address = \'"+ polish(choice) + "\' where cid = " +
                                            Integer.toString(cid) + ";";
                                    try {
                                        con.stmt.execute(sql);
                                    } catch (Exception e) {
                                        alert("change address error");
                                        if (debug) System.err.println(e.getMessage());
                                        continue;
                                    }
                                    System.out.println(">_< Changed address successfully!!!");
                                } else if (k == 4) {
                                    long phone = 0;

                                    flag = false; flag1 = false;
                                    do {
                                        if (!flag)
                                            System.out.println("Please input your phone number(11 bit)");
                                        else alert("phone number format not correct");
                                        choice = in.readLine();
                                        flag = true; flag1 = (choice.length() != 11);
                                        for (int i = 0; i < choice.length(); i++) {
                                            if (choice.charAt(i) <'0' || choice.charAt(i) > '9') {
                                                flag1 = true; break;
                                            }
                                        }
                                    } while (flag1);

                                    sql = "update customer set phone_number = \'"+ choice + "\' where cid = " +
                                            Integer.toString(cid) + ";";
                                    try {
                                        con.stmt.execute(sql);
                                    } catch (Exception e) {
                                        alert("change phone number error");
                                        if (debug) System.err.println(e.getMessage());
                                        continue;
                                    }
                                    System.out.println(">_< Changed phone number successfully!!!");
                                } else break;
                            } while (true);
                        } else if (c == 4) {
                            flag = choose("How to choose the book you want to order? book id or isbn", 'b', 'i');
                            int bid = 0;
                            if (!flag) {
                                choice = in.readLine();
                                try {
                                    bid = Integer.parseInt(choice);
                                } catch (Exception e) {
                                    alert("parse book id error while shopping");
                                    if (debug) System.err.println(e.getMessage());
                                    continue;
                                }
                                //TODO VALIDATE BOOK ID
                            } else  {
                                //TODO ISBN CASE
                            }
                        } else if (c == 5) {
                        } else if (c == 6) {
                        } else if (c == 7) {
                        } else if (c == 8) {
                        } else if (c == 9) {
                        } else if (c ==10) {
                        } else if (c ==11) {
                        } else {
                            leave = true;
                        }
                    }

                } //else choose to leave
            }
            System.out.println("Leaving.....................");
            print_split();
            System.out.println("My alipay account is zxybazh@qq.com");
            System.out.println("Buy me a cup of coffee please :)");
            System.out.println("Welcome and Goodbye~");
            con.closeConnection();
        } catch (Exception e) {
        	 if (debug) e.printStackTrace();
        	 System.err.println (">_< Cannot connect to database server!!!");
        }
    }
}
