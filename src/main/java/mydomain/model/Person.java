package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public class Person
{
    @PrimaryKey
    Long id;

    String name;

    @Extension(vendorName="datanucleus", key="fetch-fk-only", value="true")
    Enterprise enterprise;
    
    public Person(long id, String name, Enterprise ent)
    {
        this.id = id;
        this.name = name;
        this.enterprise = ent;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
    
    public Enterprise getEnterprise()
    {
        return enterprise;
    }
}
