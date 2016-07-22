package Database;

import GUI.QuestionPane.Question;
import GUI.QuestionPane.Test;
import GUI.QuestionPane.TestSet;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by cisom on 6/23/16.
 */
public class DBAccess {
    public enum ListOrder {
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
        Subtitle("Subtitle"),
        ResourceTypeID("ResourceTypeID"),
        SetName("SetName"),
        ResourceType("ResourceType");

        final String id;

        SelectionID(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }

    private enum TableID {
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

        TableID(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }

    public enum Subject {
        Art(41),
        Econ(42),
        LangLit(43),
        Music(44),
        Science(45),
        SocialSci(47);
        final int subjectID;

        Subject(int subjectID) {
            this.subjectID = subjectID;
        }

        public String toString() {
            return Integer.toString(subjectID);
        }

        int getSubjectID() {
            return subjectID;
        }
    }

    public enum ResourceType {
        FocusQuiz("Focused Quizzes"),
        ComprehensiveExam("Comprehensive Exams"),
        SectionExam("Section Exams"),
        LevelExam("Leveled Exams");
        final String id;

        ResourceType(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    private static Connection con;
    private static Subject[] focusQuizSubjects, levelExamSubjects, sectionExamSubjects, comprehensiveExamSubjects, flashcardSubjects;
    private static Statement stmnt;

    public static void setupConnection() {
        try {
            File settingsDir = getSettingsDirectory();
            con = DriverManager.getConnection("jdbc:sqlite:" + settingsDir.getPath() + "\\" + "database.db");
            stmnt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateCache();
    }

    private static File getSettingsDirectory() {
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            throw new IllegalStateException("user.home==null");
        }
        File home = new File(userHome);
        File settingsDirectory = new File(home, ".DemiDec App");
        if (!settingsDirectory.exists()) {
            if (!settingsDirectory.mkdir()) {
                throw new IllegalStateException(settingsDirectory.toString());
            }
        }
        return settingsDirectory;
    }

    private static ResultSet getData(SelectionID selection, TableID table, String filter) {
        try {
            return stmnt.executeQuery("SELECT " + selection + " FROM " + table + " " + filter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ResultSet getData(SelectionID[] selections, TableID table, String filter) {
        try {
            String selection = "";
            for (int i = 0; i < selections.length; i++) {
                selection += selections[i];
                if (i +
                        1 != selections.length) {
                    selection += " AND ";
                }
            }
            return stmnt.executeQuery("SELECT " + selection + " FROM " + table + " " + filter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static ResultSet getData(SelectionID selection, TableID[] tables, String filter) {
        try {
            String table = "";
            for (int i = 0; i < tables.length; i++) {
                table += tables[i];
                if (i + 1 != tables.length) {
                    table += " AND ";
                }
            }
            return stmnt.executeQuery("SELECT " + selection + " FROM " + table + " " + filter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static List<Question> getQuestionListBySubject(Subject subject, ListOrder order) {
        ResultSet rs = getData(SelectionID.All, TableID.Questions, "WHERE SubjectID=" + subject.subjectID + " AND " + SelectionID.ResourceTypeID + "=" + getResourceTypeIDByResourceType(ResourceType.FocusQuiz));
        List<Question> questions = new ArrayList<>();
        try {
            while (rs.next()) {
                questions.add(new Question(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        switch (order) {
            case Random:
                Collections.shuffle(questions);
                break;
            case Inverse:
                Collections.reverse(questions);
        }
        return questions;
    }

    public static String getSubjectString(Subject subject) {
        try {
            return getData(SelectionID.SubjectLong, TableID.Subjets, "WHERE " + SelectionID.SubjectID + "=" + subject.getSubjectID()).getString(SelectionID.SubjectLong.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<TestSet> getTestSetListByResourceType(ResourceType resourceType) {
        List<TestSet> testSets = new ArrayList<TestSet>();
        boolean hasTable = false;
        DatabaseMetaData meta = null;
        try {
            meta = con.getMetaData();
            ResultSet test = meta.getTables(null, null, TableID.ResourceTypes.toString(), new String[]{"TABLE"});
            while (test.next()) {
                if (test.getString("TABLE_NAME").equals(TableID.ResourceTypes.toString()))
                    hasTable = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (hasTable) {
            ResultSet rs = getData(SelectionID.All, TableID.TestSets, "WHERE " + SelectionID.ResourceTypeID + "=" + getResourceTypeIDByResourceType(resourceType));
            try {
                while (rs.next()) {
                    int setID = rs.getInt(SelectionID.TestSetID.toString());
                    String setName = rs.getString(SelectionID.SetName.toString());
                    int subID = rs.getInt(SelectionID.SubjectID.toString());
                    Subject sub = null;
                    for (int i = 0; i < Subject.values().length; i++) {
                        if (Subject.values()[i].getSubjectID() == subID) {
                            sub = Subject.values()[i];
                        }
                    }
                    testSets.add(new TestSet(setID, setName, sub, resourceType));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return testSets;
        }else {
            return testSets;
        }
    }

    private static int getResourceTypeIDByResourceType(ResourceType resourceType) {
        try {
            return getData(SelectionID.ResourceTypeID, TableID.ResourceTypes, "WHERE " + SelectionID.ResourceType + "=" + "'" + resourceType + "'").getInt(SelectionID.ResourceTypeID.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Test> getTestListByTestSet(TestSet set, ListOrder questionOrder) {
        List<Test> tests = new ArrayList<Test>();
        ResultSet testSets = getData(SelectionID.All, TableID.Tests, "WHERE " + SelectionID.TestSetID + "=" + set.getSetID());
        List<String> subtitles = new ArrayList<String>();
        List<Integer> testIDs = new ArrayList<Integer>(), testNames = new ArrayList<Integer>();
        List<List<Question>> questionSets = new ArrayList<>();
        try {
            while (testSets.next()) {
                testIDs.add(testSets.getInt(SelectionID.TestID.toString()));
                testNames.add(testSets.getInt(SelectionID.TestName.toString()));
                subtitles.add(testSets.getString(SelectionID.Subtitle.toString()));
            }
            for (int testID : testIDs) {
                ResultSet resultSet = getData(SelectionID.All, TableID.Questions, "WHERE " + SelectionID.TestID + "=" + testID);
                List<Question> questions = new ArrayList<Question>();
                while (resultSet.next()) {
                    questions.add(new Question(resultSet));
                }
                switch (questionOrder) {
                    case Random:
                        Collections.shuffle(questions);
                        break;
                    case Inverse:
                        Collections.reverse(questions);
                }
                questionSets.add(questions);
            }
            for (int i = 0; i < questionSets.size(); i++) {
                if (subtitles.get(i).length() < 3) {
                    subtitles.set(i, getSubjectString(set.getSubject()) + " " + set.getSetName() + " " + testNames.get(i));
                }
                tests.add(new Test(set.getSubject(), testIDs.get(i), testNames.get(i), subtitles.get(i), questionSets.get(i)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }

    public static List<Test> getTestListBySubject(Subject subject, ListOrder questionOrder) {
        // TODO: 7/20/2016 OUTDATED DO NOT USE
        List<Test> tests = new ArrayList<Test>();
        int testSetID = 0;
        try {
            testSetID = getData(SelectionID.TestSetID, TableID.TestSets, "WHERE " + SelectionID.SubjectID + "=" + subject.getSubjectID()).getInt(SelectionID.TestSetID.toString());
            ResultSet testSets = getData(SelectionID.All, TableID.Tests, "WHERE " + SelectionID.TestSetID + "=" + testSetID);
            List<String> subtitles = new ArrayList<String>();
            List<Integer> testIDs = new ArrayList<Integer>(), testNames = new ArrayList<Integer>();
            List<List<Question>> questionSets = new ArrayList<>();
            while (testSets.next()) {
                testIDs.add(testSets.getInt(SelectionID.TestID.toString()));
                testNames.add(testSets.getInt(SelectionID.TestName.toString()));
                subtitles.add(testSets.getString(SelectionID.Subtitle.toString()));
            }
            for (int testID : testIDs) {
                ResultSet resultSet = getData(SelectionID.All, TableID.Questions, "WHERE " + SelectionID.TestID + "=" + testID);
                List<Question> questions = new ArrayList<Question>();
                while (resultSet.next()) {
                    questions.add(new Question(resultSet));
                }
                switch (questionOrder) {
                    case Random:
                        Collections.shuffle(questions);
                        break;
                    case Inverse:
                        Collections.reverse(questions);
                }
                questionSets.add(questions);
            }
            System.out.println("TOTAL STUFF: " + questionSets.size());
            for (int i = 0; i < questionSets.size(); i++) {
                tests.add(new Test(subject, testIDs.get(i), testNames.get(i), subtitles.get(i), questionSets.get(i)));
            }
            System.out.println(subject + ": " + tests.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }

    public static Subject[] getFocusQuizSubjects() {
        return focusQuizSubjects;
    }

    public static void updateCache() {
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet test = meta.getTables(null, null, TableID.TestSets.toString(), new String[]{"TABLE"});
            boolean hasTable = false;
            while (test.next()) {
                if (test.getString("TABLE_NAME").equals(TableID.TestSets.toString()))
                    hasTable = true;
            }
            if (hasTable) {
                ResultSet rs = getData(SelectionID.All, TableID.TestSets, "WHERE " + SelectionID.ResourceTypeID + "=" + getResourceTypeIDByResourceType(ResourceType.FocusQuiz));
                List<Integer> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getInt(SelectionID.SubjectID.toString()));
                }
                focusQuizSubjects = new Subject[list.size()];
                if (list.size() == 7) {
                    focusQuizSubjects[6] = Subject.LangLit;
                }
                int count = 0;
                for (int j = 0; j < Subject.values().length; j++) {
                    Subject sub = Subject.values()[j];
                    for (int i = 0; i < list.size(); i++) {
                        if (sub.getSubjectID() == list.get(i)) {
                            list.remove(i);
                            focusQuizSubjects[count++] = sub;
                            break;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

    }


    public static void addFilesToDatabase(File[] files) {
        //File file1 = new File("All Focused Quizzes.bin");
        //File file = new File("temp.xml");
        LoadingFrame frame = new LoadingFrame("Files loading, please wait", 0, files.length);
        int progress = 0;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setProgressText(0 + "/" + files.length + " files read");
                frame.setProgress(0 / files.length);
            }
        });
        for (File file : files) {
            DefaultHandler handler = new DefaultHandler() {
                private List<String> brokenSQL;
                private SQLCommand sqlCommand;
                private ArrayList<String> tables = new ArrayList<String>();
                private Stack<Node> order = new Stack<Node>();
                private Node root;
                private boolean first = true;
                private int valuesQueued = 0;
                private int maxValueQueue = 1000;
                private Statement stmnt;
                private int acceptedParams;

                @Override
                public void startDocument() {
                    try {
                        DatabaseMetaData metaData = con.getMetaData();
                        stmnt = con.createStatement();
                        ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
                        while (rs.next()) {
                            tables.add(rs.getString("TABLE_NAME"));
                        }
                        brokenSQL = new ArrayList<>();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                @Override
                public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
                    if (first) {
                        root = new Node(qName);
                        first = false;
                        order.add(root);
                        return;
                    }
                    Node newNode = new Node(qName);
                    order.peek().addChild(newNode);
                    order.add(newNode);


                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    order.pop();
                    if (!order.empty() && order.peek() == root) {
                        Node workingNode = root.getChildren().get(0);
                        if (!tables.contains(workingNode.getName())) {
                            String sql = "CREATE TABLE " + workingNode.getName() + "\n(\n";
                            for (int i = 0; i < workingNode.getChildren().size(); i++) {
                                Node child = workingNode.getChildren().get(i);
                                sql += child.getName() + " " + child.dataType();
                                if (i + 1 <= workingNode.getChildren().size()) {
                                    sql += ",\n";
                                }
                            }
                            sql += "PRIMARY KEY (" + workingNode.getChildren().get(0).getName() + ")";
                            sql += "\n);";
                            tables.add(qName);
                            try {
                                System.out.println("CREATING TABLE: " + qName);
                                con.createStatement().executeUpdate(sql);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                System.out.println(sql);
                            }
                            if (sqlCommand != null) {
                                addSQLToBatch(sqlCommand.command);
                                valuesQueued = 0;
                            }

                        }
                        if (sqlCommand == null) {
                            createSqlStatement(workingNode);
                        }
                        if (!sqlCommand.table.equals(workingNode.getName())) {
                            addSQLToBatch(sqlCommand.command);
                            valuesQueued = 0;
                            createSqlStatement(workingNode);
                        }
                        if (workingNode.getChildren().size() != acceptedParams) {
                            //System.out.println(workingNode.getChildren().get(0).data);
                            String data = "";
                            for (int i = 0; i < workingNode.getChildren().size(); i++) {
                                Node child = workingNode.getChildren().get(i);
                                child.data = child.data.replace("\'", "\'\'");
                                data += "'" + child.data + "'";
                                if (i + 1 < workingNode.getChildren().size()) {
                                    data += ", ";
                                }
                            }
                            data = data.trim();
                            if (data.charAt(data.length() - 1) == ',') {
                                data = data.substring(0, data.length() - 1);
                            }
                            String sql = "INSERT OR IGNORE INTO " + workingNode.getName() + " (";
                            String column = "";
                            for (int i = 0; i < workingNode.getChildren().size(); i++) {
                                Node child = workingNode.getChildren().get(i);
                                column += "'" + child.getName() + "'";
                                if (i + 1 < workingNode.getChildren().size()) {
                                    column += ", ";
                                }
                            }
                            column = column.trim();
                            if (column.charAt(column.length() - 1) == ',') {
                                column = column.substring(0, column.length() - 1);
                            }
                            sql += column + ")\nVALUES" + "(" + data + ")";
                            brokenSQL.add(sql);
                        }
                        if (workingNode.getChildren().size() == acceptedParams) {
                            String data = "";
                            for (int i = 0; i < workingNode.getChildren().size(); i++) {
                                Node child = workingNode.getChildren().get(i);
                                child.data = child.data.replace("\'", "\'\'");
                                data += "'" + child.data + "'";
                                if (i + 1 < workingNode.getChildren().size()) {
                                    data += ", ";
                                }
                            }
                            data = data.trim();
                            if (data.charAt(data.length() - 1) == ',') {
                                data = data.substring(0, data.length() - 1);
                            }
                            if (valuesQueued < maxValueQueue) {
                                sqlCommand.command += "(" + data + "), ";
                                valuesQueued++;
                            } else {
                                addSQLToBatch(sqlCommand.command);
                                createSqlStatement(workingNode);
                                sqlCommand.command += "(" + data + "),  ";
                                valuesQueued = 1;
                            }
                        }
                        order.pop();
                        root = new Node(root.getName());
                        order.add(root);
                    }

            /*if(order.peek() != root)
                order.pop();
            if(order.peek() == root) {
                for (Node<String> parent : root.getChildren()) {
                    for(Node<String> child : parent.getChildren()){
                        System.out.println(child.getName() + ": " + child.getData());
                    }
                }
            }*/
            /* if(order.peek() == root){
                for(Node<String> parent : root.getChildren()){

                }
                /*for(Node<String> parent : root.getChildren()){
                    String sql = "CREATE TABLE " + parent.getName() + "(";
                    String primaryKey = "PRIMARY KEY(" + parent.getData() + ")" + System.getProperty("line.separator") + ");";
                    for(Node<String> child : parent.getChildren()){
                        sql += child.getData() + " ";
                        try{
                            int temp = Integer.parseInt(child.getData());
                            sql+= "INT, " + System.getProperty("line.separator");
                        }catch(NumberFormatException e){
                            sql += "NVARCHAR, " + System.getProperty("line.separator");
                        }
                    }
                    sql +=  primaryKey;

                    try {
                        stmnt.executeUpdate(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    root = new Node<String>("root");
                    order.pop();
                    order.add(root);
                }*/

                }

                private void addSQLToBatch(String sqlStmnt) {
                    sqlStmnt = sqlStmnt.trim();
                    if (sqlStmnt.charAt(sqlStmnt.length() - 1) == ',')
                        sqlStmnt = sqlStmnt.substring(0, sqlStmnt.length() - 1) + ";";
                    try {
                        stmnt.addBatch(sqlStmnt);
                        valuesQueued = 0;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.exit(0);
                /*int begin =  sqlStmnt.substring(0, sqlStmnt.indexOf("VALUES") + 6).length();
                String sql = sqlStmnt.substring(0, sqlStmnt.indexOf("VALUES") + 6);
                while(begin < sqlStmnt.length()){
                    int parLoc = sqlStmnt.indexOf('(', begin);
                    String values = sqlStmnt.substring(parLoc, sqlStmnt.indexOf(')', parLoc) + 1);
                    String command = sql + values;
                    try {
                        stmnt.addBatch(command);
                        stmnt.executeBatch();
                        stmnt.clearBatch();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        System.out.println(command);
                        System.exit(0);
                    }
                    begin += values.length();
                }*/


                    }
                }

                private void createSqlStatement(Node workingNode) {
                    acceptedParams = workingNode.getChildren().size();
                    sqlCommand = new SQLCommand("INSERT OR IGNORE INTO " + workingNode.getName() + " (", workingNode.getName());
                    String column = "";
                    for (int i = 0; i < workingNode.getChildren().size(); i++) {
                        Node child = workingNode.getChildren().get(i);
                        column += "'" + child.getName() + "'";
                        if (i + 1 < workingNode.getChildren().size()) {
                            column += ", ";
                        }
                    }
                    column = column.trim();
                    if (column.charAt(column.length() - 1) == ',') {
                        column = column.substring(0, column.length() - 1);
                    }
                    sqlCommand.command += column + ")\nVALUES";
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    for (int i = start; i < start + length; i++) {
                        order.peek().data += ch[i];
                    }
                    order.peek().data = order.peek().data.trim();
                }

                @Override
                public void endDocument() {
                    try {
                        if (valuesQueued != 0) {
                            addSQLToBatch(sqlCommand.command);
                        }
                        for (String sql : brokenSQL) {
                            stmnt.addBatch(sql);
                        }
                        System.out.println("EXEC BATCH");
                        stmnt.executeBatch();
                        System.out.println("BATCH DONE");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        stmnt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            };

            byte[] data = new byte[(int) file.length()];
            try {
                DataInputStream in = new DataInputStream(new FileInputStream((file)));
                in.readFully(data);
                File output = new File("temp.xml");
                DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
                out.write(data);
                out.close();
                in.close();
                FileReader fr = new FileReader(output);
                BufferedReader br = new BufferedReader(fr);
                String line = br.readLine().replace("1.0", "1.1");
                br.close();
                fr.close();
                RandomAccessFile rFile = new RandomAccessFile(output, "rws");
                rFile.write(line.getBytes());
                rFile.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            try {
                File temp = new File("temp.xml");
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(temp, handler);
                Files.delete(temp.toPath());
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
            progress++;
            final int progressFinal = progress;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setProgressText(progressFinal + "/" + files.length + " files read");
                    frame.setProgress(progressFinal);
                }
            });
        }
        frame.finish();
    }
}
