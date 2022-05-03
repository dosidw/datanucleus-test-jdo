package org.datanucleus.test;

import java.util.*;
import org.junit.*;
import javax.jdo.*;

import static org.junit.Assert.*;
import mydomain.model.*;
import org.datanucleus.util.NucleusLogger;

public class GetsFetchFkOnlyAgainAfterEvictTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        //int numPersons = 10_000;
        int numPersons = 3;
        Enterprise ent = null;
        List<Person> persons = new ArrayList<>();
        List<PersonData> personDatas = new ArrayList<>();
        
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            for (int i = 1; i <= numPersons; i++) {
                if (i < 3 || i % 5 == 0) {
                    ent = new Enterprise(i, "Ent" + i);
                    pm.makePersistent(ent);
                }
                Person p = new Person(i, "Person" + i, ent);
                PersonData pd = new PersonData(i, "Data" + i, p, ent);
                pm.makePersistent(p);
                pm.makePersistent(pd);
                persons.add(p);
                personDatas.add(pd);
            }
            tx.commit();
            
            NucleusLogger.GENERAL.info(">> Test init done.");
            
            NucleusLogger.GENERAL.info(">> Starting query0");
            Query qp = pm.newQuery(Person.class);
            Query q = pm.newQuery(PersonData.class);
            long start = System.currentTimeMillis();
            personDatas.clear();
            persons.clear();
            persons.addAll((Collection<Person>) qp.execute());
            Collection<PersonData> result = (Collection<PersonData>) q.execute();
            int count = 0;
            for (PersonData pd: result) {
            	personDatas.add(pd);
                // should not trigger query:
                pd.getPerson().getName();
                count++;
            }
            qp.closeAll();
            q.closeAll();
            long duration = System.currentTimeMillis() - start;
            NucleusLogger.GENERAL.info(">> Query0 duration: " +duration);
            NucleusLogger.GENERAL.info(">> Got num results: " + count);
            
            //pm.evictAll();
            //our level 1 cache leads to error if using a new PM, why?
            // is execution context recycled, so it will reuse our level 1 cache? I see a clear() call but no ctor in log
            pm.close();
            pm = pmf.getPersistenceManager();
            
            ent = null;
            persons.clear();
            personDatas.clear();
            
            NucleusLogger.GENERAL.info(">> Starting query1");
            q = pm.newQuery(PersonData.class);
            qp = pm.newQuery(Person.class);
            start = System.currentTimeMillis();
            personDatas.clear();
            persons.clear();
            persons.addAll((Collection<Person>) qp.execute());
            result = (Collection<PersonData>) q.execute();
            count = 0;
            for (PersonData pd: result) {
                personDatas.add(pd);
                // should not trigger query:
                pd.getPerson().getName();
                count++;
            }
            qp.closeAll();
            q.closeAll();
            duration = System.currentTimeMillis() - start;
            NucleusLogger.GENERAL.info(">> Query1 duration: " +duration);
            NucleusLogger.GENERAL.info(">> Got num results: " + count);
            
            pm.evictAll();
            
            // HERE IT WILL RETRIEVE ALL fetch-fk-only again, even Persons are in cache already
            NucleusLogger.GENERAL.warn(">> HERE IT WILL RETRIEVE ALL fetch-fk-only again, even Persons are in cache already");
            
            NucleusLogger.GENERAL.info(">> Starting query2");
            q = pm.newQuery(PersonData.class);
            qp = pm.newQuery(Person.class);
            start = System.currentTimeMillis();
            personDatas.clear();
            persons.clear();
            persons.addAll((Collection<Person>) qp.execute());
            result = (Collection<PersonData>) q.execute();
            count = 0;
            for (PersonData pd: result) {
                personDatas.add(pd);
                // should not trigger query:
                pd.getPerson().getName();
                count++;
            }
            qp.closeAll();
            q.closeAll();
            duration = System.currentTimeMillis() - start;
            NucleusLogger.GENERAL.info(">> Query2 duration: " +duration);
            NucleusLogger.GENERAL.info(">> Got num results: " + count);
            
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
