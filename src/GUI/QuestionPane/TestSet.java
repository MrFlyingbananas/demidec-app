package GUI.QuestionPane;

import Database.DBAccess;
import Database.DBAccess.Subject;

import java.sql.ResultSet;

/**
 * Created by mrfly on 7/20/2016.
 */
public class TestSet {
    private int setID;
    private String setName;
    private Subject subject;
    private DBAccess.ResourceTypeID resourceTypeID;
    public TestSet(int testSetID, String setName, Subject subject, DBAccess.ResourceTypeID resourceTypeID){
        this.setID = testSetID;
        this.setName = setName;
        this.subject = subject;
        this.resourceTypeID = resourceTypeID;
    }

    public int getSetID() {
        return setID;
    }

    public String getSetName() {
        return setName;
    }

    public Subject getSubject() {
        return subject;
    }

    public DBAccess.ResourceTypeID getResourceTypeID() {
        return resourceTypeID;
    }

}
