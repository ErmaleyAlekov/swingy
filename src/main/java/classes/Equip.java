package classes;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Equip
{
    private Weapon weapon;
    private Armor armor;
    private Helmet helmet;
    private Glove glove;
    private Boot boot;
    public Equip() {}
    public Equip(Weapon w, Armor a, Helmet h, Glove g, Boot b) {weapon = w;armor = a;helmet = h;glove = g;boot = b;}
    public Weapon getWeapon() {return weapon;}
    public Armor getArmor() {return armor;}
    public Helmet getHelmet() {return  helmet;}
    public Glove getGlove() {return glove;}
    public Boot getBoot() {return boot;}
    public void setWeapon(Weapon w) {weapon = w;}
    public void setArmor(Armor a) {armor = a;}
    public void setHelmet(Helmet h) {helmet = h;}
    public void setGlove(Glove g) {glove = g;}
    public void setBoot(Boot b) {boot = b;}
    public @NotNull Equip getInstance(@NotNull ArrayList<String> lst, Statement st) throws SQLException
    {
        ArrayList<Item> lst2 = getItems(lst,st);Equip obj = new Equip();
        for (Item i:lst2)
        {
            if (i instanceof Weapon)
                obj.setWeapon((Weapon)i);
            if (i instanceof Armor)
                obj.setArmor((Armor) i);
            if (i instanceof Helmet)
                obj.setHelmet((Helmet) i);
            if (i instanceof Boot)
                obj.setBoot((Boot) i);
            if (i instanceof Glove)
                obj.setGlove((Glove) i);
        }
        return obj;
    }

    public @NotNull ArrayList<Item> getItems(@NotNull ArrayList<String> lst, Statement st) throws SQLException
    {
        ArrayList<Item> lst2 = new ArrayList<>();
        for (String s : lst)
        {
            String type = "";int attack = 0;int def = 0;
            ResultSet rs = st.executeQuery("SELECT * FROM items WHERE name = '" + s+"'");
            while(rs.next())
            {
                type = rs.getString("type");
                attack = rs.getInt("attack");
                def = rs.getInt("defense");
                Item it = getItem(s,type,attack,def);
                lst2.add(it);
            }
        }
        return lst2;
    }
    public @Nullable Item getItem(String name, @NotNull String type, int attack, int def)
    {
        if (type.equals("weapon"))
            return new Weapon(name,attack);
        else if (type.equals("armor"))
            return new Armor(name,def);
        else if (type.equals("helmet"))
            return new Helmet(name,def);
        else if (type.equals("boot"))
            return new Boot(name,def);
        else if (type.equals("glove"))
            return new Glove(name,def);
        else
            return null;
    }
}
