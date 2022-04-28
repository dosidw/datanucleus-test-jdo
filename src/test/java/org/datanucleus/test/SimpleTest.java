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
        Person[] persons = new Person[5];
        try
        {
            tx.begin();

            persons[0] = new Person(1, "First");
            persons[1] = new Person(2, "Second");
            persons[2] = new Person(3, "Third");
            persons[3] = new Person(4, "Fourth");
            persons[4] = new Person(5, "Fifth");
            pm.makePersistentAll(persons);
            
            int idx = 1;
            for (Person p: persons) {
                PersonData pd = new PersonData(idx, "Data" + idx, persons[idx - 1]);
                pm.makePersistent(pd);
                idx++;
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
//            pm.close();
        }
//        pmf.getDataStoreCache().evictAll();
//
//        pm = pmf.getPersistenceManager();
//        tx = pm.currentTransaction();
        try
        {
            //tx.begin();

            //in but not parametrized:
//            NucleusLogger.GENERAL.info(">> query for param.contains(field)");
//            Query q = pm.newQuery("SELECT FROM " + Person.class.getName() + " WHERE :param.contains(this.name)");
//            Map<String, Object> params = new HashMap<>();
//            Collection<String> paramValue = new HashSet();
//            paramValue.add("First");
//            paramValue.add("Third");
//            paramValue.add("Fifth");
//            params.put("param", paramValue);
//            q.setNamedParameters(params);
//            NucleusLogger.GENERAL.info(">> q.execute");
//            List<Person> results = q.executeList();
            
            
            //in but not parametrized:
//            NucleusLogger.GENERAL.info(">> query for param.contains(this.name)");
//            Query q = pm.newQuery(Person.class, "param.contains(this.name)");
//            q.declareParameters("java.util.Collection param");
//            List<String> paramValue = new ArrayList<>();
//            paramValue.add("First");
//            paramValue.add("Third");
//            paramValue.add("Fifth");
//            paramValue.add("xyz");
//            paramValue.add("abs");
//            paramValue = paramValue.subList(0, 4);
//            NucleusLogger.GENERAL.info(">> q.execute");
//            Collection<Person> results = (Collection<Person>) q.execute(paramValue);
//            for (Person p : results)
//            {
//                NucleusLogger.GENERAL.info(">> result p.id=" + p.getId() + " p.name=" + p.getName());
//            }
            
            //OR query and not parametrized:
            NucleusLogger.GENERAL.info(">> query for param.contains(this.person)");
            Query q = pm.newQuery(PersonData.class, "param.contains(this.person)");
            q.declareParameters("java.util.Collection param");
            List<Person> paramValue = Arrays.asList(persons);
            paramValue = paramValue.subList(0, 4);
            NucleusLogger.GENERAL.info(">> q.execute");
            Collection<PersonData> results = (Collection<PersonData>) q.execute(paramValue);
            for (PersonData p : results)
            {
                NucleusLogger.GENERAL.info(">> result p.id=" + p.getId() + " p.name=" + p.getName());
            }

            //tx.commit();
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
