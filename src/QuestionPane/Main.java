package QuestionPane;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mrfly on 7/19/2016.
 */
public class Main {
    private JFrame frame;
    private QuestionPane questionPane;
    private MainMenu mainMenu;
    public Main(){
        try {
            DBReader.setConnection(DriverManager.getConnection("jdbc:sqlite:database.db"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        frame = new JFrame();
        new TopMenu(frame, this);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu = new MainMenu(frame, this);
        frame.add(mainMenu);
        frame.pack();
        frame.setLocationRelativeTo(null);

        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            System.out.println(Arrays.toString(UIManager.getInstalledLookAndFeels()));
        }
        catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        questionPane = new QuestionPane(frame);
        frame.setVisible(true);
    }
    public static void main(String[] args){
        new Main();
    }
    public void changeQuestionSet(List<Question> questions){
        questionPane.changeQuestionSet(questions);
        frame.remove(mainMenu);
        frame.add(questionPane);
        frame.revalidate();
        frame.repaint();
    }
}
