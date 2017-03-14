package GUI;

import Database.LoadingFrame;
import GUI.QuestionPane.Question;
import GUI.QuestionPane.QuestionPane;
import GUI.MainMenu.*;
import Database.DBAccess;
import GUI.QuestionPane.ScoreScreen;

import javax.rmi.CORBA.StubDelegate;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
/**
 * Created by mrfly on 7/19/2016.
 */
public class Main {
    public enum Activity{
        MainMenu,
        QuestionPane,
        ScoreScreen;

    }

    private JFrame frame;
    private QuestionPane questionPane;
    private MainMenu mainMenu;
    private TopMenu topBar;
    private ScoreScreen scoreScreen;
    private Activity currentActivity;
    public Main(){
        LoadingFrame loadingFrame = new LoadingFrame("Launching Application!");
        loadingFrame.setProgressUnknown(true);
        DBAccess.setupConnection();
        frame = new JFrame();
        topBar = new TopMenu(frame, this);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu = new MainMenu(frame, this);
        frame.add(mainMenu);
        frame.pack();
        frame.setLocationRelativeTo(null);
        currentActivity = Activity.MainMenu;
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            System.out.println(Arrays.toString(UIManager.getInstalledLookAndFeels()));
        }
        catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        questionPane = new QuestionPane(this, frame);
        //scoreScreen = new ScoreScreen(this, frame);
        loadingFrame.finish();
        frame.setVisible(true);
    }
    public static void main(String[] args){
        new Main();
    }

    public void changeQuestionSet(List<Question> questions){
        questionPane.changeQuestionSet(questions);
        frame.remove(mainMenu);
        frame.remove(scoreScreen);
        frame.add(questionPane);
        //frame.add(scoreScreen);
        //scoreScreen.setQuestionData(null);
        currentActivity = Activity.QuestionPane;
        frame.revalidate();
        frame.repaint();
    }
    public void addFiles(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Bin Files", "bin");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            System.out.println("File selected!");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DBAccess.addFilesToDatabase(fileChooser.getSelectedFiles());
                    DBAccess.updateCache();
                    topBar.updateMenu();
                }
            });
            thread.start();
        }
    }

    public void switchToMainMenu() {
        frame.remove(mainMenu);
        frame.remove(scoreScreen);
        frame.add(mainMenu);
        frame.revalidate();
        frame.repaint();
        currentActivity = Activity.MainMenu;
    }

    public void switchToScoreScreen(){
        frame.remove(mainMenu);
        frame.remove(questionPane);
        frame.add(mainMenu);
        frame.revalidate();
        frame.repaint();
        currentActivity = Activity.ScoreScreen;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
