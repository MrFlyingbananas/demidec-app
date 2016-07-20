package GUI.QuestionPane;

import Database.DBAccess;
import GUI.Main;
import Database.DBAccess.Subject;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    private JMenu focusQuizMenu, levelExamMenu, compExamMenu, flashcardMenu, subMenu;
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
                main.addFiles();
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
        /*Subject[] focusQuizSubjects = DBAccess.getFocusQuizSubjects();
        if(focusQuizSubjects != null && focusQuizSubjects.length != 0){
            JMenu subMenu = new JMenu("By Subject");
            baseMenu = new JMenu("Focus Quizzes");
            topBar.add(baseMenu);
            baseMenu.add(subMenu);
            for (Subject sub : focusQuizSubjects) {
                item = new JMenuItem(DBAccess.getSubjectString(sub));
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        main.changeQuestionSet(DBAccess.getQuestionListBySubject(sub, DBAccess.ListOrder.Random));
                    }
                });
            }
            subMenu.add(item);
            subMenu = new JMenu("By Quiz");
            baseMenu.add(subMenu);
            for (Subject sub : focusQuizSubjects) {
                JMenu testsMenu = new JMenu(DBAccess.getSubjectString(sub) + " Quizzes");
                List<Test> tests = DBAccess.getTestListBySubject(sub, DBAccess.ListOrder.Random);
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
    public void updateMenu(){
        JMenuItem item;
        Subject[] subjects = DBAccess.getFocusQuizSubjects();
        if(subjects != null && subjects.length != 0){
            List<Subject> updateSubjects = new ArrayList<>(Arrays.asList(subjects));
            subMenu = null;
            if(focusQuizMenu == null){
                focusQuizMenu = new JMenu("Focus Quizzes");
                topBar.add(focusQuizMenu);
                subMenu = new JMenu("By Subject");
                focusQuizMenu.add(subMenu);
            }else{
                for (Component c : focusQuizMenu.getMenuComponents()) {
                    JMenu menu = (JMenu) c;
                    if(menu.getText().equals("By Subject")) {
                        subMenu = menu;
                        break;
                    }
                }
                List<String> names = new ArrayList<>();
                for(int i = 0; i < subMenu.getItemCount(); i++){
                    names.add(subMenu.getItem(i).getText());
                }
                for(Subject s : subjects){
                    for(int i = 0; i < names.size(); i++){
                        if(DBAccess.getSubjectString(s).equals(names.get(i))){
                            names.remove(i);
                            updateSubjects.remove(s);
                            break;
                        }
                    }
                }
            }
            for (Subject sub : updateSubjects) {
                item = new JMenuItem(DBAccess.getSubjectString(sub));
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        main.changeQuestionSet(DBAccess.getQuestionListBySubject(sub, DBAccess.ListOrder.Random));
                    }
                });
                subMenu.add(item);
            }
            boolean exists = false;
            for (Component c : focusQuizMenu.getMenuComponents()) {
                JMenu menu = (JMenu) c;
                if(menu.getText().equals("By Quiz")) {
                    subMenu = menu;
                    exists = true;
                    break;
                }
            }
            if(!exists){
                subMenu = new JMenu("By Quiz");
                focusQuizMenu.add(subMenu);
            }
            List<TestSet> updateSets = DBAccess.getTestSetListByResourceTypeID(DBAccess.ResourceTypeID.FocusQuiz);
            List<String> names = new ArrayList<>();
            for(int i = 0; i < subMenu.getItemCount(); i++){
                names.add(subMenu.getItem(i).getText());
            }
            for(String name : names){
                for(int i = 0; i < updateSets.size(); i++){
                    if(updateSets.get(i).getSetName().equals(name)){
                        updateSets.remove(i);
                        i--;
                    }
                }
            }
            for(TestSet set : updateSets){
                JMenu testsMenu = new JMenu(DBAccess.getSubjectString(set.getSubject()) + " " + set.getSetName());
                List<Test> tests = DBAccess.getTestListByTestSet(set, DBAccess.ListOrder.Random);
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
            /*for (Subject sub : updateSubjects) {
                JMenu testsMenu = new JMenu(DBAccess.getSubjectString(sub) + " Quizzes");
                List<Test> tests = DBAccess.getTestListBySubject(sub, DBAccess.ListOrder.Random);
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
            }*/
        }
        frame.setJMenuBar(topBar);
    }
}
