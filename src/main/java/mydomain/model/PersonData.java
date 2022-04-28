package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public class PersonData
{
    @PrimaryKey
    Long id;

    String name;
    
    @Extension(vendorName="datanucleus", key="fetch-fk-only", value="true")
    Person person;

    public PersonData(long id, String name, Person person)
    {
        this.id = id;
        this.name = name;
        this.person = person;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
    
    public Person getPerson() {
        return person;
    }
}
