test-jdo
========

# dosi-issue-03_second_query_slower

Getting the same data again is much slower when using `fetch-fk-only`/`recursion-depth=0`

After an object is evicted, a query for that object will load it. But access to FK fields will trigger a query loading the just retrieved FKs again.

This is related to our custom Level 1 cache that does nothing on `clear()`.

NOTE: the test closes the PM, remove that to run without changes in DN. Closing the DN seems to be a problem with our custom Level 1 cache.
The cache is recycled for the next PM (ExecutionContextImpl), but our cache does nothing on `clear()`

To fix that change ExecutionContextImpl:

`cache.clear();`

to:

`initialiseLevel1Cache();`

# original:

Template project for any user testcase using JDO.
To create a DataNucleus test simply fork this project, and add/edit as 
necessary to add your model and persistence commands. The files that you'll likely need to edit are

* <a href="https://github.com/datanucleus/test-jdo/tree/master/src/main/java/mydomain/model">src/main/java/mydomain/model/</a>   **[Put your model classes here]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/main/resources/META-INF/persistence.xml">src/main/resources/META-INF/persistence.xml</a>   **[Put your datastore details in here]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/test/java/org/datanucleus/test/SimpleTest.java">src/test/java/org/datanucleus/test/SimpleTest.java</a>   **[Edit this if a single-thread test is required]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/test/java/org/datanucleus/test/MultithreadTest.java">src/test/java/org/datanucleus/test/MultithreadTest.java</a>   **[Edit this if a multi-thread test is required]**

To run this, simply type "mvn clean compile test"
