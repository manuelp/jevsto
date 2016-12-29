# jevsto

Java-based Event Store for implementing CQRS and Event Sourcing.

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
