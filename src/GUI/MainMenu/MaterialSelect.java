package GUI.MainMenu;

import GUI.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kyle on 7/20/2016.
 */
public class MaterialSelect extends JPanel implements ActionListener {

    private int material;
    private int subject;
    private JFrame frame;
    private Main main;
    private JList studyMat;
    private JLabel matMessage;
    private JButton back;
    private SubjectScreen subScreen;

    public MaterialSelect(JFrame frame, Main main, SubjectScreen subScreen, int subject){

        this.frame = frame;
        frame.setResizable(false);
        setLayout(null);
        setMinimumSize(new Dimension(400, 400));
        setPreferredSize(new Dimension(600, 600));
        this.main = main;
        this.subject = subject;
        this.subScreen = subScreen;
        material = subScreen.getMaterial();
        System.out.println(material);

        switch(material) {
            case 1:
                matMessage = new JLabel("Select a Focused Quiz to take: ");
                break;
            case 2:
                matMessage = new JLabel("Select a Leveled Exam to take: ");
                break;
            case 3:
                matMessage = new JLabel("Select a Comprehensive Exam to take: ");
                break;
            case 4:
                matMessage = new JLabel("Select a Section Exam to take: ");
                break;
            case 5:
                matMessage = new JLabel("Select a Flashcard Set to review: ");
                break;
        }
        matMessage.setBounds(100,25,400,25);
        matMessage.setFont(MainMenu.font);
        add(matMessage);

        String[] titles = {"dig","dug","dag","deg","dog"};
        studyMat = new JList(titles);
        studyMat.setBounds(75,75,450,450);
        add(studyMat);

        back = new JButton("Back");
        back.addActionListener(this);
        back.setBounds(10,10,75,50);
        add(back);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back){
            subScreen = new SubjectScreen(material, frame, main);
            frame.remove(this);
            frame.add(subScreen);
            frame.revalidate();
            frame.repaint();
        }
    }
}
