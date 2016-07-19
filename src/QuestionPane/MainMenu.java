package QuestionPane;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kyle on 7/19/2016.
 */
public class MainMenu extends JPanel implements ActionListener{

    private JFrame frame;
    private JButton[] buttons;
    private JLabel intro_message;
    private Font font;
    private Main main;
    public MainMenu(JFrame frame, Main main){
        this.frame = frame;
        this.main = main;
        setLayout(null);
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
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons[0]){
            main.changeQuestionSet(null);
        }
    }
}
