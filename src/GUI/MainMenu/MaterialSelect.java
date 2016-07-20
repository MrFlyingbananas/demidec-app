package GUI.MainMenu;

import GUI.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kyle on 7/20/2016.
 */
public class MaterialSelect extends JPanel {

    private int material;
    private int subject;
    private JFrame frame;
    private Main main;

    public MaterialSelect(JFrame frame, Main main, SubjectScreen subScreen, int subject){

        this.frame = frame;
        frame.setResizable(false);
        setLayout(null);
        setMinimumSize(new Dimension(400, 400));
        setPreferredSize(new Dimension(600, 600));
        this.main = main;
        this.subject = subject;
        material = subScreen.getMaterial();



    }

}
