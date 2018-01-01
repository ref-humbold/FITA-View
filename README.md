# FITA-View
Finite and Infinite Tree Automata Viewer

----

## About
FITA-View is a system desired for visualizing workings of tree automata. Supported automata contain:
* Bottom-up deterministic finite tree automata
* Top-down deterministic finite tree automata
* Top-down non-deterministic finite tree automata

Trees and automata can be loaded from XML files with extensions *filename.tree.xml* for trees, *filename.bua.xml* for bottom-up automata and *filename.tda.xml* for top-down automata.

----

### Dependencies
FITA-View requires at least **[Java Standard Edition version 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)** installed. There are no dependencies on additional libraries.

For unit testing in FITA-View following libraries are required:
* JUnit 4
* Mockito 1.10.19 (or newer)
* PowerMock 1.7.1 (or newer)

----

### How to build?
FITA-View can be built with **[Apache ANT](http://ant.apache.org/)**. Possible targets are:
 * `ant` - same as `ant all`
 * `ant make` - compile only source files, create executable jar
 * `ant make-test` - compile source and tests files, create executable jar
 * `ant test` - run all tests
 * `ant javadocs` - generate Javadocs
 * `ant all` - compile source and tests files, run all tests and generate Javadocs

### How to run?
FITA-View can be run by an executable sh script:
```sh
$ /path/to/directory/FITA-View
```

Antoher way is to directly execute jar file:
```sh
$ java -jar path/to/directory/fita_view.jar
```

