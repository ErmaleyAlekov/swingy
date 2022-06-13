package classes;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User
{
    private static String name;
    private static String pass;
    private static ArrayList<Character> chars = new ArrayList<Character>();
    public User(String Name,String Pass) {name = Name;pass = Pass;}
    public User(@NotNull User obj) {name = obj.name;pass = obj.pass;chars = obj.chars;}
    public static String getName() {return name;}
    public static String getPass() {return pass;}
    public static void setName(String Name) {name = Name;}
    public static void setPass(String Pass) {pass = Pass;}
    public static void getChars(@NotNull ArrayList<String> lst, Statement st) throws SQLException
    {
        for (String i:lst)
        {
            ResultSet rs = st.executeQuery("SELECT * FROM characters WHERE name = '"+i+"'");
            while(rs.next())
            {
                String cls = rs.getString("class");
                int lvl = rs.getInt("lvl");
                int exp = rs.getInt("exp");
                int hp = rs.getInt("hp");
                int attack = rs.getInt("attack");
                int defense = rs.getInt("defense");
                String equip = rs.getString("equip");
                String inventory = rs.getString("inventory");
                Character c = new Character(i,cls);Equip e = null;ArrayList<String> eq;
                c.setLvl(lvl);c.setExp(exp);c.setHp(hp);c.setAttack(attack);c.setDef(defense);
                if (equip != null)
                {
                    eq = Utils.Split(equip,',');
                    e = Equip.getInstance(eq,st);
                }
                if (inventory != null)
                {
                    eq = Utils.Split(inventory,',');
                    c.inventory = Equip.getItems(eq,st);
                }
                c.setEquip(e);
                chars.add(c);
            }
        }
    }
    public static int getCharLvl(String name)
    {
        for (Character c:chars)
            if (c.getName() == name)
                return c.getLvl();
        return 0;
    }
}
