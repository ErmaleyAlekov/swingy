package classes;

public class Helmet implements Item
{
    private String name;
    private int def;
    public Helmet(String Name, int d) {name = Name;def = d;}
    public String getName() {return name;}
    public int getDef() {return def;}
    public void setName(String n) {name = n;}
    public void setDef(int d) {def = d;}
}
