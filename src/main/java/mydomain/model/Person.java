package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable
public class Person
{
    @PrimaryKey
    Long perId;

    String perName;

    //@Persistent(defaultFetchGroup="true", recursionDepth = 0)
    @Persistent(defaultFetchGroup="true")
    @Extension(vendorName="datanucleus", key="fetch-fk-only", value="true")
    Enterprise perEnterprise;
    
    public Person(long id, String name, Enterprise ent)
    {
        this.perId = id;
        this.perName = name;
        this.perEnterprise = ent;
    }

    public Long getId()
    {
        return perId;
    }

    public String getName()
    {
        return perName;
    }
    
    public Enterprise getEnterprise()
    {
        return perEnterprise;
    }
}
