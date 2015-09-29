# jevsto

Java-based Event Store for implementing CQRS and Event Sourcing.

## Changelog

### 0.1.0

* Changed `Event` predicates from `F<A, B>` to `Func1<A, B>` since they are generally used to filter and event stream.

### 0.0.3

* Added predicate `Event#isOfType(EventType)`.
* Added `EventConstructor` static factory method.
* Added `EventDescriptor` type to encapsulate `EventType` and reading and writing of `Event`s.

### 0.0.2

* Changed group name and LICENSE (to MPL 2.0)

### 0.0.1

First public stable release. Basic functionality and data types.

## Install

Artifacts can be found on [JitPack](https://jitpack.io/#manuelp/jevsto). [Basically](https://jitpack.io/docs/), you just need to add JitPack repo and add the dependency. Using [Gradle](http://gradle.org/) as an example:

```groovy
allprojects {
  repositories { 
    jcenter()
      maven { url "https://jitpack.io" }
  }
}

dependencies {
  compile 'com.github.manuelp:jevsto:[X.Y.Z]'
}
```
