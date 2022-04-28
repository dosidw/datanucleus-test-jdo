package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public class Enterprise
{
    @PrimaryKey
    Long id;

    String name;

    public Enterprise(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
