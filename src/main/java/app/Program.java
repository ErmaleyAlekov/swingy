package app;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import classes.*;
import classes.Character;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Program {
    // static User u = new User();
    static Controler c = Singleton.getInstance();
    public static void console() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello! Have you got account? (yes/no/changeMode)");
        String in = "";
        in = sc.nextLine();
        Connection con = connectToDb();
        Statement st = con.createStatement();
        if (in.equals("yes")) {
            while (true)
            {
                System.out.println("If you want change mode write: CHANGE");
                System.out.print("LOGIN: ");
                String login = sc.nextLine();
                if (login.equals("CHANGE"))
                {
                    c.changeMode();break;
                }
                System.out.print("PASS: ");
                String pass = sc.nextLine();
                if (pass.equals("CHANGE"))
                {
                    c.changeMode();break;
                }
                if (checkLogin(st, login, pass) == 0) {
                    c.setUser(new User(login,pass));
                    System.out.println("You are login in!");
                    startConsoleGame(login, st);
                    c.setState(0);
                    continue;
                }
                else
                    System.out.println("Wrong data!");
            }
        } 
        else if (in.equals("no"))
        {
            createAccount(st);
        }
        else if (in.equals("changeMode"))
        {
            sc.close();
            c.changeMode();
        }
        else
            console();
        sc.close();
    }

    public static void gui() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new Frames();
                frame.getContentPane().setBackground(Color.BLACK);
                frame.setTitle("Logged In");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
    }

    public static void createAccount(Statement st) throws Exception {
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
    public static Connection connectToDb() throws SQLException {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        cfg.setUsername("postgres");cfg.setPassword("123");
        HikariDataSource ds = new HikariDataSource(cfg);
        return ds.getConnection();
    }

    public static int checkLogin(@NotNull Statement st, String log, String pass) throws SQLException {
        ArrayList<String> l = new ArrayList<>();
        ArrayList<String> p = new ArrayList<>();
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
                c.setMode("console");console();
            } else if (args[0].equals("gui")) {
                c.setMode("gui");gui();
            } else
                throw new IllegalArgumentException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startConsoleGame(String log, Statement st) throws Exception {
        while (true)
        {
            c.setState(1);
            System.out.println("Hi, "+log+"!");
            System.out.println("Welcome to the game!");
            System.out.print("List of your characters: ");
            ArrayList<String> chars = getCharsName(log,st);
            c.getUser().getChars(chars,st);
            for (String s : chars)
                System.out.print(s+"("+c.getUser().getCharLvl(s)+" lvl)"+",");
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
                for (Character ch : c.getUser().getChars()) {
                    if (ch.getName().equals(in)) {
                        launchGame(ch, st, sc);
                        break;
                    }
                }
            }
        }
    }

    public static @NotNull ArrayList<String> getCharsName(String log, @NotNull Statement st) throws SQLException {
        String ids = ""; ArrayList<String> lst;
        ArrayList<String> res = new ArrayList<>();
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
            ArrayList<Integer> ids = new ArrayList<>();int id = 0;
            ResultSet rs = st.executeQuery("SELECT id FROM characters");
            while (rs.next())
                ids.add(rs.getInt("id"));
            if (ids.size() > 0)
                id = ids.get(ids.size()-1) + 1;
            st.executeUpdate("INSERT INTO characters(id,name,class,lvl,exp,attack,defense,hp,equip,inventory) " +
                    "VALUES("+id+",'"+name+"','"+cls+"',"+1+","+1000+","+5+","+3+","+100+",'','');");
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
            throws Exception {
        int size = (ch.getLvl()-1)*5+10;char in = 0;
        ArrayList<Position> lst = getPositions(ch,size);
        ArrayList<Enemy> lst2 = getEnemys(ch);
        while (true)
        {
            if (lst.get(0).getX() <= 0 || lst.get(0).getX() >= size -1
                    || lst.get(0).getY() <= 0 || lst.get(0).getY() >= size /2 -1
                    || ch.getHp() <= 0 || in == 'q')
            {
                System.out.println("You are lose!");
                c.deState();
                break;
            }
            System.out.println("w - up, s - down, d - right, a - left,i - inventory, e - hit, q - exit");
            System.out.println("Your heals points: " + ch.getHp());
            for (Enemy e : lst2)
                System.out.println("Enemy heals point: " + e.hp);
            printMap(lst,ch.getLvl());in = sc.next().charAt(0);lst = charMove(lst,in);
            if (in == 'i') {
                ch = openInventory(ch);
                in = sc.next().charAt(0);
                lst = charMove(lst,in);
            }
            if (in == 'e') {
                lst2 = makeDamageToAll(lst,lst2,ch);
                ArrayList<Integer> del = checkEnemysHp(lst2);
                for (Integer i : del) {
                    lst2.remove(lst2.get(i));
                    lst.remove(i + 1);
                    ch = getDrop(st, ch);
                }
            }
            if (lst2.isEmpty())
            {
                System.out.println("You are win!");
                ch.addExp((int)(ch.getExp() * 1.5));
                ch.updateCharInBase(st);
                launchGame(ch,st,sc);
                break;
            }
            ch = getDamage(lst,lst2,ch);
            lst = enemyMove(lst);
        }
    }

    public static Character openInventory(Character ch)
    {
        System.out.println("Your items: " + ch.getItemNames(ch.inventory));
        System.out.println("Your equip items: "+ ch.getEquip().getEquipItemsNames());
        System.out.println("If you want equip item write his name.");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        for (Item i : ch.inventory)
        {
            if (i.getName().equals(in)) {
                ch.equipItem(i);
            }
        }
        return ch;
    }
    public static ArrayList<Enemy> makeDamageToAll(ArrayList<Position> lst, ArrayList<Enemy> lst2, Character ch)
    {
        for (int i = 1; i < lst.size(); i++) {
            if (!Utils.moreOn(lst.get(0).getX(), lst.get(i).getX(), 2)
                    && !Utils.moreOn(lst.get(0).getY(), lst.get(i).getY(), 2))
                lst2.get(i - 1).setEnemy(makeDamage(lst2.get(i - 1), ch));
        }
        return lst2;
    }
    public static Character getDamage(ArrayList<Position> lst, ArrayList<Enemy> lst2, Character ch)
    {
        for (int i = 0;i< lst2.size();i++) {
            if (!Utils.moreOn(lst.get(0).getX(), lst.get(i + 1).getX(), 2)
                    && !Utils.moreOn(lst.get(0).getY(), lst.get(i + 1).getY(), 2))
                ch = lst2.get(i).giveDamage(ch);
        }
        return ch;
    }
    public static ArrayList<Position> charMove(ArrayList<Position> lst, char in)
    {
        if (in == 'w')
            lst.get(0).setY(lst.get(0).getY() -1);
        if (in == 's')
            lst.get(0).setY(lst.get(0).getY() +1);
        if (in == 'd')
            lst.get(0).setX(lst.get(0).getX() +1);
        if (in == 'a')
            lst.get(0).setX(lst.get(0).getX() -1);
        return lst;
    }
    public static Character getDrop(@NotNull Statement st,@NotNull Character ch) throws SQLException {
        ArrayList<Item> items = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT * FROM items");
        while (rs.next())
        {
            String type = rs.getString("type");
            Item it = getType(type);
            if (it != null) {
                it.setName(rs.getString("name"));
                it.setAttack(rs.getInt("attack"));
                it.setDef(rs.getInt("defense"));
                items.add(it);
            }
        }
        int random = (int)(Math.random() * (items.size() -1));
        System.out.println("You are get: " + items.get(random).getName());
        ch.inventory.add(items.get(random));
        return ch;
    }

    public static @Nullable Item getType(@NotNull String type)
    {
        if (type.equals("armor"))
            return new Armor();
        if (type.equals("boot"))
            return new Boot();
        if (type.equals("glove"))
            return new Glove();
        if (type.equals("helmet"))
            return new Helmet();
        if (type.equals("weapon"))
            return new Weapon();
        return null;
    }
    public static Enemy makeDamage(Enemy obj, Character ch)
    {
        int rand = 1 + (int)(Math.random() * 3);int crit = 1;
        if (rand == 3)
            crit = 2;
        obj.hp = obj.hp - (ch.getAttack()*crit - obj.defense);
        return obj;
    }

    public static ArrayList<Integer> checkEnemysHp(ArrayList<Enemy> lst)
    {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i=0;i<lst.size();i++)
        {
            if (lst.get(i).hp <= 0)
                res.add(i);
        }
        return res;
    }

    @Contract(pure = true)
    public static @NotNull ArrayList<Enemy> getEnemys(@NotNull Character ch)
    {
        ArrayList<Enemy> lst = new ArrayList<>();
        for (int i = 0;i<ch.getLvl();i++)
        {
            Enemy e = new Enemy();
            e.hp = ch.getLvl() * 100;
            e.attack = ch.getLvl() * 2;
            e.defense = ch.getLvl();
            lst.add(e);
        }
        return lst;
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
