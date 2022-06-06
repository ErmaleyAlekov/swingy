package classes;

public class Glove implements Item
{
    private static String name;
    private static int def;
    public Glove(String Name, int d) {name = Name;def = d;}
    public static String getName() {return name;}
    public static int getDef() {return def;}
    public static void setName(String n) {name = n;}
    public static void setDef(int d) {def = d;}
}
