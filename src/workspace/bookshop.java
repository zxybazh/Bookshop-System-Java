package workspace;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.Scanner;

public class bookshop {
    final public static boolean debug = true;
    // TODO change the debug state
    private static boolean logged_in = false;
    private static boolean leave = false;

    private static String cname = "";
    private static int cid = 0;

    private static void Hello_world() {
        myfun.print_title();
        myfun.print_face();
    }
    private static void display_unlogged_in() {
        System.out.println("\tCurrently you are logged out.");
        System.out.println("\tWhat would you like to do next?");
        System.out.println("1.log in as a registered customer");
        System.out.println("2.register a new account");
        System.out.println("3.exit peacefully without hesitation");
    }
    private static void print_split() {
        System.out.println();
        System.out.println("**********Wexley's Book Shop**********");
        System.out.println("**************************************");
        System.out.println();
    }
    public static void main(String[] args) {
        myconnector con = null;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    Scanner cin = new Scanner(new BufferedInputStream(System.in));

        if (!debug) Hello_world();
        try {
            con = new myconnector();

            System.out.println("Connection established XD!!");
            System.out.println("0v0 Welcome to Wexley's Bookshop!!!");
            print_split();

            while (!logged_in) {
                display_unlogged_in();
                String choice = "";
                int c = 0;
                while ((choice = in.readLine()) == null || choice.length() == 0);
                try{
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
                    } catch(Exception e) {
                        System.out.println(">_< Log in process is broken, detailed information is below:");
                        if (debug) e.printStackTrace();
                        continue;
                    }
                    if (cid > 0) {
                        System.out.print("Log in succeed !! Now you are logged_in as " + cname);
                        System.out.println(", and your customer id is " + cid);
                        logged_in = true; break;
                    } else {
                        System.out.println(">_< Log in failed, wrong username or password, please try again!");
                        continue;
                    }
                } else if (c == 2) {
                    System.out.println("Please Input your username(3~30 characters)");
                    ResultSet rs;
                    boolean flag = false, flag1 = false, flag2 = false;
                    do {
                        if (flag) System.out.println(">_< Sorry, you username has been picked !!");
                        if (flag1) System.out.println(">_< Sorry, your username is tooooo short !!");
                        if (flag2) System.out.println(">_< Sorry, your username is tooooo long !!");
                        flag = flag1 = flag2 = false;

                        cname = in.readLine();
                        if (cname.length() < 3) {
                            flag1 = true; continue;
                        }
                        if (cname.length() > 30) {
                            flag2 = true; continue;
                        }
                        try {
                            rs = con.stmt.executeQuery("select * from customer where login_name = \'"+cname+"\';");
                        } catch (Exception e) {
                            System.out.println(">_< Query username duplicate error at sql");
                            if (debug) e.printStackTrace();
                            break;
                        }
                        if (rs.next()) flag = true;
                    } while (flag1 || flag2 || flag);

                    String password, tmp, full_name, addr;
                    long phone = 0;

                    flag = false; flag1 = false; flag2 = false;
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
                        flag = false; phone = 0;
                        for (int i = 0; i < tmp.length(); i++) {
                            if (tmp.charAt(i) < '0' || tmp.charAt(i) > '9') {
                                flag = true; break;
                            }
                            phone = phone * 10l + (long)(tmp.charAt(i) - '0');
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
                    leave = true; break;
                }
            }
            if (!leave) {
                print_split();
                System.out.println("Now loading Book Management System...");
                //TODO ADD FORMAL book management system
            } //else choose to leave

            System.out.println("Leaving.....................");
            print_split();
            System.out.println("My alipay account is zxybazh@qq.com");
            System.out.println("Buy me a cup of coffee please :)");
            System.out.println("Welcome and Goodbye~");
            cin.close();
            con.closeConnection();
        } catch (Exception e) {
        	 if (debug) e.printStackTrace();
        	 System.err.println (">_< Cannot connect to database server!!!");
        }
    }
}
