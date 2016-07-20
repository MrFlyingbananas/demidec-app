package QuestionPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        SubjectID("SubjectID"),
        TestSetID("TestSetID"),
        TestID("TestID"),
        TestName("TestName"),
        Subtitle("Subtitle");
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
    public static boolean databaseExists = false, focusQuizzesAdded = false;
    private static Statement stmnt;
    public static void setConnection(Connection con){
        try {
            stmnt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private static ResultSet getData(SelectionID selection, TableID table, String filter){
        try {
            return stmnt.executeQuery("SELECT " + selection.getSelectionID() + " FROM " + table.getTableID() + " " + filter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static ResultSet getData(SelectionID[] selections, TableID table, String filter){
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
    private static ResultSet getData(SelectionID selection, TableID[] tables, String filter){
        try {
            String table = "";
            for(int i = 0; i < tables.length; i++){
                table += tables[i].getTableID();
                if(i + 1 != tables.length){
                    table += " AND ";
                }
            }
            return stmnt.executeQuery("SELECT " + selection.getSelectionID() + " FROM " + table + " " + filter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static List<Question> getQuestionListBySubject(Subject subject, ListOrder order){
        ResultSet rs = getData(SelectionID.All, TableID.Questions, "WHERE SubjectID=" + subject.subjectID);
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

    public static String getSubjectString(Subject subject){
        try {
            return getData(SelectionID.SubjectLong, TableID.Subjets, "WHERE " + SelectionID.SubjectID.getSelectionID() + "=" + subject.getSubjectID()).getString(SelectionID.SubjectLong.getSelectionID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Test> getTestListBySubject(Subject subject, ListOrder questionOrder){
        List<Test> tests = new ArrayList<Test>();
        int testSetID = 0;
        try {
            testSetID = getData(SelectionID.TestSetID, TableID.TestSets, "WHERE " + SelectionID.SubjectID.getSelectionID() + "=" + subject.getSubjectID()).getInt(SelectionID.TestSetID.getSelectionID());
            ResultSet testSets = getData(SelectionID.All, TableID.Tests, "WHERE " + SelectionID.TestSetID.getSelectionID() + "=" + testSetID);
            List<String> subtitles = new ArrayList<String>();
            List<Integer> testIDs = new ArrayList<Integer>(), testNames = new ArrayList<Integer>();
            List<List<Question>> questionSets = new ArrayList<>();
            while (testSets.next()) {
                testIDs.add(testSets.getInt(SelectionID.TestID.getSelectionID()));
                testNames.add(testSets.getInt(SelectionID.TestName.getSelectionID()));
                subtitles.add(testSets.getString(SelectionID.Subtitle.getSelectionID()));
            }
            for(int testID : testIDs){
                ResultSet resultSet = getData(SelectionID.All, TableID.Questions, "WHERE " + SelectionID.TestID.getSelectionID() + "=" + testID);
                List<Question> questions = new ArrayList<Question>();
                while(resultSet.next()){
                    questions.add(new Question(resultSet));
                }
                switch(questionOrder){
                    case Random:
                        Collections.shuffle(questions);
                        break;
                    case Inverse:
                        Collections.reverse(questions);
                }
                questionSets.add(questions);
            }
            System.out.println("TOTAL STUFF: " + questionSets.size());
            for(int i = 0; i < questionSets.size(); i++){
                tests.add(new Test(subject, testIDs.get(i), testNames.get(i), subtitles.get(i), questionSets.get(i)));
            }
            System.out.println(subject + ": " + tests.size());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }
}
