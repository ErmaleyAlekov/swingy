package classes;

import java.util.ArrayList;

public class Character
{
    private String name;
    private String Class;
    private int lvl = 1;
    private int exp = 1000;
    private int hp = 100;
    private int attack = 5;
    private int def = 3;
    private Equip equip;
    public ArrayList<Item> inventory = new ArrayList<Item>();
    public Character(String Name, String cls) {name = Name; Class = cls;}
    public String getName() {return name;}
    public String getCls() {return Class;}
    public int getLvl() {return lvl;}
    public int getExp()  {return exp;}
    public int getHp() {return hp;}
    public int getAttack() {return attack;}
    public int getDef() {return def;}
    public Equip getEquip() {return equip;}
    public void setName(String Name) {name = Name;}
    public void setCls(String Cls) {Class = Cls;}
    public void setLvl(int l) {lvl = l;}
    public void setExp(int Exp) {exp = Exp;}
    public void setHp(int Hp) {hp = Hp;}
    public void setAttack(int a) {attack = a;}
    public void setDef(int d) {def = d;}
    public void setEquip(Equip o) {equip = o;}
    public void lvlUp() {lvl++;hp+= 100;attack+=3;def+=1;}
    public void addExp(int Exp) {exp += Exp;checkExp();}
    public void equipItem(Item item)
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
    public void checkExp()
    {
        int e = (lvl+1)*1000+(int)Math.pow(lvl,2)*450;
        if (exp > e)
            lvlUp();
    }
}
