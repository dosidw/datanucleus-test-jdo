package org.datanucleus.test;

import java.util.*;
import org.junit.*;
import javax.jdo.*;

import static org.junit.Assert.*;
import mydomain.model.*;
import org.datanucleus.util.NucleusLogger;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();

            Person p1 = new Person(1, "First");
            Person p2 = new Person(2, "Second");
            Person p3 = new Person(3, "Third");
            Person p4 = new Person(4, "Fourth");
            Person p5 = new Person(5, "Fifth");
            pm.makePersistentAll(p1, p2, p3, p4, p5);

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        pmf.getDataStoreCache().evictAll();

        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();

            NucleusLogger.GENERAL.info(">> query for param.contains(field)");
            Query q = pm.newQuery("SELECT FROM " + Person.class.getName() + " WHERE :param.contains(this.name)");
            Map<String, Object> params = new HashMap<>();
            Collection<String> paramValue = new HashSet();
            paramValue.add("First");
            paramValue.add("Third");
            paramValue.add("Fifth");
            params.put("param", paramValue);
            q.setNamedParameters(params);
            NucleusLogger.GENERAL.info(">> q.execute");
            List<Person> results = q.executeList();
            for (Person p : results)
            {
                NucleusLogger.GENERAL.info(">> result p.id=" + p.getId() + " p.name=" + p.getName());
            }

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
