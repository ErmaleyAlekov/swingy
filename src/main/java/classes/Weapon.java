package classes;

public class Weapon implements Item
{
    private String name = "No name";
    private int damage;

    private int defense;
    public Weapon() {}
    public Weapon(String Name, int d) {name = Name;damage=d;}
    public String getName() {return name;}
    public int getDamage() {return damage;}
    public void setName(String n) {name = n;}
    public void setAttack(int d) {damage = d;}
    public void setDef(int d) {defense = d;}

}
