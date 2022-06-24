package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import classes.Character;
import classes.Position;
import classes.User;
import classes.Utils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.*;
public class Program {
    static User u = null;
    public static void console() throws SQLException, ClassNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);
        String in = "";
        System.out.println("Hello! Have you got account? (yes/no)");
        in = sc.nextLine();
        Connection con = connectToDb();
        Statement st = con.createStatement();
        if (in.equals("yes")) {
            while (true)
            {
                System.out.print("LOGIN: ");
                String login = sc.nextLine();
                System.out.print("PASS: ");
                String pass = sc.nextLine();
                if (checkLogin(st, login, pass) == 0) {
                    u = new User(login,pass);
                    System.out.println("You are login in!");
                    startConsoleGame(login, st);
                    break;
                }
                else
                    System.out.println("Wrong data!");
            }
        } else if (in.equals("no"))
            createAccount(st);
        else
            console();
        sc.close();
    }

    public static void gui() {
    }

    public static void createAccount(Statement st) throws SQLException, IOException {
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
    public static int getLastId(@NotNull Statement st) throws SQLException
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

    public static void main(String []args)
    {
        try {
            if (args[0].equals("console")) {
                console();
            } else if (args[0].equals("gui")) {
                gui();
            } else
                throw new IllegalArgumentException();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void startConsoleGame(String log, Statement st) throws SQLException, IOException {
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
            if (chars.contains(in)) {
                for (Character ch : u.getChars()) {
                    if (ch.getName().equals(in)) {
                        launchGame(ch, st, sc);
                        break;
                    }
                }
            }
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
            st.executeUpdate("INSERT INTO characters(id,name,class,lvl,exp,attack,defense,hp,equip,inventory) " +
                    "VALUES("+id+",'"+name+"','"+cls+"',"+1+","+1000+","+5+","+3+","+100+",'','','');");
            rs = st.executeQuery("SELECT charsid FROM users WHERE name = '"+log+"'");String i = "";
            while (rs.next())
                i = rs.getString("charsid");
            st.executeUpdate("UPDATE users SET charsid = '"+i+id+",' WHERE name = '"+log+"'");
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

    public static void launchGame(@NotNull Character ch, Statement st, @NotNull Scanner sc)
            throws SQLException, IOException {
        int size = (ch.getLvl()-1)*5+10;char in = 0;
        ArrayList<Position> lst = getPositions(ch,size);
        while (true)
        {
            if (lst.get(0).getX() <= 0 || lst.get(0).getX() >= size -1
                    || lst.get(0).getY() <= 0 || lst.get(0).getY() >= size /2 -1)
            {
                System.out.println("You are lose!");
                break;
            }
            System.out.println("w - up, s - down, d - right, a - left, e - hit, q - exit");
            System.out.println("Your heals points: " + ch.getHp());
            printMap(lst,ch.getLvl());
            in = sc.next().charAt(0);
            if (in == 'w')
                lst.get(0).setY(lst.get(0).getY() -1);
            if (in == 's')
                lst.get(0).setY(lst.get(0).getY() +1);
            if (in == 'd')
                lst.get(0).setX(lst.get(0).getX() +1);
            if (in == 'a')
                lst.get(0).setX(lst.get(0).getX() -1);
            if (in == 'q') {
                break;
            }
            lst = enemyMove(lst);
        }
    }
    public static @NotNull ArrayList<Position> getPositions(@NotNull Character ch, int size)
    {
        ArrayList<Position> lst = new ArrayList<>();
        for (int i = 0;i<=ch.getLvl();i++)
        {
            Position p = new Position();
            if (i == 0)
            {
                p.setX(size/2);p.setY(size / 4);
                lst.add(p);
            }
            else
            {
                p.setX(1 + (int)(Math.random() * (size -2)));
                p.setY(1 + (int)(Math.random() * (size / 2 - 2)));
                lst.add(p);
            }
        }
        return lst;
    }

    public static ArrayList<Position> enemyMove(@NotNull ArrayList<Position> lst)
    {
        int x = lst.get(0).getX();int y = lst.get(0).getY();
        for (int i = 1;i< lst.size();i++)
        {
            if (Utils.moreOn(x,lst.get(i).getX(),2)
                    && Utils.moreOn(y,lst.get(i).getY(),2))
            {
                int rx = Math.max(x,lst.get(i).getX())-Math.min(x,lst.get(i).getX());
                int ry = Math.max(y,lst.get(i).getY())-Math.min(y,lst.get(i).getY());
                if (rx > ry)
                {
                    if (y > lst.get(i).getY())
                        lst.get(i).setY(lst.get(i).getY() +1);
                    else if (y < lst.get(i).getY())
                        lst.get(i).setY(lst.get(i).getY() -1);
                } else if (ry > rx) {
                    if (x > lst.get(i).getX())
                        lst.get(i).setX(lst.get(i).getX() +1);
                    else if (x < lst.get(i).getX())
                        lst.get(i).setX(lst.get(i).getX() -1);
                }
            }
            if (Utils.moreOn(x,lst.get(i).getX(),2)
                    && !Utils.moreOn(y,lst.get(i).getY(),2))
            {
                if (x > lst.get(i).getX())
                    lst.get(i).setX(lst.get(i).getX() +1);
                else if (x < lst.get(i).getX())
                    lst.get(i).setX(lst.get(i).getX() -1);
            }
            if (Utils.moreOn(y,lst.get(i).getY(),2)
                    && !Utils.moreOn(x,lst.get(i).getX(),2))
            {
                if (y > lst.get(i).getY())
                    lst.get(i).setY(lst.get(i).getY() +1);
                else if (y < lst.get(i).getY())
                    lst.get(i).setY(lst.get(i).getY() -1);
            }
        }
        lst = checkPositions(lst);
        return lst;
    }

    @Contract("_ -> param1")
    public static ArrayList<Position> checkPositions(@NotNull ArrayList<Position> lst)
    {
        for (int i =0;i< lst.size();i++)
        {
            for (int j = 0;j< lst.size();j++)
            {
                if (lst.get(i).equals(lst.get(j)) && i != j)
                    lst.get(j).setX(lst.get(j).getX() + 1);
            }
        }
        return lst;
    }
    public static void printMap(ArrayList<Position> lst, int lvl)
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
                    Position p = new Position();
                    p.setX(j);p.setY(i);
                    if (j == 0 || j == size -1)
                        System.out.print("|");
                    else if (p.equals(lst.get(0)))
                        System.out.print("&");
                    else if (p.contains(lst))
                        System.out.print("x");
                    else
                        System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
