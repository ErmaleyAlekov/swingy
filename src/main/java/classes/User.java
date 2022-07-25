package classes;

import app.Program;
import org.jetbrains.annotations.NotNull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User
{
    private String name;
    private String pass;
    private ArrayList<Character> chars = new ArrayList<>();
    public User(String Name,String Pass) {name = Name;pass = Pass;}
    public User(@NotNull User obj) {name = obj.name;pass = obj.pass;chars = obj.chars;}
    public String getName() {return name;}
    public String getPass() {return pass;}
    public void setName(String Name) {name = Name;}
    public void setPass(String Pass) {pass = Pass;}
    public void getChars(@NotNull ArrayList<String> lst, Statement st) throws SQLException, ClassNotFoundException {
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
                Character c = new Character(i,cls);Equip e = new Equip();ArrayList<String> eq;
                c.setLvl(lvl);c.setExp(exp);c.setHp(hp);c.setAttack(attack);c.setDef(defense);
                if (equip != null)
                {
                    Connection con = Program.connectToDb();
                    Statement st2 = con.createStatement();
                    eq = Utils.Split(equip,',');
                    e = e.getInstance(eq,st2);
                }
                if (inventory != null)
                {
                    Connection con = Program.connectToDb();
                    Statement st2 = con.createStatement();
                    eq = Utils.Split(inventory,',');
                    c.inventory = e.getItems(eq,st2);
                }
                c.setEquip(e);
                chars.add(c);
            }
        }
    }
    public int getCharLvl(String name)
    {
        for (Character c:chars)
            if (c.getName().equals(name))
                return c.getLvl();
        return 0;
    }

    public @NotNull ArrayList<Character> getChars() {return chars;}
}
