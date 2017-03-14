package GUI.MainMenu;

import GUI.Main;
import GUI.QuestionPane.QuestionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kyle on 7/19/2016.
 */
public class MainMenu extends JPanel implements ActionListener{

    private JFrame frame;
    private JButton[] buttons;
    private JLabel intro_message;
    protected static Font font;
    private Main main;
    private SubjectScreen subScreen;

    public MainMenu(JFrame frame, Main main){
        this.frame = frame;
        this.main = main;
        setLayout(null);
        frame.setResizable(false);
       /* popupMenu = new JPopupMenu("Buh?");
        JMenuItem[] items = new JMenuItem[6];
        items[0] = new JMenuItem("Art Quizzes");
        items[1] = new JMenuItem("Music Quizzes");
        items[2] = new JMenuItem("Social Science Quizzes");
        items[3] = new JMenuItem("Economic Quizzes");
        items[4] = new JMenuItem("Science Quizzes");
        items[5] = new JMenuItem("Literature Quizzes");
        for(int i = 0; i < items.length; i++){
            popupMenu.add(items[i]);
            if(i != items.length - 1) {
                popupMenu.addSeparator();
            }
        }
*/
        font = new Font("Times New Roman", Font.PLAIN, 24);
        intro_message = new JLabel("<html>Welcome to the improved DemiQuiz!<br>" +
                "Choose a study material below.</html>", SwingConstants.CENTER);
        intro_message.setBounds(100,25,400,60);
        intro_message.setFont(font);
        add(intro_message, Component.CENTER_ALIGNMENT);
        buttons = new JButton[5];
        buttons[0] = new JButton("Focused Quizzes");
        buttons[1] = new JButton("Leveled Exams");
        buttons[2] = new JButton("Comprehensive Exams");
        buttons[3] = new JButton("Section Exams");
        buttons[4] = new JButton("Flashcards");
        int xLoc = 200;
        int yLoc = 125;
        for(int i = 0; i < buttons.length; i++) {
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
            setSubScreen(1);
        }
        if(e.getSource() == buttons[1]){
            setSubScreen(2);
        }
        if(e.getSource() == buttons[2]){
            setSubScreen(3);
        }
        if(e.getSource() == buttons[3]){
            setSubScreen(4);
        }
        if(e.getSource() == buttons[4]){
            setSubScreen(5);
        }
    }

    public void setSubScreen(int material){
        subScreen = new SubjectScreen(material, frame, main);
        frame.remove(this);
        frame.add(subScreen);
        frame.revalidate();
        frame.repaint();
    }

}