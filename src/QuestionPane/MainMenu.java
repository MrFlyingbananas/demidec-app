package QuestionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Kyle on 7/19/2016.
 */
public class MainMenu extends JPanel implements ActionListener, MouseListener{

    private JFrame frame;
    private JButton[] buttons;
    private JLabel intro_message;
    private Font font;
    private Main main;
    private JPopupMenu popupMenu;

    public MainMenu(JFrame frame, Main main){
        this.frame = frame;
        this.main = main;
        addMouseListener(this);
        setLayout(null);

        popupMenu = new JPopupMenu("Buh?");
        JMenuItem[] items = new JMenuItem[6];
        items[0] = new JMenuItem("Art Quizzes");
        items[0].addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                main.changeQuestionSet(DBReader.getQuestionListBySubject(DBReader.Subject.Art, DBReader.ListOrder.Random));
            }
        });
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

        font = new Font("Times New Roman", Font.PLAIN, 24);
        intro_message = new JLabel("<html>Welcome to the improved DemiQuiz!<br>" +
                "Choose a study material below.</html>", SwingConstants.CENTER);
        intro_message.setBounds(100,25,400,50);
        intro_message.setFont(font);
        add(intro_message, Component.CENTER_ALIGNMENT);
        buttons = new JButton[6];
        buttons[0] = new JButton("Focused Quizzes");
        buttons[1] = new JButton("Leveled Exams");
        buttons[2] = new JButton("Comprehensive Exams");
        buttons[3] = new JButton("Competition Exams");
        buttons[4] = new JButton("Section Exams");
        buttons[5] = new JButton("Flashcards");
        int xLoc = 200;
        int yLoc = 100;
        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setBounds(xLoc, yLoc, 200, 50);
            add(buttons[i]);
            yLoc += 75;
            buttons[i].addActionListener(this);
        }
        buttons[0].setComponentPopupMenu(popupMenu);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons[0]){
            popupMenu.setVisible(true);
            popupMenu.setLocation(frame.getX() + buttons[0].getX() + (frame.getWidth()/20),
                    + frame.getY() + buttons[0].getY() + (frame.getHeight()/7));
            popupMenu.updateUI();
            //main.changeQuestionSet(null);
        }
        if(e.getSource() != buttons[0]){
            popupMenu.setVisible(false);
            popupMenu.updateUI();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Buh?");
        if(!buttons[0].contains(e.getX(), e.getY())){
            popupMenu.setVisible(false);
            popupMenu.updateUI();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}