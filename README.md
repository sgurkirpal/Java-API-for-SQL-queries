# cs305_2022

Submitter name: Gurkirpal Singh

Roll No.: 2019csb1087

Course: CS305

=================================


1. What does this program do?
    -
   - The program implements a Java based library for executing 
      SQL queries for create, read, update, delete (CRUD) operations 
      against an RDBMS. It provides an API which implements the following.
   - Reading the to be executed sql queries from an xml based configuration file.
   - The parameters for the SQL queries are dynamically populated at runtime from
   the supplied objects.
   - Population plain-old-java-objects (POJOs) from the results of SELECT queries that is
   returning the results of the select queries in the form requested by the user.
   - The program is tested on the MySql Sakila sample database
    

2. A description of how this program works (i.e. its logic)
    - 
   - The program/project is created using gradle and the unit tests are 
   implemented via JUnit.
   - The API structure of the library is defined by the interface named SqlRunner that is:
    ```
   interface SqlRunner {
        Object selectOne(String queryId, Object queryParam, Class resultType) ;
        List<?> selectMany(String queryId, Object queryParam, Class resultItemType);
        int update(String queryId, Object queryParam);
        int insert(String queryId, Object queryParam);
        int delete(String queryId, Object queryParam);
    }
    ```
   - A class named Library is implemented using this interface. 
   - The Sql queries which can be executed are Select, Update, Delete, Insert.
   - Sql queries are read from a file named config.xml.
   - After reading the queries, the queries are stored in a Hashmap to make them accessible by their ids.
   - The queries contain variables of the form ${...} whose values are passed by the user as an Object through
   the function queries. These values are put in the queries by using regex and replacing the varibles with these
   values.
   - If object is of primitive data type, all the variables are replaced with the same value as passed in the  queryParam
   - if object is an array type, all the variables are replaced with the same value as passed in the  queryParam.
   - A database connection is initialized on Sakila sample database using MySql username and password.
   - If the parameters passed are not of the object form i.e. if they are integer, string, array etc, then the
   parameters are typecasted into objects and the same value is assigned to all the variables.
   - Line coverage of 98%, Method coverage of 100% and Class coverage of 100%.
   - Query specific functions:
     - **Select**:
       - The function takes the following as arguments: queryID (id according to the xml file which has to be
       executed), paramQuery (parameters or values of the variables present in the query) and resultType (Class 
       of the object which the user want the results in). 
       - The query is then executed using the Sql API.
       - In the case of select One query, if result contains more than one row, then n exception is raised.
       - Similary, if there is no row in the result, nothing is printed and a warning is shown.
       - If the queryId passed as the argument is not of select type, then also an exception is raised and warning shown.
       - The results are then typecasted into the format requested by the user.
     - **Insert/Delete/Update**:
       - The function takes the following as arguments: queryID (id according to the xml file which has to be
       executed), paramQuery (parameters or values of the variables present in the query) and returns the number of rows
       of the database affected by the query.
       - The query is then executed using the Sql API.
       - If the queryId passed as the argument is of select type, then an exception is raised and warning shown.



3. How to compile and run this program
    -
   - It is a gradle library, so the project can be run with Intellij by clicking on Run with
   Coverage button.
   - The project is made using gradle, so the test files are run using:
   ```gradle test```
   - Or, It can be build with ```./gradlew build```
   - To test: ```./gradlew test```
   - For more information: ```gradle test --info```
   - For running particular tests: ```gradle test --tests your.package.TestClassName.particularMethod```

4. Provide a snapshot of a sample run
   -
   - Snapshots are attached in a folder named ScreenShots.

5. Github Link
   -
   https://github.com/sgurkirpal/cs305_2022


6. File Structure
   -
   C:.
   ├───.gradle
   │   ├───7.1
   │   │   ├───dependencies-accessors
   │   │   ├───executionHistory
   │   │   ├───fileChanges
   │   │   ├───fileHashes
   │   │   └───vcsMetadata-1
   │   ├───7.2
   │   │   ├───dependencies-accessors
   │   │   ├───executionHistory
   │   │   ├───fileChanges
   │   │   ├───fileHashes
   │   │   └───vcsMetadata-1
   │   ├───7.4
   │   │   ├───checksums
   │   │   ├───dependencies-accessors
   │   │   ├───executionHistory
   │   │   ├───fileChanges
   │   │   ├───fileHashes
   │   │   └───vcsMetadata
   │   ├───buildOutputCleanup
   │   ├───checksums
   │   └───vcs-1
   ├───.idea
   │   └───modules
   ├───gradle
   │   └───wrapper
   ├───lib
   │   ├───build
   │   │   ├───classes
   │   │   │   └───java
   │   │   │       ├───main
   │   │   │       │   └───cs305_2022
   │   │   │       └───test
   │   │   │           └───cs305_2022
   │   │   ├───generated
   │   │   │   └───sources
   │   │   │       ├───annotationProcessor
   │   │   │       │   └───java
   │   │   │       │       ├───main
   │   │   │       │       └───test
   │   │   │       └───headers
   │   │   │           └───java
   │   │   │               ├───main
   │   │   │               └───test
   │   │   ├───reports
   │   │   │   └───tests
   │   │   │       └───test
   │   │   │           ├───classes
   │   │   │           ├───css
   │   │   │           ├───js
   │   │   │           └───packages
   │   │   ├───test-results
   │   │   │   └───test
   │   │   │       └───binary
   │   │   └───tmp
   │   │       ├───compileJava
   │   │       ├───compileTestJava
   │   │       └───test
   │   └───src
   │       ├───main
   │       │   ├───java
   │       │   │   └───cs305_2022
   │       │   └───resources
   │       └───test
   │           ├───java
   │           │   └───cs305_2022
   │           └───resources
   └───ScreenShots