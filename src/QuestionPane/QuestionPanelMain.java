package QuestionPane;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by 120658 on 3/15/2016.
 */
/*public class QuestionPanelMain {
    public QuestionPanelMain(){
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            System.out.println(Arrays.toString(UIManager.getInstalledLookAndFeels()));
        }
        catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:sample.db");
            DBReader.setConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        frame.add(new QuestionPane(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        if(!frame.isVisible())
            frame.setVisible(true);
    }
    public static void main(String[] args){
        new QuestionPanelMain();
    }
}*/
