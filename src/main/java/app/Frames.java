package app;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import classes.*;
import javax.swing.*;

public class Frames extends JFrame {
    private Auth w;
    private Controler c = Singleton.getInstance();
    public Frames() {
        if (c.getState() == 0)
        {
            w = new Auth(this, true);
            w.setVisible(true);
            w.setUndecorated(true);
        }
        if (c.getState() == 1)
        {
            
        }  
    }
    class Auth extends JDialog 
    {   
        private final JLabel logLbl = new JLabel("LOGIN:");
        private final JLabel passLbl = new JLabel("PASSWORD:");
        private final JTextField logTf = new JTextField(15);
        private final JPasswordField passPf = new JPasswordField();
        private final JButton buttonCreate = new JButton("Create");
        private final JButton buttonOk = new JButton("Ok");
        private final JButton buttonCancel = new JButton("Cancel");
        private final JLabel statusLbl = new JLabel(" ");
        public Auth(){
            this(null, true);
        }
        public Auth(final JFrame parent, boolean modal)
        {
            super(parent, modal);
            JPanel p3 = new JPanel(new GridLayout(2, 1));p3.add(logLbl);p3.add(passLbl);
            JPanel p4 = new JPanel(new GridLayout(2, 1));p4.add(logTf);p4.add(passPf);
            JPanel p1 = new JPanel();p1.add(p3);p1.add(p4);
            JPanel p2 = new JPanel();p2.add(buttonCreate);p2.add(buttonOk);p2.add(buttonCancel);
            JPanel p5 = new JPanel(new BorderLayout());p5.add(p2, BorderLayout.CENTER);p5.add(statusLbl, BorderLayout.NORTH);
            statusLbl.setForeground(Color.RED);statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
            setLayout(new BorderLayout());add(p1, BorderLayout.CENTER);add(p5, BorderLayout.SOUTH);
            pack();setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            buttonOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Connection con;Statement st;
                    try {
                        con = Program.connectToDb();st = con.createStatement();
                        if (Program.checkLogin(st, logTf.getText(), String.valueOf(passPf.getPassword())) == 0)
                        {
                            c.setUser(new User(logTf.getText(),String.valueOf(passPf.getPassword())));
                            ChooseCharacter cc = new ChooseCharacter(parent, true, logTf.getText());
                            setVisible(false);
                            cc.setUndecorated(true);
                            cc.setVisible(true);
                        }
                        else
                             statusLbl.setText("Invalid login or password!");
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        System.exit(-1);
                    }
                }
            });
            buttonCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    parent.dispose();
                    // try {
                    //     Program.console();
                    // } catch (Exception ex) {
                    //     ex.printStackTrace();
                    // }            
                }
            });
            buttonCreate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Connection con;Statement st;
                    try {
                        con = Program.connectToDb();st = con.createStatement();
                        if (Program.checkLogin(st,logTf.getText(), String.valueOf(passPf.getPassword())) != 1)
                            statusLbl.setText("Account with this name exist! Try again");
                        else
                        {
                            int Id = Program.getLastId(st) + 1;
                            st.executeUpdate("INSERT INTO users(id,name,password,charsID) VALUES ("+Id+",'"
                                    +logTf.getText()+"','"+String.valueOf(passPf.getPassword())+"',"+"''"+");");
                            statusLbl.setText("Account created! Start game");
                            setVisible(false);parent.dispose();
                            Program.startConsoleGame(logTf.getText(), st);
                            st.close();
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        System.exit(-1);
                    }
                }
            });
        }
    }
    class ChooseCharacter extends JDialog
    {
        private final JButton buttonCreate = new JButton("Create Character");
        private final JButton buttonCancel = new JButton("Cancel");
        private JPanel p = new JPanel();
        private ArrayList<String> chars;
        private ChooseCharacter(){
            this(null, true, "");
        }
        public ChooseCharacter(final JFrame parent, boolean modal, String login)
        {
            super(parent, modal);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            buttonCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    parent.dispose();
                }
            });
            try {
                Connection con = Program.connectToDb();
                Statement st = con.createStatement();
                chars = Program.getCharsName(login, st);
                if (chars.size() > 0)
                {
                    for (String i:chars)
                    {
                        JButton btn = new JButton(i);
                        btn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                setVisible(false);
                                parent.dispose();
                            }
                        });
                        p.add(btn);
                    }
                }
                p.add(buttonCreate);p.add(buttonCancel);
                setLayout(new BorderLayout());add(p, BorderLayout.CENTER);
                pack();setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class startGame extends JDialog
    {

    }
}
