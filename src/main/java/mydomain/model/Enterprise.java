package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable
public class Enterprise
{
    @PrimaryKey
    Long entId;

    String entName;

    public Enterprise(long id, String name)
    {
        this.entId = id;
        this.entName = name;
    }

    public Long getId()
    {
        return entId;
    }

    public String getName()
    {
        return entName;
    }
}
