package QuestionPane;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 120658 on 3/15/2016.
 */
public class QuestionPanelMain {
    public QuestionPanelMain(){
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:sample.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        frame.add(new QuestionPane(frame, con));
        frame.pack();
        frame.setLocationRelativeTo(null);
        if(!frame.isVisible())
            frame.setVisible(true);
    }
    public static void main(String[] args){
        new QuestionPanelMain();
    }
}
