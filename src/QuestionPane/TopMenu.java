package QuestionPane;

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
    private JMenuBar topBar;
    private QuestionPane questionPane;
    public TopMenu(JFrame frame, QuestionPane pane, DBReader reader){
        topBar = new JMenuBar();
        this.questionPane = pane;
        JMenu baseMenu = new JMenu("Change Questions");
        topBar.add(baseMenu);
        JMenu subMenu = new JMenu("By Subject");
        baseMenu.add(subMenu);
        JMenuItem item = new JMenuItem(reader.getSubjectString(DBReader.Subject.Art));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionPane.changeQuestionSet(reader.getQuestionListBySubject(DBReader.Subject.Art, DBReader.ListOrder.Random));
            }
        });
        subMenu.add(item);
        item = new JMenuItem(reader.getSubjectString(DBReader.Subject.Music));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionPane.changeQuestionSet(reader.getQuestionListBySubject(DBReader.Subject.Music, DBReader.ListOrder.Random));
            }
        });
        subMenu.add(item);
        item = new JMenuItem(reader.getSubjectString(DBReader.Subject.LangLit));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionPane.changeQuestionSet(reader.getQuestionListBySubject(DBReader.Subject.LangLit, DBReader.ListOrder.Random));
            }
        });
        subMenu.add(item);
        item = new JMenuItem(reader.getSubjectString(DBReader.Subject.Science));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionPane.changeQuestionSet(reader.getQuestionListBySubject(DBReader.Subject.Science, DBReader.ListOrder.Random));
            }
        });
        subMenu.add(item);
        item = new JMenuItem(reader.getSubjectString(DBReader.Subject.SocialSci));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionPane.changeQuestionSet(reader.getQuestionListBySubject(DBReader.Subject.SocialSci, DBReader.ListOrder.Random));
            }
        });
        subMenu.add(item);
        item = new JMenuItem(reader.getSubjectString(DBReader.Subject.Econ));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionPane.changeQuestionSet(reader.getQuestionListBySubject(DBReader.Subject.Econ, DBReader.ListOrder.Random));
            }
        });
        subMenu.add(item);
        subMenu = new JMenu("By Test");
        baseMenu.add(subMenu);

        JMenu testsMenu = new JMenu(reader.getSubjectString(DBReader.Subject.Art) + " Tests");
        List<Test> tests = reader.getTestListBySubject(DBReader.Subject.Art, DBReader.ListOrder.Random);
        for(Test t : tests){
            item = new JMenuItem(t.getSubtitle());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    questionPane.changeQuestionSet(t.getQuestions());
                }
            });
            testsMenu.add(item);
        }
        subMenu.add(testsMenu);

        testsMenu = new JMenu(reader.getSubjectString(DBReader.Subject.Music) + " Tests");
        tests = reader.getTestListBySubject(DBReader.Subject.Music, DBReader.ListOrder.Random);
        for(Test t : tests){
            item = new JMenuItem(t.getSubtitle());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    questionPane.changeQuestionSet(t.getQuestions());
                }
            });
            testsMenu.add(item);
        }
        subMenu.add(testsMenu);

        testsMenu = new JMenu(reader.getSubjectString(DBReader.Subject.LangLit) + " Tests");
        tests = reader.getTestListBySubject(DBReader.Subject.LangLit, DBReader.ListOrder.Random);
        for(Test t : tests){
            item = new JMenuItem(t.getSubtitle());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    questionPane.changeQuestionSet(t.getQuestions());
                }
            });
            testsMenu.add(item);
        }
        subMenu.add(testsMenu);
        testsMenu = new JMenu(reader.getSubjectString(DBReader.Subject.Science) + " Tests");
        tests = reader.getTestListBySubject(DBReader.Subject.Science, DBReader.ListOrder.Random);
        for(Test t : tests){
            item = new JMenuItem(t.getSubtitle());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    questionPane.changeQuestionSet(t.getQuestions());
                }
            });
            testsMenu.add(item);
        }
        subMenu.add(testsMenu);
        testsMenu = new JMenu(reader.getSubjectString(DBReader.Subject.SocialSci) + " Tests");
        tests = reader.getTestListBySubject(DBReader.Subject.SocialSci, DBReader.ListOrder.Random);
        for(Test t : tests){
            item = new JMenuItem(t.getSubtitle());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    questionPane.changeQuestionSet(t.getQuestions());
                }
            });
            testsMenu.add(item);
        }
        subMenu.add(testsMenu);
        testsMenu = new JMenu(reader.getSubjectString(DBReader.Subject.Econ) + " Tests");
        tests = reader.getTestListBySubject(DBReader.Subject.Econ, DBReader.ListOrder.Random);
        for(Test t : tests){
            item = new JMenuItem(t.getSubtitle());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    questionPane.changeQuestionSet(t.getQuestions());
                }
            });
            testsMenu.add(item);
        }
        subMenu.add(testsMenu);
        frame.setJMenuBar(topBar);
    }
    public int getHeight(){
        return topBar.getHeight();
    }
}
