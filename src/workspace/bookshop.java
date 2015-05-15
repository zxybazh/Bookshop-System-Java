package workspace;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;

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
        System.out.println(" 8. Book browse");
        System.out.println(" 9. Exit peacefully without hesitation");

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
    private static void alert(String s) {
        System.out.println(">_< " + s + "!!!");
    }
    /*
    private static void runsql(String sql) {
        try {
            con.stmt.execute(sql);
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
                                rs = con.stmt.executeQuery("select * from customer where login_name = \'" + cname + "\';");
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
                                                "select cid from customer where login_name = \'" +choice+"\';");
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

                            flag2 = choose("\tYou can change in two ways\n 1. Make the user customer\n 2. Make the user administrator", '1', '2');

                            sql = "update customer set admin =";
                            if (flag) sql += "true";
                                else sql += "false";
                            sql += " where cid = "+Integer.toString(k)+";";

                            try {
                                System.out.println(sql);
                                con.stmt.execute(sql);
                            } catch (Exception e) {
                                System.out.println(">_< change authority error");
                                if (debug) e.printStackTrace();
                                continue;
                            }
                            System.out.println("0w0 Changed authority successfully!!!");
                        } else if (c == 4) {
                            long isbn = 0; flag1 = false;
                            do {
                                if (flag1) System.out.println("Please input the 10 bit isbn in the correct way:");
                                    else System.out.println("Please input the 10 bit isbn for the new book:");
                                choice = in.readLine();
                                flag = false; isbn = 0;
                                if (choice.length() != 10) {
                                    flag = true; continue;
                                }
                                for (int i = 0; i < choice.length(); i++) {
                                    if (choice.charAt(i) < '0' || choice.charAt(i) > '9') {
                                        flag = true; break;
                                    } else {
                                        isbn = isbn * 10 + choice.charAt(i) - '0';
                                    }
                                }
                            } while (flag);

                            sql = "select * from book where isbn = "+Long.toString(isbn)+";";
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

                            sql = "insert into book values(null, "+Long.toString(isbn)+", ";
                            if (format != null) sql += format.toString(); else sql += "null";
                            sql += ", \'" + w1 + "\', \'" + w2 + "\', \'" + w3 + "\', "+Double.toString(price);
                            sql += ", " + Integer.toString(num) + ");";

                            try {
                                con.stmt.execute(sql);
                            } catch (Exception e) {
                                System.out.println(">_< Add book information to SQL error!!");
                                if (debug) e.printStackTrace();
                                continue;
                            }
                            System.out.println("Add book information successfully !!!");
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
                                if (choice.length() != 10) {
                                    alert("ISBN length incorrect"); continue;
                                }
                                flag1 = false;
                                for (int i = 0; i < choice.length(); i++) {
                                    if (choice.charAt(i) < '0' || choice.charAt(i) > '9') {
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
                                System.out.println(" 1. the list of the m(default m= 10) most popular books");
                                System.out.println(" 2. the list of the m(default m= 10) most popular authors");
                                System.out.println(" 3. the list of the m(default m= 10) most popular publishers");

                                choice = in.readLine();
                            } while (choice.equals("") || (choice.charAt(0) < '1' || choice.charAt(0) > '2'));

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
                            //TODO statistic feature
                            if (choice.charAt(0) == '1') {

                            } else if (choice.charAt(0) == '2') {

                            } else {

                            }

                        } else if (c == 7) {
                            //TODO User Awards
                        } else if (c == 8) {
                            //TODO BOOK BROWSE
                        } else {
                            leave = true; break;
                        }
                    }

                    while (!leave && logged_in) {
                        display_customer();

                        System.out.println("Building...");
                        leave = true;
                        //TODO add customer feature.

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
