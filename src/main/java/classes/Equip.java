package classes;

public class Equip
{
    private static Weapon weapon;
    private static Armor armor;
    private static Helmet helmet;
    private static Glove glove;
    private static Boot boot;
    public Equip(Weapon w, Armor a, Helmet h, Glove g, Boot b) {weapon = w;armor = a;helmet = h;glove = g;boot = b;}
    public static Weapon getWeapon() {return weapon;}
    public static Armor getArmor() {return armor;}
    public static Helmet getHelmet() {return  helmet;}
    public static Glove getGlove() {return glove;}
    public static Boot getBoot() {return boot;}
    public static void setWeapon(Weapon w) {weapon = w;}
    public static void setArmor(Armor a) {armor = a;}
    public static void setHelmet(Helmet h) {helmet = h;}
    public static void setGlove(Glove g) {glove = g;}
    public static void setBoot(Boot b) {boot = b;}
}
