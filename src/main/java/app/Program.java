package app;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

import classes.User;
import classes.Utils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.*;
public class Program {
    static User u = null;
    public static void console() throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        String in = "";
        System.out.println("Hello! Have you got account? (yes/no)");
        in = sc.nextLine();
        Connection con = connectToDb();
        Statement st = con.createStatement();
        if (in.equals("yes")) {
            System.out.print("LOGIN: ");
            String login = sc.nextLine();
            System.out.print("PASS: ");
            String pass = sc.nextLine();
            if (checkLogin(st, login, pass) == 0) {
                u = new User(login,pass);
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
                int Id = getLastId(st) + 1;
                System.out.println(Id);
                st.executeUpdate("INSERT INTO users(id,name,password,charsID) VALUES ("+Id+",'"
                        +login+"','"+pass+"',"+"''"+");");
                System.out.println("Account created! Start game");
                startConsoleGame(login, st);
                sc.close();
                break;
            }
        }
    }
    public static int getLastId(Statement st) throws SQLException
    {
        int id = 0;
        ResultSet rs = st.executeQuery("SELECT id FROM users");
        while(rs.next()) { id = rs.getInt(1);}
        return id;
    }
    public static Connection connectToDb() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        cfg.setUsername("postgres");cfg.setPassword("123");
        HikariDataSource ds = new HikariDataSource(cfg);
        conn = ds.getConnection();
        return conn;
    }

    public static int checkLogin(@NotNull Statement st, String log, String pass) throws SQLException {
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

    public static void main(String @NotNull [] args)
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
        while (true)
        {
            System.out.println("Hi, "+log+"!");
            System.out.println("Welcome to the game!");
            System.out.print("List of your characters: ");
            ArrayList<String> chars = getCharsName(log,st);
            u.getChars(chars,st);
            for (String s : chars)
                System.out.print(s+"("+u.getCharLvl(s)+" lvl)"+",");
            System.out.println();
            System.out.println("Choose exist character or create new. (create/name of char/delete)");
            System.out.println("For quit write EXIT");
            Scanner sc = new Scanner(System.in);
            String in = sc.nextLine();
            if (in.equals("create"))
                createChar(log,st,sc);
            if (in.equals("delete"))
                deleteChar(log,chars,sc,st);
            if (in.equals("EXIT"))
                break;
            if (chars.contains(in))
                launchGame(in,st,sc);
        }
    }

    public static @NotNull ArrayList<String> getCharsName(String log, @NotNull Statement st) throws SQLException {
        String ids = ""; ArrayList<String> lst = new ArrayList<String>();
        ArrayList<String> res = new ArrayList<String>();
        ResultSet rs = st.executeQuery("SELECT charsid FROM users WHERE name = '"+log+"'");
        while (rs.next())
            ids = rs.getString("charsid");
        lst = Utils.Split(ids,',');
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
            ArrayList<Integer> ids = new ArrayList<Integer>();int id = 0;
            ResultSet rs = st.executeQuery("SELECT id FROM characters");
            while (rs.next())
                ids.add(rs.getInt("id"));
            if (ids.size() > 0)
                id = ids.get(ids.size()-1) + 1;
            st.executeUpdate("INSERT INTO characters(id,name,class,lvl,exp,attack,defense,hp,equip,inventory,position) " +
                    "VALUES("+id+",'"+name+"','"+cls+"',"+1+","+1000+","+5+","+3+","+100+",'','','');");
            rs = st.executeQuery("SELECT charsid FROM users WHERE name = '"+log+"'");String i = "";
            while (rs.next())
                i = rs.getString("charsid");
            st.executeUpdate("UPDATE users SET charsid = '"+i+id+",' WHERE name = '"+log+"'");
            break;
        }
    }

    public static String checkCharName(@NotNull Statement st, @NotNull Scanner sc) throws SQLException {
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

    public static @NotNull String checkCharClass(@NotNull Scanner sc)
    {
        String s = sc.nextLine();
        if (!s.equals("mage") && !s.equals("warrior")) {
            System.out.println("Wrong type of class! Try again (mage or warrior)");
            s = checkCharClass(sc);
        }
        return s;
    }

    public static void deleteChar(String log, @NotNull ArrayList<String> lst, @NotNull Scanner sc, Statement st)
            throws SQLException {
        System.out.print("Write nik name of character for delete: ");
        String nik = sc.nextLine();
        if (!lst.contains(nik))
            System.out.println("You haven`t character with this name!");
        else
        {
            int id = 0;
            ResultSet rs = st.executeQuery("SELECT id FROM characters WHERE name = '"+nik+"'");
            while (rs.next())
                id = rs.getInt("id");
            String ids = "";
            rs = st.executeQuery("SELECT * FROM users WHERE name = '"+log+"'");
            while (rs.next())
                ids = rs.getString("charsid");
            String s = id +"";
            String str = Utils.eraseSubString(ids, s);
            st.executeUpdate("UPDATE users SET charsid = '"+str+"' WHERE name = '"+log+"'");
            st.executeUpdate("DELETE FROM characters WHERE name = '"+nik+"'");
            System.out.println("Character " + nik +" success deleted.");
        }
    }

    public static void launchGame(String nik,Statement st,Scanner sc) throws SQLException
    {

    }

    public static void printMap(int x,int y, int lvl)
    {
        int size = (lvl-1)*5+10;
        for (int i = 0;i<size/2;i++)
        {
            for (int j = 0;j<size;j++)
            {
                if (i == 0 || i == size/2 -1)
                    System.out.print("_");
                else
                {
                    if (j == 0 || j == size -1)
                        System.out.print("|");
                    else if (i == y && j == x)
                        System.out.print("&");
                    else
                        System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
