package classes;

import java.util.ArrayList;

public class User
{
    private static String name;
    private static String pass;
    private ArrayList<Character> chars = new ArrayList<Character>();
    public User(String Name,String Pass) {name = Name;pass = Pass;}
    public User(User obj) {name = obj.name;pass = obj.pass;chars = obj.chars;}
    public static String getName() {return name;}
    public static String getPass() {return pass;}
    public static void setName(String Name) {name = Name;}
    public static void setPass(String Pass) {pass = Pass;}
}
