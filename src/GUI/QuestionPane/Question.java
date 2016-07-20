package GUI.QuestionPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by cisom on 6/24/16.
 */
public class Question {

    enum QDataID{
        QuestionID("QuestionID"),
        Question("Question"),
        QAnswer1("Answer1"),
        QAnswer2("Answer2"),
        QAnswer3("Answer3"),
        QAnswer4("Answer4"),
        QAnswer5("Answer5"),
        AnswerGuide("AnswerGuide"),
        TypeID("TypeID"),
        Source("Source"),
        Footnote("Footnote"),
        CardTypeID("CardTypeID"),
        Year("Year"),
        SubjectID("SubjectID"),
        QuestionNumber("QuesNumber"),
        AuxNumber("AuxNum"),
        RandList("RandList"),
        ResourceTypeID("ResourceTypeID"),
        TreeKey("TreeKey"),
        TestID("TestID"),
        DIffShort("DiffShort"),
        CardType("CardType");

        final String databaseID;
        QDataID(String databaseID){
            this.databaseID = databaseID;
        }
        String getID(){
            return databaseID;
        }
    }

    private String text;
    private String[] choices;
    private int correctAnswerIndex;
    private String answerGuide;
    public Question(ResultSet rs){
        try {
            Random random = new Random();
            text = rs.getString(QDataID.Question.getID()).trim().replace("\n", "").replace("\r", "");
            choices = new String[5];
            choices[0] = rs.getString(QDataID.QAnswer1.getID());
            choices[1] = rs.getString(QDataID.QAnswer2.getID());
            choices[2] = rs.getString(QDataID.QAnswer3.getID());
            choices[3] = rs.getString(QDataID.QAnswer4.getID());
            choices[4] = rs.getString(QDataID.QAnswer5.getID());
            for(int i =0; i < choices.length; i++){
                if(choices[i] == null)
                    continue;
                if(choices[i].contains("<chr")){
                    while(choices[i].contains("<chr")){
                        int loc = choices[i].indexOf("<chr");
                        choices[i] = choices[i].substring(0, loc) +
                                (char)Integer.parseInt(choices[i].substring(loc + 4, loc + 8)) + choices[i].substring(loc + 9);

                    }
                }
            }
            if(text.contains("<chr")){
                while(text.contains("<chr")){
                    int loc = text.indexOf("<chr");
                    text = text.substring(0, loc) +
                            (char)Integer.parseInt(text.substring(loc + 4, loc + 8)) + text.substring(loc + 9);

                }
            }
            int index;
            String temp;
            for(int i = choices.length - 1; i > 0; i--){
                index = random.nextInt(i + 1);
                temp = choices[index];
                if(index == correctAnswerIndex){
                    correctAnswerIndex = i;
                }else if(i == correctAnswerIndex){
                    correctAnswerIndex = index;
                }
                choices[index] = choices[i];
                choices[i] = temp;
            }
            answerGuide = rs.getString(QDataID.AnswerGuide.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String[] getChoices() {
        return choices;
    }
    public String getText() {
        return text;
    }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getAnswerGuide() { return answerGuide; }
}
