test-jdo
========

# dosi-issue-03_second_query_slower

Getting the same data again is much slower when using `fetch-fk-only`/`recursion-depth=0`

It seems to get data for fields that it should not (possible by separate queries).

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
