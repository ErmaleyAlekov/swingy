import classes.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        String str = eraseSubString("11,","11");
        System.out.println(str);
    }

    public static @NotNull String eraseSubString(@NotNull String str, @NotNull String str2)
    {
        String res = str.replace(str2,"");
        System.out.println(res);int f =0;
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
}
