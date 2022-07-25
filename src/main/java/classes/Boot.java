package classes;

public class Boot implements Item
{
    private String name = "No name";
    private int def;
    private int attack;
    public Boot() {}
    public Boot(String Name, int d) {name = Name;def = d;}
    public String getName() {return name;}
    public int getDef() {return def;}
    public void setName(String n) {name = n;}
    public void setDef(int d) {def = d;}

    public void setAttack(int a) {attack = a;}
    public int getAttack() {return attack;}
}
