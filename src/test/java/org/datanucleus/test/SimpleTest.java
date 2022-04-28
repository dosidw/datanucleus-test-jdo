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
        int numPersons = 10;
        List<Person> persons = new ArrayList<>();
        List<PersonData> personDatas = new ArrayList<>();
        
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            ent = new Enterprise(1, "Ent1");
            pm.makePersistent(ent);
            for (int i = 1; i <= numPersons; i++) {
                Person p = new Person(i, "Person" + i, ent);
                PersonData pd = new PersonData(i, "Data" + i, p);
                pm.makePersistent(p);
                pm.makePersistent(pd);
                persons.add(p);
                personDatas.add(pd);
            }
            tx.commit();
            
            pm.evictAll();
            
            assertNotNull(persons.get(0).getEnterprise());
            assertNotNull(personDatas.get(0).getPerson());
            
            //setup fetch group
            String fgName = "in-test-fg";
            String field = "person";
            FetchGroup fg = pm.getFetchGroup(PersonData.class, fgName);
            fg.addMember(field);
            fg.setRecursionDepth(field, 5);
            pm.getFetchPlan().addGroup(fgName);
            
            NucleusLogger.GENERAL.info(">> Starting query");
            //use in query:
            Query q = pm.newQuery(PersonData.class);
            Collection<PersonData> result = (Collection<PersonData>) q.execute();
            int count = 0;
            for (PersonData pd: result) {
                // should not trigger query:
                pd.getPerson().getName();
                count++;
            }
            
            NucleusLogger.GENERAL.info(">> Got num results: " + count);
            
            //cleanup fetch group
            pm.getFetchPlan().removeGroup(fgName);
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
