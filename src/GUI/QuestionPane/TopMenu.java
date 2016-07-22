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
    private JMenu focusQuizMenu, levelExamMenu, compExamMenu, sectionExamMenu, subMenu;
    public TopMenu(JFrame frame, Main main){
        JMenuItem item;
        topBar = new JMenuBar();
        this.main = main;
        this.frame = frame;
        JMenu baseMenu = new JMenu("File");
        topBar.add(baseMenu);
        item = new JMenuItem("Add files");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.addFiles();
            }
        });
        baseMenu.add(item);
        item = new JMenuItem("Main Menu");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.switchToMainMenu();
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
                            main.chang  eQuestionSet(t.getQuestions());
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
        for(int i = topBar.getMenuCount(); i < 0; i--){
            topBar.remove(i);
        }
        updateFocusQuizMenu();
        updateCompExamMenu();
        updateSectionExamMenu();
        updateLevelExamMenu();
        frame.setJMenuBar(topBar);
    }

    private void updateLevelExamMenu() {
        JMenuItem item;
        List<TestSet> updateSets = DBAccess.getTestSetListByResourceType(DBAccess.ResourceType.LevelExam);
        if(updateSets != null && updateSets.size() != 0) {
            subMenu = null;
            boolean exists = false;
            if (levelExamMenu == null) {
                levelExamMenu = new JMenu("Leveled Exams");
                topBar.add(levelExamMenu);
            } else {
                for (Component c : levelExamMenu.getMenuComponents()) {
                    JMenu menu = (JMenu) c;
                    if (menu.getText().equals("Exam Sets")) {
                        subMenu = menu;
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                subMenu = new JMenu("Exam Sets");
                levelExamMenu.add(subMenu);
            }
            List<String> names = new ArrayList<>();
            for (int i = 0; i < subMenu.getItemCount(); i++) {
                names.add(subMenu.getItem(i).getText());
            }
            for (String name : names) {
                for (int i = 0; i < updateSets.size(); i++) {
                    if (updateSets.get(i).getSetName().equals(name)) {
                        updateSets.remove(i);
                        i--;
                    }
                }
            }
            for (TestSet set : updateSets) {
                JMenu testsMenu = new JMenu(DBAccess.getSubjectString(set.getSubject()) + " " + set.getSetName());
                List<Test> tests = DBAccess.getTestListByTestSet(set, DBAccess.ListOrder.Normal);
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
    }

    private void updateSectionExamMenu() {
        JMenuItem item;
        List<TestSet> updateSets = DBAccess.getTestSetListByResourceType(DBAccess.ResourceType.SectionExam);
        if(updateSets != null && updateSets.size() != 0) {
            subMenu = null;
            boolean exists = false;
            if (sectionExamMenu == null) {
                sectionExamMenu = new JMenu("Section Exams");
                topBar.add(sectionExamMenu);
            } else {
                for (Component c : sectionExamMenu.getMenuComponents()) {
                    JMenu menu = (JMenu) c;
                    if (menu.getText().equals("Exam Sets")) {
                        subMenu = menu;
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                subMenu = new JMenu("Exam Sets");
                sectionExamMenu.add(subMenu);
            }
            List<String> names = new ArrayList<>();
            for (int i = 0; i < subMenu.getItemCount(); i++) {
                names.add(subMenu.getItem(i).getText());
            }
            for (String name : names) {
                for (int i = 0; i < updateSets.size(); i++) {
                    if (updateSets.get(i).getSetName().equals(name)) {
                        updateSets.remove(i);
                        i--;
                    }
                }
            }
            for (TestSet set : updateSets) {
                JMenu testsMenu = new JMenu(DBAccess.getSubjectString(set.getSubject()) + " " + set.getSetName());
                List<Test> tests = DBAccess.getTestListByTestSet(set, DBAccess.ListOrder.Normal);
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
    }

    private void updateCompExamMenu() {
        JMenuItem item;
        List<TestSet> updateSets = DBAccess.getTestSetListByResourceType(DBAccess.ResourceType.ComprehensiveExam);
        if(updateSets != null && updateSets.size() != 0) {
            subMenu = null;
            boolean exists = false;
            if (compExamMenu == null) {
                compExamMenu = new JMenu("Comprehensive Exams");
                topBar.add(compExamMenu);
            } else {
                for (Component c : compExamMenu.getMenuComponents()) {
                    JMenu menu = (JMenu) c;
                    if (menu.getText().equals("Exam Sets")) {
                        subMenu = menu;
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                subMenu = new JMenu("Exam Sets");
                compExamMenu.add(subMenu);
            }
            List<String> names = new ArrayList<>();
            for (int i = 0; i < subMenu.getItemCount(); i++) {
                names.add(subMenu.getItem(i).getText());
            }
            for (String name : names) {
                for (int i = 0; i < updateSets.size(); i++) {
                    if (updateSets.get(i).getSetName().equals(name)) {
                        updateSets.remove(i);
                        i--;
                    }
                }
            }
            for (TestSet set : updateSets) {
                JMenu testsMenu = new JMenu(DBAccess.getSubjectString(set.getSubject()) + " " + set.getSetName());
                List<Test> tests = DBAccess.getTestListByTestSet(set, DBAccess.ListOrder.Normal);
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
    }

    private void updateFocusQuizMenu() {
        JMenuItem item;
        Subject[] subjects = DBAccess.getFocusQuizSubjects();
        if(subjects != null && subjects.length != 0){
            List<Subject> updateSubjects = new ArrayList<>(Arrays.asList(subjects));
            subMenu = null;
            if(focusQuizMenu == null){
                focusQuizMenu = new JMenu("Focus Quizzes");
                topBar.add(focusQuizMenu);
            }
            boolean exists = false;
            for (Component c : focusQuizMenu.getMenuComponents()) {
                JMenu menu = (JMenu) c;
                if(menu.getText().equals("Quiz Sets")) {
                    subMenu = menu;
                    exists = true;
                    break;
                }
            }
            if(!exists){
                subMenu = new JMenu("By Quiz");
                focusQuizMenu.add(subMenu);
            }
            List<TestSet> updateSets = DBAccess.getTestSetListByResourceType(DBAccess.ResourceType.FocusQuiz);
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
                List<Test> tests = DBAccess.getTestListByTestSet(set, DBAccess.ListOrder.Normal);
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
            exists = false;
            for (Component c : focusQuizMenu.getMenuComponents()) {
                JMenu menu = (JMenu) c;
                if(menu.getText().equals("All Questions By Subject")) {
                    subMenu = menu;
                    exists = true;
                    break;
                }
            }
            if(!exists){
                subMenu = new JMenu("All Questions By Subject");
                focusQuizMenu.add(subMenu);
            }
            names = new ArrayList<>();
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
    }
}
