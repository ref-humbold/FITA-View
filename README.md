# FITA-View

![Release](https://img.shields.io/github/v/release/ref-humbold/FITA-View?style=plastic)
![License](https://img.shields.io/github/license/ref-humbold/FITA-View?style=plastic)

Finite and Infinite Tree Automata Viewer

## About

FITA-View is a system desired for visualizing workings of tree automata. It supports the
following types of automata:

+ Bottom-up deterministic finite tree automata
+ Top-down deterministic finite tree automata
+ Top-down non-deterministic finite tree automata

Trees and automata can be loaded from XML files with extensions *filename.tree.xml* for trees,
*filename.bua.xml* for bottom-up automata and *filename.tda.xml* for top-down automata.

-----

## System requirements

> versions used by the author are in and italics

+ Operating system \
  *Debian testing*
+ [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html) \
  *APT package `openjdk-11-jdk`, version 11 SE*
+ [Apache ANT](http://ant.apache.org/) \
  *APT package `ant`, version 1.10.+*

## Dependencies

> dependencies are automatically downloaded during build process

+ JUnit 4.+
+ Mockito 2.+
+ PowerMock 2.+

-----

## How to build?

FITA-View can be built with **Apache ANT** using **Apache Ivy** to resolve all dependencies.
Ivy itself and all dependencies are downloaded during build, so make sure your Internet
connection is working!

Possible ANT targets are:

+ `ant`, `ant build` - resolve dependencies & compile source files & create executable jar & run
  all tests
+ `ant resolve` - resolve dependencies
+ `ant jar` - compile source files & create executable jar
+ `ant test` - run all tests
+ `ant docs` - generate Javadoc
+ `ant clean` - remove additional build files
+ `ant rebuild` - remove additional build files & resolve dependencies & compile source files &
  create executable jar & run all tests

## How to run?

FITA-View can be run by the executable *sh* script in the `antBuild` directory:

```sh
$ sh /path/to/project/directory/antBuild/fita-view
```

Alternatively one may directly execute the *jar* file in the `antBuild/dist` directory:

```sh
$ java -jar /path/to/project/directory/antBuild/dist/fitaview-{version}.jar
```
