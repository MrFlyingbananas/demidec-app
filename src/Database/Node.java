package Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrfly on 2/25/2016.
 */
public class Node {
    private String name;
    public String data;
    private List<Node> children;
    public Node(String title) {
        this.name = title;
        children = new ArrayList<Node>();
        data = "";
    }
    public void addChild(Node n){
        children.add(n);
    }
    public String getName() { return name; }
    public List<Node> getChildren() {
        return children;
    }
    public String dataType() {
        try{
            int i = Integer.parseInt(data);
            return "int";
        }catch(NumberFormatException e){
            return "nvarchar";
        }

    }
}
