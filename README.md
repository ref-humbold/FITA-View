# FITA-View
Finite and Infinite Tree Automata Viewer

### **How to build?**
FITA-View can be built with Apache ANT.
Possible targets are:
 * `ant` same as `ant all`
 * `ant make` compile only source files
 * `ant make-test` compile source and tests files
 * `ant jar` compile only source files and create executable jar
 * `ant test` run all tests
 * `ant javadocs` generate Javadocs
 * `ant all` compile source and tests files, create executable jar, run all tests and generate Javadocs

### **How to run?**
FITA-View can be run directly from JAR:
~~~sh
$ java -jar path/to/directory/fita_view.jar
~~~

Moreover this can be done with an executable sh script:
~~~sh
$ /path/to/directory/FITA-View
~~~
