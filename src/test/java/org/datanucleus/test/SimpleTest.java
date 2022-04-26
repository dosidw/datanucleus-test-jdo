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

        Enterprise ent = null;
        Person pers = null;
        
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            ent = new Enterprise(1, "Ent1");
            pm.makePersistent(ent);
            for (int i = 1; i < 10001; i++) {
                pers = new Person(i, "Person" + i, ent);
                pm.makePersistent(pers);
            }
            tx.commit();
            
            //pm.evict(ent);
            //pm.evict(pers);
            pm.evictAll();
            
            // fails if fetch-fk-only and no level 2 cache!!!!!
            assertNotNull(pers.getEnterprise());

            //if I do this, then query will fail with: javax.jdo.JDOUserException: Exception thrown while loading remaining rows of query
            //java.lang.NullPointerException at org.datanucleus.state.StateManagerImpl.getLoadedFields(StateManagerImpl.java:1658)
            //pm.close();
            
            
            //not needed, fails above already
//            pm = pmf.getPersistenceManager();
//            Query q = pm.newQuery(Person.class);
//            Collection<Person> persons = (Collection<Person>) q.execute();
//            for (Person p: persons) {
//                assertNotNull(p.getEnterprise());
//            }
//            q.closeAll();
        }
        catch (Throwable thr)
        {
            System.out.println("//////////////////////////// Exception " + thr + " ////////////////");
            thr.printStackTrace();
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            if (pm != null)
            {
                pm.close();
            }
        }
        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
