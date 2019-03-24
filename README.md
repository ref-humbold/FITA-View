# FITA-View
Finite and Infinite Tree Automata Viewer

----

## About
FITA-View is a system desired for visualizing workings of tree automata. Supported automata:
+ Bottom-up deterministic finite tree automata
+ Top-down deterministic finite tree automata
+ Top-down non-deterministic finite tree automata

Trees and automata can be loaded from XML files with extensions *filename.tree.xml* for trees, *filename.bua.xml* for bottom-up automata and *filename.tda.xml* for top-down automata.

----

## Dependencies
FITA-View requires at least **[Java Standard Edition version 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)** installed. There are no dependencies on additional libraries.

For unit testing of FITA-View following libraries are required. They are automatically downloaded during build process:
+ JUnit 4.12
+ Mockito 1.10.19
+ PowerMock 1.7.1

----

## How to build?
FITA-View can be built with **[Apache ANT](http://ant.apache.org/)** using **[Apache Ivy](http://ant.apache.org/ivy/)** to resolve all dependencies. Ivy and all libraries are downloaded during build, so make sure your Internet connection is working! Possible targets are:
+ `ant` - same as `ant all`
+ `ant release` - resolve dependencies & compile source files & create executable jar
+ `ant test` - run all tests
+ `ant javadocs` - generate Javadoc
+ `ant all` - resolve dependencies & compile source and test files & create executable jar & run all tests & generate Javadoc

## How to run?
FITA-View can be run by the executable *sh* script:
```sh
$ sh /path/to/directory/FITA-View
```

Antoher way is to directly execute the *jar* file:
```sh
$ java -jar path/to/directory/fitaview-{version}.jar
```

