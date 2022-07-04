package classes;

import org.jetbrains.annotations.NotNull;

public class Enemy {
    public int hp;
    public int attack;
    public int defense;
    public void setEnemy(@NotNull Enemy obj)
    {
        hp = obj.hp;
        attack = obj.attack;
        defense = obj.defense;
    }
    public Character giveDamage(@NotNull Character ch)
    {
        int rand = 1 + (int)(Math.random() * 3);int crit = 1;
        if (rand == 3)
            crit = 2;
        ch.setHp(ch.getHp() - attack * crit);
        return ch;
    }
}
