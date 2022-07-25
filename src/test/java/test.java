
import classes.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws IOException {
        ArrayList<p> lst = new ArrayList<>();
        p P = new p();p l = new p();
        P.x = 1;l.x = 1;
        P.y = 2;l.y = 2;
        lst.add(P);
        if (lst.equals(l))
            System.out.println("YEs");
        else
            System.out.println("No");
    }
    public static class p
    {
        public int x;
        public int y;
    }
}
