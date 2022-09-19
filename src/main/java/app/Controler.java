package app;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import classes.*;
public class Controler {
    private int state;
    private User u;
    private String currentMode;
    public Controler() {state = 0;currentMode = "";u = new User();}
    public void setState(int newState) {state = newState;}
    public String getMode() {return currentMode;}
    public void setMode(String newMode) {currentMode = newMode;}
    public int getState() {return state;}
    public void inState() {state++;}
    public void deState() {state--;}
    public User getUser() {return u;}
    public void setUser(User obj) {u = obj;}
    public void nextState() {}
    public void changeMode() {
        if (currentMode.equals("gui"))
        {
            setMode("console");
            if (state == 0)
            {
                try {
                    Program.console();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (state == 1)
            {
                try {
                    Connection con = Program.connectToDb();
                    Statement st = con.createStatement();
                    Program.startConsoleGame(u.getName(), st);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (currentMode.equals("console"))
        {
            setMode("gui");
            if (state == 0)
            {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFrame frame = new Frames();
                        frame.getContentPane().setBackground(Color.BLACK);
                        frame.setTitle("Logged In");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLocationRelativeTo(null);
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    }
                });
                return;
            }
        }
    }
}
