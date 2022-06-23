package classes;

import org.jetbrains.annotations.NotNull;

public class Position {
    private int x;
    private int y;
    public boolean contains(@NotNull Object obj)
    {
        Position p = (Position) obj;
        return x == p.x && y == p.y;
    }
    public int getX() {return x;}
    public int getY() {return y;}
    public void setX(int X) {x = X;}
    public void setY(int Y) {y = Y;}
    public void printPosition()
    {System.out.print("Position: ");System.out.print(x);System.out.print(' ');System.out.println(y);}
}
