package classes;

import java.util.ArrayList;

public class Character
{
    private static String name;
    private static String Class;
    private static int lvl = 1;
    private static int exp = 1000;
    private static int hp = 100;
    private static int attack = 5;
    private static int def = 3;
    private static Equip equip;
    public static ArrayList<Item> inventory = new ArrayList<Item>();
    public Character(String Name, String cls) {name = Name; Class = cls;}
    public static String getName() {return name;}
    public static String getCls() {return Class;}
    public static int getLvl() {return lvl;}
    public static int getExp()  {return exp;}
    public static int getHp() {return hp;}
    public static int getAttack() {return attack;}
    public static int getDef() {return def;}
    public static Equip getEquip() {return equip;}
    public static void setName(String Name) {name = Name;}
    public static void setCls(String Cls) {Class = Cls;}
    public static void setLvl(int l) {lvl = l;}
    public static void setExp(int Exp) {exp = Exp;}
    public static void setHp(int Hp) {hp = Hp;}
    public static void setAttack(int a) {attack = a;}
    public static void setDef(int d) {def = d;}
    public static void setEquip(Equip o) {equip = o;}
    public static void lvlUp() {lvl++;hp+= 100;attack+=3;def+=1;}
    public static void addExp(int Exp) {exp += Exp;checkExp();}
    public static void equipItem(Item item)
    {
        if (item instanceof Weapon) {
            if (equip.getWeapon() != null)
                attack -= equip.getWeapon().getDamage();
            equip.setWeapon((Weapon) item);
            attack += equip.getWeapon().getDamage();
        }
        if (item instanceof Armor) {
            if (equip.getArmor() != null)
                def -= equip.getArmor().getDef();
            equip.setArmor((Armor) item);
            def += equip.getArmor().getDef();
        }
        if (item instanceof Helmet) {
            if (equip.getHelmet() != null)
                def -= equip.getHelmet().getDef();
            equip.setHelmet((Helmet) item);
            def += equip.getHelmet().getDef();
        }
        if (item instanceof Glove) {
            if (equip.getGlove() != null)
                def -= equip.getGlove().getDef();
            equip.setGlove((Glove) item);
            def += equip.getGlove().getDef();
        }
        if (item instanceof Boot) {
            if (equip.getBoot() != null)
                def -= equip.getBoot().getDef();
            equip.setBoot((Boot) item);
            def += equip.getBoot().getDef();
        }
    }
    public static void checkExp()
    {
        int e = (lvl+1)*1000+(int)Math.pow(lvl,2)*450;
        if (exp > e)
            lvlUp();
    }
}
