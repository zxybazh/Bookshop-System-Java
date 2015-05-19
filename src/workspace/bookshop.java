package workspace;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class bookshop {
    final public static boolean debug = false;
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
        System.out.println(" 4. Feedback my books");
        System.out.println(" 5. Book browse and buy books");
        System.out.println(" 6. Two degrees of separation");
        System.out.println(" 7. Make Suggestion");
        System.out.println(" 8. Trust/unTrust someone");
        System.out.println(" 9. Exit peacefully without hesitation");
    }

    private static void display_browse() {
        System.out.println("\tWhat do you want to do next?");
        System.out.println(" 1. Show browse result again");
        System.out.println(" 2. Show feedback");
        System.out.println(" 3. Make purchase");
        System.out.println(" 4. Exit book browse system");
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
        System.out.println(">_< " + s + "!!!"); pause();
    }
    public static void finish(String s) {
        System.out.println("0w0 " + s + "!!!"); pause();
    }
    public static String polish(String a) {
        String b = "";
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == '\'' || a.charAt(i) == '\"' || a.charAt(i) == '\\') {
                b += '\\';
            }
            b += a.charAt(i);
        }
        return b;
    }

    public static boolean runsql(String sql) {
        try {
            con.stmt.execute(sql);
        } catch (Exception e) {
            alert("Error while excecuting " + sql);
            if (debug) e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ResultSet querysql(String sql) {
        ResultSet rs;
        try {
            rs = con.stmt.executeQuery(sql);
        } catch (Exception e) {
            alert("Error while querying " + sql);
            if (debug) e.printStackTrace();
            return null;
        }
        return rs;
    }

    public static void print(String a) {
        System.out.println(a);
    }

    public static void degree(String x) {
        print("The two writers are "+x+" degree(s) separated.");
        pause();
    }

    public static void pause() {
        print("Press Enter to continue...");
        try {
            in.readLine();
        } catch (IOException e) {
            alert("Pause readline error");
            if (debug) e.printStackTrace();
        }
    }

    public static Integer getint(String s){
        int x = -1;
        try {
            x = Integer.parseInt(s);
        } catch (Exception e) {
            alert("Parse integer error");
            System.err.println(e.getMessage());
            return null;
        }
        return x;
    }

    public static void show_the_book(ResultSet rs) throws SQLException {
        do {
            print("************************************************************************");
            System.out.println("title    : " + rs.getString(5));
            print("book id\t\tisbn\t\tcover\t\tprice");
            String tmp = "%s\t\t\t%s\t";
            if (rs.getString(3) == null || rs.getString(3).toLowerCase().equals("null")) {
                tmp += "unknown";
            } else if (rs.getBoolean(3)) tmp += "hard"; else tmp += "soft";
            tmp += "\t\t%s\n";
            System.out.printf(tmp, rs.getString(1), rs.getString(2), rs.getString(7));
            System.out.println("keywords : " + rs.getString(4));
            System.out.println("subjects : " + rs.getString(6));
            print("************************************************************************");
        } while (rs.next());
    }

    public static void main(String[] args) {

        if (!debug) Hello_world();
        try {
            con = new myconnector();

            print("Connection established XD!!");
            print("0v0 Welcome to Wexley's Bookshop!!!");
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
                            if (flag) alert("Sorry, you username has been picked");
                            if (flag1) alert("Sorry, your username is tooooo short");
                            if (flag2) alert("Sorry, your username is tooooo long");
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
                                aid = mywriter.fetch_aid(con.stmt, choice);
                                if (aid == -1 || !mywriter.iwrite(con.stmt, aid, bid)) {
                                    flag2= true; break;
                                }
                                flag = choose("Do you want to add another author?", 'y', 'n');
                                tmp.add(aid);
                                //System.err.println(aid);
                            } while (!flag);
                            if (flag2) continue;

                            flag = false;
                            for (int i = 0; i < tmp.size(); i++) {
                                for (int j = 0; j < i; j++) {
                                    if (!mywriter.coauthor(con.stmt, bid, tmp.get(i), tmp.get(j))) {
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

                                if (debug) System.out.println(sql);

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
                        if (c < 1 || c > 9) continue;
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
                            print("\tCurrently you have make the following order(s):");
                            sql = "select buy.oid, buy.buy_date, buy.amount, buy.bid, book.title_words from buy, book " +
                                    "where cid = " + Integer.toString(cid) + " and buy.bid = book.bid;";
                            rs = querysql(sql);
                            if (rs == null) continue;
                            if (rs.next()) {
                                System.out.println("order id\t\tdate\t\tamount\t\tbook id\t\tbtitle");
                                do {
                                    System.out.printf("%s\t\t\t%s\t\t\t%s\t\t\t%s\t%s\n", rs.getString(1),
                                            rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                                } while (rs.next());
                            } else {
                                alert("You haven't made any purchase yet");
                                continue;
                            }

                            Integer bid = null;
                            flag = false;
                            flag2 = false;
                            do {
                                if (flag2) {
                                    flag = choose("Continue to feedback?", 'y', 'n');
                                    if (flag) break;
                                }

                                print("Please Input the book id you want to feedback");

                                flag2 = true;
                                choice = in.readLine();
                                bid = getint(choice);
                                if (bid == null) {
                                    flag = true;
                                    break;
                                }
                                flag1 = false;
                                sql = "select * from buy where cid = " + Integer.toString(cid) + " and bid = " +
                                        Integer.toString(bid) + ";";
                                rs = querysql(sql);
                                if (rs == null) {
                                    flag = true;
                                    break;
                                }
                                if (!rs.next()) {
                                    flag1 = true;
                                    alert("No such order exists");
                                    continue;
                                }

                                sql = "select * from feedback where cid = " + Integer.toString(cid) + " and bid = " +
                                        Integer.toString(bid) + ";";
                                rs = querysql(sql);
                                if (rs == null) {
                                    flag = true;
                                    break;
                                }
                                if (rs.next()) {
                                    flag1 = true;
                                    alert("You have feedback for this book before");
                                    continue;
                                }
                            } while (flag1);
                            if (flag) continue;


                            print("Please input a score for this book[0= terrible, 10= masterpiece]");
                            Integer score = 0;
                            boolean flag3 = false, flag4 = false;
                            do {
                                if (flag3) alert("Please input the score between 0~10");
                                flag3 = true;
                                choice = in.readLine();
                                score = getint(choice);
                                if (score == null) {
                                    flag4 = true;
                                    break;
                                }
                            } while (score < 0 || score > 10);
                            if (flag4) {
                                flag = true;
                                break;
                            }

                            print("Please input a short txt for comment(can be blank, no more than 100 characters)");
                            flag3 = false;
                            do {
                                if (flag3) alert("too long comment");
                                flag3 = true;
                                choice = in.readLine();
                            } while (choice.length() > 100);

                            sql = "insert into feedback values(null, "+Integer.toString(bid)+", "+Integer.toString(cid);
                            sql+= ", now(), "+Integer.toString(score)+", \'"+polish(choice)+"\');";
                            flag3 = runsql(sql);
                            if (flag3) print("0w0 Make feedback successfully!!!");
                        } else if (c == 5) {
                            print("Now entering book browse system...");
                            print("The rule of browse is SUM OF PRODUCTS, you may ask like the following:");
                            print("the book in { [S_1] OR [S_2] OR ... OR [S_n] }");
                            print("S_i is like { [C_1] AND [C_2] AND ... AND [C_n] }");
                            print("C_i can be constraint about  authors, and/or publisher, " +
                                    "and/or title-words, and/or subject.");
                            print("Get it?"); pause();

                            String br;

                            boolean stop = false;

                            do {
                                sql = "select bid,isbn,cover_format,key_words,title_words,subjects,price";
                                sql+= " from book where number_of_copies > 0";
                                boolean firsts = true, stop1 = false;
                                do {
                                    String sqls = "select distinct book.bid from publisher, book, publish " +
                                            " where (book.bid = publish.bid " +
                                            "and publisher.pid = publish.pid)";
                                    boolean firstc = true, stop2 = false;
                                    boolean[] hash = new boolean[10];
                                    do {
                                        Integer cc = null;
                                        print("\tWhat Constraint do you want to add?");
                                        print("1.Publish year");
                                        print("2.Publisher");
                                        print("3.Author");
                                        print("4.Title");
                                        print("5.Subject");
                                        print("6.Key words");
                                        print("7.No constraint to add");

                                        choice = in.readLine();
                                        cc = getint(choice);
                                        if (cc == null) continue;
                                        if (cc < 1 || cc > 7) {
                                            alert("improper input for the constraint");
                                            continue;
                                        }

                                        if (hash[cc]) {
                                            alert("You have added this constraint");
                                            continue;
                                        }
                                        switch (cc) {
                                            case 1: {
                                                print("Please input the publish year");
                                                choice = in.readLine();
                                                Integer year = getint(choice);
                                                if (year == null) continue;

                                                sqls += " and (publish.year = " + choice + ")";
                                                break;
                                            }
                                            case 2: {
                                                print("Please input the publisher(can be part of it)");
                                                choice = in.readLine();
                                                sqls += " and (publisher.pname like \'%" + polish(choice) + "%\')";
                                                break;
                                            }
                                            case 3: {
                                                print("Please input the author(s) one by one");
                                                Integer aid;
                                                do {
                                                    aid = null;
                                                    if (choose("input the writer buy author id or author name", 'i', 'n')) {
                                                        choice = in.readLine();
                                                        aid = mywriter.aname_aid(con.stmt, choice);
                                                        if (aid == null) continue;
                                                        if (aid == -1) {
                                                            alert("No such author");
                                                            continue;
                                                        }
                                                    } else {
                                                        choice = in.readLine();
                                                        aid = getint(choice);
                                                        Boolean tmp = mywriter.valid(con.stmt, aid);
                                                        if (tmp == null) continue;
                                                        if (tmp == false) {
                                                            alert("No such author");
                                                            continue;
                                                        }
                                                    }
                                                    sqls += " and (exists(select * from iwrite where " +
                                                            "iwrite.bid = book.bid and iwrite.aid ="
                                                            + Integer.toString(aid) + "))";
                                                } while (!choose("Continue input author(s)?", 'y', 'n'));
                                                break;
                                            }
                                            case 4: {
                                                print("Please input the title(can be part of it)");
                                                choice = in.readLine();
                                                sqls += " and (title_words like \'%" + polish(choice) + "%\')";
                                                break;
                                            }
                                            case 5: {
                                                print("Please input the subject(can be part of it)");
                                                choice = in.readLine();
                                                sqls += " and (subjects like \'%" + polish(choice) + "%\')";
                                                break;
                                            }
                                            case 6: {
                                                print("Please input the keyword(can be part of it)");
                                                choice = in.readLine();
                                                sqls += " and (key_words like \'%" + polish(choice) + "%\')";
                                                break;
                                            }
                                            default: {
                                                stop2 = true;
                                            }
                                        }
                                        hash[cc] = true;
                                        firstc = false;
                                        stop2 = choose("Continue this sum S?", 'y', 'n');
                                    } while (!stop2);
                                    if (!firstc) {
                                        if (firsts) {
                                            sql += " and ((bid in (" + sqls + "))";
                                        } else {
                                            sql += " or (bid in (" + sqls + "))";
                                        }
                                        firsts = false;
                                    }
                                    stop1 = choose("Continue more product C?", 'y', 'n');
                                } while (!stop1);
                                if (!firsts) sql += ")";

                                do {
                                    print("How to order the result? (a) by year, or (b) by the average numerical " +
                                            "score of the feedbacks, or (c) by the average numerical score " +
                                            "of the trusted user feedbacks");
                                    choice = in.readLine();
                                } while (choice.length() != 1 || choice.charAt(0) < 'a' || choice.charAt(0) > 'c');

                                if (choice.charAt(0) == 'a') {
                                    sql += "order by (select year from publish where publish.bid = book.bid) ";
                                    if (choose("sort by descending or ascending", 'd', 'a')) sql += "asc";
                                        else sql +="desc";
                                } else if (choice.charAt(0) == 'b') {
                                    sql += "order by (select avg(score) from feedback where feedback.bid = book.bid) desc";
                                } else {
                                    sql += "order by (select avg(score) from feedback where feedback.bid = book.bid" +
                                            " and feedback.cid in (select cid from customer where (" +
                                            "select count(*) from judge where judge.cid2 = customer.cid and judge.trust = true)>=(" +
                                            "select count(*) from judge where judge.cid2 = customer.cid and judge.trust = false))) desc";
                                }

                                sql += ";";

                                br = sql;
                                rs = querysql(br);
                                if (rs == null) continue;
                                if (!rs.next()) {
                                    alert("No such book available");
                                } else {
                                    show_the_book(rs);
                                }

                                stop = choose("Continue browse input?", 'y', 'n');
                            } while(!stop);

                            Integer cc = null;
                            do {
                                display_browse();
                                choice = in.readLine();
                                cc = getint(choice);
                                if (cc == null) break;
                                if (cc == 1) {
                                    rs = querysql(br);
                                    if (debug) print(br);
                                    if (rs == null) break;
                                    if (!rs.next()) {
                                        alert("No such book avaliable");
                                        break;
                                    }
                                    show_the_book(rs);
                                } else if (cc == 3) {
                                    Integer bid, amount;
                                    print("Now enter the book id please:");
                                    choice = in.readLine();
                                    bid = getint(choice);
                                    if (bid == null) break;
                                    if (!mybook.valid(con.stmt, bid)) {
                                        alert("No such book exists");
                                        break;
                                    }
                                    print("Now enter the amount please:");
                                    choice = in.readLine();
                                    amount = getint(choice);
                                    if (amount == null) break;
                                    sql = "select number_of_copies, price from book where " +
                                            "bid ="+Integer.toString(bid)+";";

                                    rs = querysql(sql);
                                    if (rs == null) break;
                                    rs.next();
                                    if (rs.getInt(1) < amount) {
                                        alert("Not enough book available"); continue;
                                    }
                                    double cost = rs.getDouble(2);

                                    sql = "update book set number_of_copies = number_of_copies-"
                                            +Integer.toString(amount)+" where bid ="+Integer.toString(bid)+";";
                                    Boolean tmp = runsql(sql);
                                    if (!tmp) break;
                                    sql = "insert into buy values(null, "+Integer.toString(bid)+", "+Integer.toString(cid)+", "
                                            +Integer.toString(amount)+", now());";
                                    tmp = runsql(sql);
                                    if (!tmp) break;

                                    cost *= amount;
                                    finish("Make purchase successfully, the total cost is " + Double.toString(cost));
                                } else if (cc == 2) {
                                    Integer bid = null;
                                    print("Now enter the book id please:");
                                    choice = in.readLine();
                                    bid = getint(choice);
                                    if (bid == null) break;
                                    if (!mybook.valid(con.stmt, bid)) {
                                        alert("No such book exists");
                                        break;
                                    }

                                    Integer nn = null;
                                    do {
                                        print("Now specify the value of n[1-10]:");
                                        choice = in.readLine();
                                        nn = getint(choice);
                                    } while (nn == null || nn < 1 || nn > 10);

                                    sql = "select fid, login_name, fdate, score, text, ";
                                    sql+= "(select avg(score) from rate where ";
                                    sql+= "rate.fid = feedback.fid) as rating";
                                    sql+= " from customer, feedback where customer.cid = feedback.cid and bid = ";
                                    sql+= Integer.toString(bid)+" order by rating desc limit 0, "+Integer.toString(nn)+";";

                                    rs = querysql(sql);
                                    if (rs == null) break;
                                    if (!rs.next()) {
                                        alert("No such feedback available");
                                        continue;
                                    }
                                    print("Book id = "+Integer.toString(bid)+", following are the feedbacks:");
                                    print("feadback id\t\tuser name\t\tdate\t\tscore\t\ttext");
                                    do {
                                        System.out.printf("%s\t\t\t\t%s\t\t\t%s\t\t%s\t%s\n", rs.getString(1),
                                                rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                                    } while (rs.next());
                                    pause();

                                    boolean stop_rate = false, first_time = true;
                                    stop_rate = choose("Rate the feedbacks?", 'y', 'n');
                                    Integer fid;
                                    while (!stop_rate) {
                                        if (!first_time) stop_rate = choose("Continue to rate the feedbacks?", 'y', 'n');
                                        first_time = false;
                                        if (stop_rate) break;
                                        if (choose("Which feedback do you want to rate, please input "
                                                +"the user name or feedback id?", 'u', 'f')) {
                                            choice = in.readLine();
                                            fid = getint(choice);
                                            if (fid == null) break;
                                            if (!myfeedback.valid(con.stmt, fid)) {
                                                alert("No such feedback");
                                                continue;
                                            }
                                        } else {
                                            choice = in.readLine();
                                            fid = myfeedback.username_bid_fid(con.stmt, choice, bid);
                                            if (fid == null) break;
                                            if (fid == -1) {
                                                alert("No such feedback");
                                                continue;
                                            }
                                        }

                                        sql = "select * from feedback where fid = "+Integer.toString(fid)+" and cid = "
                                                +Integer.toString(cid);
                                        rs = querysql(sql);
                                        if (rs == null) break;
                                        if (rs.next()) {alert("You can not rate your own feedbacks"); continue;}

                                        sql = "select * from rate where fid = "+Integer.toString(fid)+" and cid = "
                                                +Integer.toString(cid);
                                        rs = querysql(sql);
                                        if (rs == null) break;
                                        if (rs.next()) {alert("You have rated this feedback"); break;}

                                        do {
                                            print("How do you like this feedback? give a numerical score of 0-2 to "
                                                +"represent \'useless\', \'useful\', \'very useful\' respectively.");
                                            choice = in.readLine();
                                        } while (choice.length() != 1 || choice.charAt(0) < '0'
                                                || choice.charAt(0) > '2');

                                        sql = "insert into rate values("+Integer.toString(cid)+", ";
                                        sql+= Integer.toString(fid)+", "+choice+");";

                                        boolean tmp = runsql(sql);
                                        if (tmp == false) break;
                                        else finish("Add rate successfully");
                                    }
                                }
                            } while (cc != 4);
                        } else if (c == 6) {
                            Integer aid1 = -1, aid2 = -1;
                            flag = false; flag1 = false;
                            do {
                                if (flag1) {
                                    flag = choose("Didn't find the writer. continue?", 'y', 'n');
                                    if (flag) break;
                                }
                                System.out.println("Please input the name of the first writer");
                                flag1 = true;
                                choice = in.readLine();
                                aid1 = mywriter.aname_aid(con.stmt, choice);
                                if (aid1 == null) {
                                    flag = true; break;
                                }
                            } while (aid1 == -1);
                            if (flag) continue;

                            flag = false; flag1 = false;
                            do {
                                if (flag1) {
                                    flag = choose("Didn't find the writer. continue?", 'y', 'n');
                                    if (flag) break;
                                }
                                System.out.println("Please input the name of the second writer");
                                flag1 = true;
                                choice = in.readLine();
                                aid2 = mywriter.aname_aid(con.stmt, choice);
                                if (aid2 == null) {
                                    flag = true; break;
                                }
                            } while (aid2 == -1);
                            if (flag) continue;

                            if (aid1 == aid2) {
                                degree("0"); continue;
                            }

                            if (aid1 > aid2) {
                                int tmp = aid1;
                                aid1 = aid2;
                                aid2 = tmp;
                            }

                            sql = "select * from coauthor where aid1 = "+Integer.toString(aid1)+" and aid2 = "+
                                    Integer.toString(aid2) + "; ";
                            rs = querysql(sql);
                            if (rs == null) continue;
                            if (rs.next()) {
                                degree("1"); continue;
                            }

                            sql = "select max(aid) from author;";
                            rs = querysql(sql);
                            if (rs == null) continue;
                            int n = 0;
                            if (rs.next()) n = rs.getInt(1);

                            flag = false;
                            for (int i = 1; i <= n; i++) if (i != aid1 && i != aid2) {
                                sql = "select * from coauthor x, coauthor y where x.aid1 = " +
                                        Integer.toString(Math.min(aid1, i)) + " and x.aid2 = " +
                                        Integer.toString(Math.max(aid1, i)) + " and y.aid1 = " +
                                        Integer.toString(Math.min(aid2, i)) + " and y.aid2 = " +
                                        Integer.toString(Math.max(aid2, i))+";";
                                rs = querysql(sql);
                                if (rs == null) {
                                    flag = true; break;
                                }
                                if (rs.next()) {
                                    degree("2"); flag = true; break;
                                }
                            }
                            if (flag) continue;

                            degree("more than 2");
                        } else if (c == 7) {
                            do {
                                flag = choose("Currently you want suggestions from your "
                                        +"order or specific book?", 'o', 's');
                                if (!flag) {
                                    sql = "select bid,isbn,price,title_words, (";
                                    sql+= "select sum(amount) from buy where buy.bid = book.bid and buy.cid in";
                                    sql+= "(select cid from customer where exists (";
                                    sql+= "select * from buy as x where x.cid = customer.cid and book.bid = x.bid) and ";
                                    sql+= "exists (select * from buy as y where y.cid = customer.cid and y.bid in (";
                                    sql+= "select bid from buy as z where z.cid = "+Integer.toString(cid)+")))) as amount ";
                                    sql+= "from book where not exists ";
                                    sql+= "(select * from buy where buy.cid = "+Integer.toString(cid);
                                    sql+= " and buy.bid = book.bid) and exists (select * from customer where exists (";
                                    sql+= "select * from buy as x where x.cid = customer.cid and book.bid = x.bid) and ";
                                    sql+= "exists (select * from buy as y where y.cid = customer.cid and y.bid in (";
                                    sql+= "select bid from buy as z where z.cid = "+Integer.toString(cid)+"))) order"+
                                            " by amount desc;";

                                    rs = querysql(sql);
                                    if (rs == null) break;
                                    if (rs.next()) {
                                        print("book id\t\t\tisbn\t\t\tprice\t\t\tamount\t\t\ttitle");
                                        do {
                                            System.out.printf("%s\t\t\t%s\t\t\t%s\t\t\t%s\t\t\t%s\n", rs.getString(1),
                                                    rs.getString(2), rs.getString(3), rs.getString(5), rs.getString(4));
                                        } while (rs.next());
                                        pause();
                                    } else {
                                        alert("No suggestions available");
                                    }
                                } else {
                                    Integer bid = null;
                                    flag1 = !choose("Input the book\'s book id or isbn?", 'b', 'i');
                                    if (flag1) {
                                        print("Now enter the book id please:");
                                        choice = in.readLine();
                                        bid = getint(choice);
                                        if (bid == null) break;
                                        if (!mybook.valid(con.stmt, bid)) {
                                            alert("No such book exists");
                                            break;
                                        }
                                    } else {
                                        print("Now enter the isbn please:");
                                        choice = in.readLine();
                                        bid = mybook.isbn_bid(con.stmt, choice);
                                        if (bid == null) break;
                                        if (bid == -1) {
                                            alert("No such book exists"); break;
                                        }
                                    }
                                    sql = "select bid,isbn,price,title_words,(select sum(amount) from buy where ";
                                    sql += "buy.bid = book.bid and buy.cid in(select cid from customer where exists (";
                                    sql+= "select cid from buy as x where x.cid = customer.cid and book.bid = x.bid) and ";
                                    sql+= "exists (select * from buy as y where y.cid = customer.cid and y.bid = "
                                            +Integer.toString(bid)+"))) as amount from book where not exists ";
                                    sql+= "(select * from buy where buy.cid = "+Integer.toString(cid);
                                    sql+= " and buy.bid = book.bid) and exists (select * from customer where exists (";
                                    sql+= "select * from buy as x where x.cid = customer.cid and book.bid = x.bid) and ";
                                    sql+= "exists (select * from buy as y where y.cid = customer.cid and y.bid = "
                                            +Integer.toString(bid)+")) order by amount desc;";

                                    rs = querysql(sql);
                                    if (rs == null) break;
                                    if (rs.next()) {
                                        print("book id\t\t\tisbn\t\t\tprice\t\t\tamount\t\t\ttitle");
                                        do {
                                            System.out.printf("%s\t\t\t%s\t\t\t%s\t\t\t%s\t\t\t%s\n", rs.getString(1),
                                                    rs.getString(2), rs.getString(3), rs.getString(5), rs.getString(4));
                                        } while (rs.next());
                                        pause();
                                    } else {
                                        alert("No suggestions available");
                                    }
                                }
                                flag = choose("Continue to make suggestions?", 'y', 'n');
                            } while (!flag);
                        } else if (c == 8) {
                            flag = choose("Who do you want to trust or untrust, choose by "
                                    +"user name or costomer id?", 'u', 'c');
                            Integer cid2 = null;
                            if (flag) {
                                print("Now enter the customer id please:");
                                choice = in.readLine();
                                cid2 = getint(choice);
                                if (cid2 == null) continue;
                                Boolean tmp = mycustomer.valid(con.stmt, cid2);
                                if (tmp == null) continue;
                                if (!tmp) {
                                    alert("NO such user");
                                    continue;
                                }
                            } else {
                                print("Now enter the user name please:");
                                choice = in.readLine();
                                cid2 = mycustomer.username_cid(con.stmt, choice);
                                if (cid2 == null) continue;
                                if (cid2 == -1) {
                                    alert("No such user");
                                    continue;
                                }
                            }
                            if (cid2 == cid) {
                                alert("You can not judge yourself");
                                continue;
                            }

                            sql = "select * from judge where cid1 = "+Integer.toString(cid)+" and cid2 = ";
                            sql+= Integer.toString(cid2)+";";

                            rs = querysql(sql);
                            if (rs == null) continue;
                            if (rs.next()) {
                                alert("You have (un)trusted the customer"); continue;
                            }

                            flag = choose("How you want to judge the customer, trusted or untrusted?", 't', 'u');
                            sql = "insert into judge values("+Integer.toString(cid)+", "+Integer.toString(cid2);
                            sql+= ", "+(flag?"False":"True")+");";

                            Boolean tmp = runsql(sql);
                            if (tmp) {
                                print("0W0 Add (un)trust information successfully!!!");
                            }
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
