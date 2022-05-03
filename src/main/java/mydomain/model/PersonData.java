package mydomain.model;

import javax.jdo.annotations.*;

//@FetchGroup(name = "default", members = {@Persistent(name="enterprise", recursionDepth = 0)})
@PersistenceCapable
public class PersonData
{
    @PrimaryKey
    Long dataId;

    String dataName;
    
    //@Persistent(defaultFetchGroup="true", recursionDepth = 0)
    @Persistent(defaultFetchGroup="true")
    @Extension(vendorName="datanucleus", key="fetch-fk-only", value="true")
    Person dataPerson;
    
    //needed hack to allow 0, there is code that turns it into 1. Also java doc says only works on fetch group usage, but seems to work
    //@Persistent(defaultFetchGroup="true", recursionDepth = 0)
    @Persistent(defaultFetchGroup="true")
    @Extension(vendorName="datanucleus", key="fetch-fk-only", value="true")
    //@Persistent(defaultFetchGroup="true", extensions = { @Extension(vendorName="datanucleus", key="fetch-fk-only", value="true") })
    Enterprise dataEnterprise;

    public PersonData(long id, String name, Person person, Enterprise ent)
    {
        this.dataId = id;
        this.dataName = name;
        this.dataPerson = person;
        this.dataEnterprise = ent;
    }

    public Long getId()
    {
        return dataId;
    }

    public String getName()
    {
        return dataName;
    }
    
    public Person getPerson() {
        return dataPerson;
    }
    
    public Enterprise getEnterprise() {
        return dataEnterprise;
    }
}
