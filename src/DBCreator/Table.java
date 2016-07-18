package DBCreator;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by mrfly on 2/27/2016.
 */
public class Table {
    private String name;
    private ArrayList<String> insertValues;
    private String sqlInsertStatment;
    private int execCount = 0;
    public Table(String name){
        this.name = name;
        insertValues = new ArrayList<String>();
        insertValues.add("");
    }
    public void setSqlInsertStatment(String sql){
        sqlInsertStatment = sql;
    }
    public void addInsertValues(String values){
        if(++execCount % 1000 != 0){
            insertValues.set(execCount / 1000, insertValues.get(execCount / 1000) + " ," + values);
        }else{
            insertValues.add(values);
        }
    }
    public void executeSQL(Statement stmnt){

        try {
            for(String insert : insertValues){
                stmnt.addBatch(sqlInsertStatment + insert);
            }
            stmnt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getName(){
        return name;
    }
    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(!Table.class.isAssignableFrom(obj.getClass()))
            return false;
        Table table = (Table)obj;
        if(table.name.equals(this.name))
            return true;
        return false;
    }


}
