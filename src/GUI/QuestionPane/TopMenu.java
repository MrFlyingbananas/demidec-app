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
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mrfly on 7/18/2016.
 */
public class TopMenu {
    private static JMenuBar topBar;
    private JFrame frame;
    private Main main;
    private JMenu focusQuizMenu, levelExamMenu, compExamMenu, flashcardMenu;
    public TopMenu(JFrame frame, Main main){
        JMenuItem item;
        topBar = new JMenuBar();
        this.main = main;
        this.frame = frame;
        JMenu baseMenu = new JMenu("File");
        topBar.add(baseMenu);
        item = new JMenuItem("Add file");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Bin Files", "bin");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    System.out.println("File selected!");
                    DBCreator.addFilesToDatabase(fileChooser.getSelectedFiles());
                    DBReader.updateCache();
                    updateMenu();
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
        /*Subject[] focusQuizSubjects = DBReader.getFocusQuizSubjects();
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
        }*/
        updateMenu();
        frame.setJMenuBar(topBar);
    }
    public static int getHeight(){
        return topBar.getHeight();
    }
    private void updateMenu(){
        JMenuItem item;
        Subject[] subjects = DBReader.getFocusQuizSubjects();
        if(subjects != null && subjects.length != 0){
            List<Subject> updateSubjects = new ArrayList<>(Arrays.asList(subjects));
            JMenu subMenu = null;
            if(focusQuizMenu == null){
                focusQuizMenu = new JMenu("Focus Quizzes");
                topBar.add(focusQuizMenu);
                subMenu = new JMenu("By Subject");
                focusQuizMenu.add(subMenu);
                System.out.println("NO EXIST");
            }else{
                for (Component c : focusQuizMenu.getComponents()) {
                    if(c.getName().equals("By Subject")) {
                        subMenu = (JMenu) c;
                        System.out.println("EXISTERINO");
                        return;
                    }
                }
                List<String> names = new ArrayList<>();
                for(Component c : subMenu.getComponents()){
                    System.out.println(names);
                    names.add(c.getName());
                }
                for(Subject s : subjects){
                    for(int i = 0; i < names.size(); i++){
                        if(s.toString().equals(names.get(i))){
                            names.remove(i);
                            updateSubjects.remove(s);
                            break;
                        }
                    }
                }
            }
            for (Subject sub : updateSubjects) {
                item = new JMenuItem(DBReader.getSubjectString(sub));
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        main.changeQuestionSet(DBReader.getQuestionListBySubject(sub, DBReader.ListOrder.Random));
                    }
                });
                subMenu.add(item);
            }
            subMenu = new JMenu("By Quiz");
            focusQuizMenu.add(subMenu);
            for (Subject sub : subjects) {
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
        System.out.println("UPDATED");
        frame.setJMenuBar(topBar);
    }
}
