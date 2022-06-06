package app;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
public class Program {
    public static void console() throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        String in = "";
        System.out.println("Have you got account? (yes/no)");
        in = sc.nextLine();
        Connection con = connectToDb();
        Statement st = con.createStatement();
        if (in.equals("yes")) {
            System.out.print("LOGIN: ");
            String login = sc.nextLine();
            System.out.print("PASS: ");
            String pass = sc.nextLine();
            if (checkLogin(st, login, pass) == 0) {
                System.out.println("You are login in!");
                startConsoleGame(login, st);
            }
            else
                System.out.println("Wrong data!");
        } else if (in.equals("no"))
            createAccount(st);
        else
            throw new InputMismatchException();
        sc.close();
    }

    public static void gui() {
    }

    public static void createAccount(Statement st) throws SQLException {
        while (true)
        {
            System.out.print("Create login: ");
            Scanner sc = new Scanner(System.in);
            String login = sc.nextLine();
            System.out.print("Write password: ");
            String pass = sc.nextLine();
            if (checkLogin(st,login,pass) != 1)
                System.out.println("Account with this name exist! Try again");
            else
            {
                st.executeUpdate("INSERT INTO users(name,password,charsID) VALUES ('"
                        +login+"','"+pass+"',"+"''"+");");
                System.out.println("Account created! Start game");
                startConsoleGame(login, st);
                sc.close();
                break;
            }
        }
    }

    public static Connection connectToDb() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:postgresql://localhost:5432/uterese");
        cfg.setUsername("uterese");
        HikariDataSource ds = new HikariDataSource(cfg);
        conn = ds.getConnection();
        return conn;
    }

    public static int checkLogin(Statement st, String log, String pass) throws SQLException {
        ArrayList<String> l = new ArrayList<String>();
        ArrayList<String> p = new ArrayList<String>();
        ResultSet rs = st.executeQuery("SELECT name FROM users");
        while (rs.next())
            l.add(rs.getString("name"));
        rs = st.executeQuery("SELECT password FROM users");
        while (rs.next())
            p.add(rs.getString("password"));
        if (l.contains(log) && p.contains(pass))
            return 0;
        if (l.contains(log) && !p.contains(pass))
            return 2;
        return 1;
    }

    public static void main(String[] args)
    {
        try {
            if (args[0].equals("console")) {
                console();
            } else if (args[0].equals("gui")) {
                gui();
            } else
                throw new IllegalArgumentException();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void startConsoleGame(String log, Statement st) throws SQLException {
        System.out.println("Hi, "+log+"!");
        System.out.println("Welcome to the game!");
        System.out.println("List of your characters: ");
        ArrayList<String> chars = getCharsName(log,st);
        for (String s : chars)
            System.out.println("Character name: "+s);
        System.out.println("Choose exist character or create new. (create/name of char)");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        if (in.equals("create"))
            createChar(log,st,sc);
    }

    public static ArrayList<String> getCharsName(String log, Statement st) throws SQLException {
        String ids = ""; ArrayList<String> lst = new ArrayList<String>();
        ArrayList<String> res = new ArrayList<String>();
        ResultSet rs = st.executeQuery("SELECT charsid FROM users WHERE name = '"+log+"'");
        while (rs.next())
            ids = rs.getString("charsid");
        char []buff = ids.toCharArray();
        StringBuilder b = new StringBuilder();
        for (char a :buff)
        {
            if (a == ',')
            {
                lst.add(b.toString());
                b = new StringBuilder();
            }
            else
                b.append(a);
        }
        lst.add(b.toString());lst.remove(lst.size()-1);
        for (int i = 0;i < lst.size();i++)
        {
            rs = st.executeQuery("SELECT name FROM characters WHERE id = "+lst.get(i));
            while (rs.next())
                res.add(rs.getString("name"));
        }
        return res;
    }

    public static void createChar(String log, Statement st, Scanner sc) throws SQLException {
        while (true)
        {
            System.out.print("Write character name: ");
            String name = checkCharName(st,sc);
            System.out.print("Choose character class (mage or warrior): ");
            String cls = checkCharClass(sc);
            ArrayList<Integer> ids = new ArrayList<Integer>();
            ResultSet rs = st.executeQuery("SELECT id FROM characters");
            while (rs.next())
                ids.add(rs.getInt("id"));
            int id = ids.get(ids.size()-1) + 1;
            st.executeUpdate("INSERT INTO characters(id,name,class,lvl,exp,attack,defense,hp,equip,inventory,position) " +
                    "VALUES("+id+",'"+name+"','"+cls+"',"+1+","+1000+","+5+","+3+","+100+",'','','');");
            rs = st.executeQuery("SELECT charsid FROM users WHERE name = '"+log+"'");String i = "";
            while (rs.next())
                i = rs.getString("charsid");
            st.executeUpdate("UPDATE users SET charsid = '"+i+id+",' WHERE name = '"+log+"'");
            break;
        }
    }

    public static String checkCharName(Statement st, Scanner sc) throws SQLException {
        String s = sc.nextLine();
        ResultSet rs = st.executeQuery("SELECT name FROM characters WHERE name = '"+s+"'");
        String check = "";
        while (rs.next())
            check = rs.getString("name");
        if (check.equals(""))
            return s;
        else
            System.out.println("Character with this name exit!");
        s = checkCharName(st, sc);
        return s;
    }

    public static String checkCharClass(Scanner sc)
    {
        String s = sc.nextLine();
        if (!s.equals("mage") && !s.equals("warrior")) {
            System.out.println("Wrong type of class! Try again (mage or warrior)");
            s = checkCharClass(sc);
        }
        return s;
    }
}
