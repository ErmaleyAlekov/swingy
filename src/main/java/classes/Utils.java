package classes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Utils {
    public static @NotNull ArrayList<String> Split(@NotNull String str, char del)
    {
        char []buff = str.toCharArray();
        StringBuilder b = new StringBuilder();
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0;i< buff.length;i++)
        {
            if (buff[i] != del) {
                b.append(buff[i]);
                if (i == buff.length -1)
                    res.add(b.toString());
            }
            else {
                res.add(b.toString());
                b = new StringBuilder();
            }
        }
        return res;
    }
    public static @NotNull String eraseSubString(@NotNull String str,@NotNull String str2)
    {
        String res = str.replace(str2,"");int f =0;
        char []buff = res.toCharArray();StringBuilder b = new StringBuilder();
        for (int i = 0;i< buff.length;i++)
        {

            if (i != 0 && buff[i] == ',' && buff[i-1] == ',')
                f = 1;
            else if (i == 0 && buff[i] == ',')
                f = 1;
            else
                b.append(buff[i]);
        }
        return b.toString();
    }
    public static boolean moreOn(int a, int b, int on)
    {
        if (a > b)
            return (a-b) >= on;
        else if (a < b) {
            return (b - a) >= on;
        }
        else
            return false;
    }
}
