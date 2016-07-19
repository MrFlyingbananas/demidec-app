package QuestionPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by cisom on 6/23/16.
 */
public class DBReader {
    public enum ListOrder{
        Random,
        Normal,
        Inverse;
    }
    private enum SelectionID {
        All("*"),
        SubjectLong("SubjectLong"),
        SubjectID("SubjectID");
        final String id;
        SelectionID(String id){
            this.id = id;
        }
        String getSelectionID(){
            return id;
        }
    }
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
    private Random random;
    public DBReader(Connection con){
        this.con = con;
        try {
            stmnt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        random = new Random();
    }
    private ResultSet getData(TableID table, SelectionID data, String filter){
        try {
            return stmnt.executeQuery("SELECT " + data.getSelectionID() + " FROM " + table.getTableID() + " " + filter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private ResultSet getData(TableID table, SelectionID[] selections, String filter){
        try {
            String selection = "";
            for(int i = 0; i < selections.length; i++){
                selection += selections[i].getSelectionID();
                if(i + 1 != selections.length){
                    selection += " AND ";
                }
            }
            return stmnt.executeQuery("SELECT " + selection + " FROM " + table.getTableID() + " " + filter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Question> getQuestionListBySubject(Subject subject, ListOrder order){
        ResultSet rs = getData(TableID.Questions, SelectionID.All, "WHERE SubjectID=" + subject.subjectID);
        List<Question> questions = new ArrayList<>();
        try {
            while(rs.next()){
                questions.add(new Question(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        switch(order){
            case Random:
                Collections.shuffle(questions);
                break;
            case Inverse:
                Collections.reverse(questions);
        }
        return questions;
    }

    public String getSubjectString(Subject subject){
        try {
            return getData(TableID.Subjets, SelectionID.SubjectLong, "WHERE " + SelectionID.SubjectID.getSelectionID() + "=" + subject.getSubjectID()).getString(SelectionID.SubjectLong.getSelectionID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
