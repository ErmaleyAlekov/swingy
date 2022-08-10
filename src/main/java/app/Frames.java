package app;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Statement;

import javax.swing.*;

public class Frames extends JFrame {
    private Auth w;
    public Frames() {
        w = new Auth(this, true);
        w.setVisible(true);
    }
    class Auth extends JDialog {
        
        private final JLabel logLbl = new JLabel("LOGIN:");
        private final JLabel passLbl = new JLabel("PASSWORD:");
        private final JTextField logTf = new JTextField(15);
        private final JPasswordField passPf = new JPasswordField();
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
            JPanel p2 = new JPanel();p2.add(buttonOk);p2.add(buttonCancel);
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
                            parent.setVisible(true);
                            setVisible(false);
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
                    System.exit(0);
                }
            });
        }
    }
}
