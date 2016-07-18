package DBCreator;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

public class DBCreator_OLD {

    public static void main(String[] args) throws SQLException {
        DefaultHandler handler = new DefaultHandler(){
            private String prevTable;
            private ArrayList<String> tables = new ArrayList<String>();
            private Stack<Node> order = new Stack<Node>();
            private Node root;
            private Connection connection = null;
            private PreparedStatement pstmnt = null;
            private boolean first = true;
            private int valuesQueued = 0;
            private int maxValueQueue = 1000;
            @Override
            public void startDocument(){
                try {
                    System.out.println("Connecting to db...");
                    connection = DriverManager.getConnection("jdbc:sqlite:sample10.db");
                    System.out.println("Connected to db!");
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
                    while(rs.next()){
                        tables.add(rs.getString("TABLE_NAME"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }

            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
                if(first) {
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
                if(!order.empty() && order.peek() == root){
                    Node workingNode = root.getChildren().get(0);
                    if(!tables.contains(workingNode.getName())) {
                        String sql = "CREATE TABLE " + workingNode.getName() + "\n(\n";
                        for (int i = 0; i < workingNode.getChildren().size(); i++) {
                            Node child = workingNode.getChildren().get(i);
                            sql += child.getName() + " " + child.dataType();
                            if (i + 1 < workingNode.getChildren().size()) {
                                sql += ",\n";
                            }
                        }
                        sql += "\n);";
                        tables.add(qName);
                        try {
                            System.out.println("CREATING TABLE: " + qName);
                            connection.createStatement().executeUpdate(sql);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println(sql);
                        }
                        createSqlStatement(workingNode);
                    }
                    if(pstmnt == null){
                        createSqlStatement(workingNode);
                    }
                    if(!prevTable.equals(qName)){
                        createSqlStatement(workingNode);
                    }

                    for(int i = 0; i < workingNode.getChildren().size(); i++){
                        Node child = workingNode.getChildren().get(i);
                        try {
                            System.out.println(pstmnt.getParameterMetaData());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            pstmnt.setString(i, child.data);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        pstmnt.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
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
                if(sqlStmnt.charAt(sqlStmnt.length() - 1) == ',')
                    sqlStmnt = sqlStmnt.substring(0, sqlStmnt.length() - 1) + ";";
                try {
                    pstmnt.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println(sqlStmnt);
                    System.exit(0);
                }
            }

            private void createSqlStatement(Node workingNode) {
                prevTable = workingNode.getName();
                String sql = ("INSERT INTO " + workingNode.getName() + " (");
                String column = "";
                String values = "(";
                for(int i = 0; i < workingNode.getChildren().size(); i++){
                    Node child = workingNode.getChildren().get(i);
                    column += "'" + child.getName() + "'";
                    values += "?,";
                    if(i + 1 < workingNode.getChildren().size()) {
                        column += ", ";
                    }
                }
                column = column.trim();
                values = values.substring(0, values.length() -1) + ")";
                if(column.charAt(column.length() - 1) == ','){
                    column = column.substring(0, column.length() - 1);
                }
                sql +=column + ")\nVALUES" + values;
                System.out.println("CREATE STAMENT PSMNT VALUES: " + values);
                try {
                    pstmnt = connection.prepareStatement(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) {
                for (int i = start; i < start + length; i++) {
                    order.peek().data += ch[i];
                }
                order.peek().data = order.peek().data.trim();
            }
            @Override
            public void endDocument(){
               /* try {
                    System.out.println("EXEC BATCH");
                    stmnt.executeBatch();
                    System.out.println("BATCH DONE");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    stmnt.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }*/
                try {
                    System.out.println("EXECUTING");
                    pstmnt.executeBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };



        File file = new File("All Focused Quizzes.bin");
        byte[] data = new byte[(int)file.length()];
        try {
            DataInputStream in = new DataInputStream(new FileInputStream((file)));
            in.readFully(data);
            //File output = new File("temp.xml");
            //DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
            //out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(new ByteArrayInputStream(data), handler);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }


}
