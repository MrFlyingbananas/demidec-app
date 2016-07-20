package Database;

/**
 * Created by mrfly on 2/27/2016.
 */
public class SQLCommand {
    public String command;
    public String table;
    public SQLCommand(String command, String table){
        this.command = command;
        this.table = table;
    }
}
