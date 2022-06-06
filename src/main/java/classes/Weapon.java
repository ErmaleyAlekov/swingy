package classes;

public class Weapon implements Item
{
    private static String name;
    private static int damage;
    public Weapon(String Name, int d) {name = Name;damage=d;}
    public static String getName() {return name;}
    public static int getDamage() {return damage;}
    public static void setName(String n) {name = n;}
    public static void setDamage(int d) {damage = d;}
}
