# FITA-View
Finite and Infinite Tree Automata Viewer

## About
FITA-View is a system desired for visualizing workings of tree automata. Supported automata:
+ Bottom-up deterministic finite tree automata
+ Top-down deterministic finite tree automata
+ Top-down non-deterministic finite tree automata

Trees and automata can be loaded from XML files with extensions *filename.tree.xml* for trees, *filename.bua.xml* for bottom-up automata and *filename.tda.xml* for top-down automata.

-----

## Dependencies

### Standard build & run
Build process:
+ [Java Standard Edition 8](https://www.oracle.com/technetwork/java/javase/overview/index.html)
+ [Apache ANT](http://ant.apache.org/)

### Unit testing
Additional libraries (automatically downloaded during build process):
+ JUnit 4.+
+ Mockito 2.+
+ PowerMock 1.7.+

-----

## How to build?
FITA-View can be built with **Apache ANT** using **Apache Ivy** to resolve all dependencies. Ivy and all libraries are downloaded during build, so make sure your Internet connection is working!

> Possible ANT targets are:
> + `ant` - same as `ant all`
> + `ant release` - resolve dependencies & compile source files & create executable jar
> + `ant test` - run all tests
> + `ant javadocs` - generate Javadoc
> + `ant all` - resolve dependencies & compile source and test files & create executable jar & run all tests & generate Javadoc
> + `ant refresh` - remove additional build files & resolve dependencies & compile source files & create executable jar
> + `ant refresh-all` - remove additional build files & resolve dependencies & compile source and test files & create executable jar & run all tests & generate Javadoc

## How to run?
FITA-View can be run by the executable *sh* script in the `dist` directory:
```sh
$ sh /path-to-directory/dist/FITA-View
```

Alternatively one may directly execute the *jar* file in the `dist` directory:
```sh
$ java -jar /path-to-directory/dist/fitaview-{version}.jar
```
