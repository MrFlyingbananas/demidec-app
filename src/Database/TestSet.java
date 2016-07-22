package Database;

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
    private DBAccess.ResourceType resourceType;
    public TestSet(int testSetID, String setName, Subject subject, DBAccess.ResourceType resourceType){
        this.setID = testSetID;
        this.setName = setName;
        this.subject = subject;
        this.resourceType = resourceType;
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

    public DBAccess.ResourceType getResourceType() {
        return resourceType;
    }

}
