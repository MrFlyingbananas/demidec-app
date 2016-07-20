package QuestionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kyle on 7/20/2016.
 */
public class SubjectScreen extends JPanel implements ActionListener {

    private JFrame frame;
    private JLabel subjectMessage;
    private JButton[] subjectButtons;
    private JButton back;
    private Font font;
    private int material;
    private Main main;
    private MainMenu menu;
    private MaterialSelect matSelect;

    public SubjectScreen(int material, JFrame frame, Main main){

        this.frame = frame;
        this.main = main;
        this.material = material;
        frame.setResizable(false);
        setLayout(null);
        setMinimumSize(new Dimension(400, 400));
        setPreferredSize(new Dimension(600, 600));


        font = new Font("Times New Roman", Font.PLAIN, 24);
        subjectMessage =  new JLabel("Select a subject to study: ");
        subjectMessage.setFont(font);
        subjectButtons = new JButton[6];
        subjectButtons[0] = new JButton("Art");
        subjectButtons[1] = new JButton("Music");
        subjectButtons[2] = new JButton("Economics");
        subjectButtons[3] = new JButton("Social Science");
        subjectButtons[4] = new JButton("Science");
        subjectButtons[5] = new JButton("Literature");
        back = new JButton("Back");
        back.addActionListener(this);

        subjectMessage.setBounds(185,25,400,50);
        add(subjectMessage);
        int xLoc = 200;
        int yLoc = 85;
        for(int i = 0; i < subjectButtons.length; i++){
            subjectButtons[i].setBounds(xLoc, yLoc, 200, 50);
            add(subjectButtons[i]);
            yLoc += 75;
            subjectButtons[i].addActionListener(this);
        }
        back.setBounds(10,10,75,50);
        add(back);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back){
            menu = new MainMenu(frame, main);
            frame.remove(this);
            frame.add(menu);
            frame.revalidate();
            frame.repaint();
        }
        if(e.getSource() == subjectButtons[0]){
            setMaterialSelect(1);
        }
        if(e.getSource() == subjectButtons[1]){
            setMaterialSelect(2);
        }
        if(e.getSource() == subjectButtons[2]){
            setMaterialSelect(3);
        }
        if(e.getSource() == subjectButtons[3]){
            setMaterialSelect(4);
        }
        if(e.getSource() == subjectButtons[4]){
            setMaterialSelect(5);
        }
        if(e.getSource() == subjectButtons[5]){
            setMaterialSelect(6);
        }
    }

    public int getMaterial(){
        return material;
    }

    public void setMaterialSelect(int subject){
        matSelect = new MaterialSelect(frame, main, this, subject);
        frame.remove(this);
        frame.add(matSelect);
        frame.revalidate();
        frame.repaint();
    }
}
