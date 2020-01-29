# FITA-View
![Release](https://img.shields.io/github/v/release/ref-humbold/FITA-View?style=plastic)
![License](https://img.shields.io/github/license/ref-humbold/FITA-View?style=plastic)

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
> *versions used by the author are in double parentheses and italic*

Build process:
+ operating system *((Debian testing))*
+ [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html) *((Standard Edition 11))*
+ [Apache ANT](http://ant.apache.org/) *((1.10.+))*

### Unit testing
> libraries are automatically downloaded during build process

+ JUnit 4.+
+ Mockito 2.+
+ PowerMock 2.+

-----

## How to build?
FITA-View can be built with **Apache ANT** using **Apache Ivy** to resolve all dependencies. Ivy and all libraries are downloaded during build, so make sure your Internet connection is working!

> Possible ANT targets are:
> + `ant` - same as `ant all`
> + `ant build` - compile source files & create executable jar
> + `ant rebuild` - resolve dependencies & compile source files & create executable jar
> + `ant test` - run all tests
> + `ant docs` - generate Javadoc
> + `ant main` - compile source and test files & create executable jar & run all tests
> + `ant all` - resolve dependencies & compile source and test files & create executable jar & run all tests
> + `ant refresh` - remove additional build files & resolve dependencies & compile source files & create executable jar
> + `ant refresh-main` - remove additional build files & compile source and test files & create executable jar & run all tests
> + `ant refresh-all` - remove additional build files & resolve dependencies & compile source and test files & create executable jar & run all tests

## How to run?
FITA-View can be run by the executable *sh* script in the `dist` directory:
```sh
$ sh /path-to-project-directory/dist/FITA-View
```

Alternatively one may directly execute the *jar* file in the `dist` directory:
```sh
$ java -jar /path-to-project-directory/dist/fitaview-{version}.jar
```
