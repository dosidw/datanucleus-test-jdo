test-jdo
========

# contains creating OR-query example

If the collection used in a query as parameter is of a db object type, it seems to create OR instead of IN queries.
Neither the IN nor the OR queries are parameterized.

Parts of log for the OR queries:

	Parameter ParameterExpression{param} is being resolved as a literal, so the query is no longer precompilable
	Not caching the datastore compilation since some parameters are evaluated during the compilation and arent present in the final datastore-specific query
	SELECT 'mydomain.model.PersonData' AS DN_TYPE,A0.ID,A0."NAME" FROM PERSONDATA A0 WHERE (1 = A0.PERSON_ID_OID OR 2 = A0.PERSON_ID_OID OR 3 = A0.PERSON_ID_OID OR 4 = A0.PERSON_ID_OID)

# original:

Template project for any user testcase using JDO.
To create a DataNucleus test simply fork this project, and add/edit as 
necessary to add your model and persistence commands. The files that you'll likely need to edit are

* <a href="https://github.com/datanucleus/test-jdo/tree/master/src/main/java/mydomain/model">src/main/java/mydomain/model/</a>   **[Put your model classes here]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/main/resources/META-INF/persistence.xml">src/main/resources/META-INF/persistence.xml</a>   **[Put your datastore details in here]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/test/java/org/datanucleus/test/SimpleTest.java">src/test/java/org/datanucleus/test/SimpleTest.java</a>   **[Edit this if a single-thread test is required]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/test/java/org/datanucleus/test/MultithreadTest.java">src/test/java/org/datanucleus/test/MultithreadTest.java</a>   **[Edit this if a multi-thread test is required]**

To run this, simply type "mvn clean compile test"
