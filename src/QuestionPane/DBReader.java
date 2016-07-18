package QuestionPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cisom on 6/23/16.
 */
public class DBReader {
    private enum TableID{
        CardTypes("CardTypes"),
        Outlines("Outlines"),
        OutlinesAndQuestions("OutlinesAndQuestions"),
        Questions("Questions"),
        ResourceTypes("ResourceTypes"),
        Subjets("Subjects"),
        TestSets("TestSets"),
        Tests("Tests"),
        TestAndQuestions("TestAndQuestions");

        final String id;
        TableID(String id){
            this.id = id;
        }
        String getTableID(){
            return id;
        }
    }
    public enum Subject{
        Art(41),
        Econ(42),
        LangLit(43),
        Music(44),
        Science(45),
        SocialSci(47);
        final int subjectID;

        Subject(int subjectID){
            this.subjectID = subjectID;
        }

        int getSubjectID() {
            return subjectID;
        }
    }
    private Connection con;
    private String[] tableNames, dataNames;
    private Statement stmnt;
    public DBReader(Connection con){
        this.con = con;
        try {
            stmnt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private ResultSet getData(TableID table, String selection, String filter){
        try {
            return stmnt.executeQuery("SELECT " + selection + " FROM " + table.getTableID() + " " + filter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Question> getQuestionListBySubject(Subject subject){
        ResultSet rs = getData(TableID.Questions, "*", "WHERE SubjectID=" + subject.subjectID);
        List<Question> questions = new ArrayList<>();
        try {
            while(rs.next()){
                questions.add(new Question(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
