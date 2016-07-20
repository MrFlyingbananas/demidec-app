package GUI.QuestionPane;

import Database.DBCreator.DBCreator;
import Database.DBReader;
import GUI.Main;
import Database.DBReader.Subject;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by mrfly on 7/18/2016.
 */
public class TopMenu {
    private static JMenuBar topBar;
    private Main main;
    public TopMenu(JFrame frame, Main main){
        JMenuItem item;
        topBar = new JMenuBar();
        this.main = main;
        JMenu baseMenu = new JMenu("File");
        topBar.add(baseMenu);
        item = new JMenuItem("Add file");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    System.out.println("File selected!");
                    DBCreator.addFilesToDatabase(fileChooser.getSelectedFiles());
                }
            }
        });
        baseMenu.add(item);
        item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        baseMenu.add(item);
        //TODO: A WAY TO SHOW TOP BAR STUFF ONLY IF INSTALLED
        Subject[] focusQuizSubjects = DBReader.getFocusQuizSubjects();
        if(focusQuizSubjects != null && focusQuizSubjects.length != 0){
            JMenu subMenu = new JMenu("By Subject");
            baseMenu = new JMenu("Focus Quizzes");
            topBar.add(baseMenu);
            baseMenu.add(subMenu);
            for (Subject sub : focusQuizSubjects) {
                item = new JMenuItem(DBReader.getSubjectString(sub));
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        main.changeQuestionSet(DBReader.getQuestionListBySubject(sub, DBReader.ListOrder.Random));
                    }
                });
            }
            subMenu.add(item);
            subMenu = new JMenu("By Quiz");
            baseMenu.add(subMenu);
            for (Subject sub : focusQuizSubjects) {
                JMenu testsMenu = new JMenu(DBReader.getSubjectString(sub) + " Quizzes");
                List<Test> tests = DBReader.getTestListBySubject(sub, DBReader.ListOrder.Random);
                for (Test t : tests) {
                    item = new JMenuItem(t.getSubtitle());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            main.changeQuestionSet(t.getQuestions());
                        }
                    });
                    testsMenu.add(item);
                }
                subMenu.add(testsMenu);
            }
        }

        frame.setJMenuBar(topBar);
    }
    public static int getHeight(){
        return topBar.getHeight();
    }
}
