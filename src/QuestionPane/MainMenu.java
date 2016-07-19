package QuestionPane;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by Kyle on 7/19/2016.
 */
public class MainMenu extends JPanel implements ActionListener{

    private JFrame frame;
    private JButton[] buttons;
    private JLabel intro_message;
    private Font font;
    QuestionPane questionPane;
    public MainMenu(){
        try {
        // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            System.out.println(Arrays.toString(UIManager.getInstalledLookAndFeels()));
        }
        catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        try {
            DBReader.setConnection(DriverManager.getConnection("jdbc:sqlite:sample.db"));
        } catch (SQLException e) {
            e.printStackTrace();
        };
        questionPane = new QuestionPane(frame);
        font = new Font("Times New Roman", Font.PLAIN, 24);
        intro_message = new JLabel("<html>Welcome to the improved DemiQuiz!<br>" +
                "Choose a study material below.</html>", SwingConstants.CENTER);
        intro_message.setBounds(100,25,400,50);
        intro_message.setFont(font);
        add(intro_message);

        buttons = new JButton[6];
        buttons[0] = new JButton("Focused Quizzes");
        buttons[1] = new JButton("Leveled Exams");
        buttons[2] = new JButton("Comprehensive Exams");
        buttons[3] = new JButton("Competition Exams");
        buttons[4] = new JButton("Section Exams");
        buttons[5] = new JButton("Flashcards");
        int xLoc = 200;
        int yLoc = 100;
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setBounds(xLoc, yLoc, 200, 50);
            add(buttons[i]);
            yLoc += 75;
            buttons[i].addActionListener(this);
        }

        frame.setContentPane(this);
        frame.pack();
    }

    public static void main(String[] args){
        new MainMenu();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons[0]){
            frame.add(new QuestionPane(frame));
            frame.pack();
            frame.setLocationRelativeTo(null);
            if(!frame.isVisible())
                frame.setVisible(true);
        }
    }
}
