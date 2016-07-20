package GUI.QuestionPane;

import Database.DBAccess;
import Database.DBAccess.Subject;

import java.util.List;
/**
 * Created by mrfly on 7/19/2016.
 */
public class Test {


    private enum TestDataID{
        TestID("TestID"),
        TestSetID("TestSetID"),
        TestName("TestName"),
        Subtitle("Subtitle"),
        SubjectID("SubjectID");

        final String id;
        TestDataID(String id){
            this.id = id;
        }
        String getTestDataID(){
            return id;
        }
    }
    private String subtitle;
    private List<Question> questions;
    private int testID;
    private Subject subject;
    private int testNumber;
    public Test(Subject subject, int testID, int testNumber, String subtitle, List<Question> questions){
        this.subject = subject;
        this.testID = testID;
        this.subtitle = subtitle;
        this.questions = questions;
        this.testNumber = testNumber;
        if(subtitle.length() == 0)
            this.subtitle = DBAccess.getSubjectString(subject) + " Quiz " + testNumber;
    }
    public List<Question> getQuestions(){
        return questions;
    }
    public String getSubtitle() {
        return subtitle;
    }

    public Subject getSubject() {
        return subject;
    }

}
