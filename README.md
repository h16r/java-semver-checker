# java-semver-checker

Check your library for [Semver](https://semver.org/) violations before releasing updates.

Heavily inspired by Rust's [cargo-semver-checks](https://github.com/obi1kenobi/cargo-semver-checks) and
the [cargo book](https://doc.rust-lang.org/cargo/reference/semver.html#item-remove).

* [Usage](#usage)
* [Quick Start](#quick-start)
* [Rules](#rules)

## Usage

The semver-checker identifies breaking changes based on git-based differences in java sourcecode files. 

Currently the following operation modes are supported:

* maven-plugin (as part of the build process)
* maven-plugin (CLI-based)

## Quick Start

> An example with a more detailed explanation can be found [here](https://github.com/trpouh/jsc-example)

Clone the repository and install it via maven

```shell
git clone https://github.com/trpouh/java-semver-checker.git
cd java-semver-checker
mvn install
```

Find breaking changes in your sourcecode:

```shell
# refs can be commits, tags or branches
mvn semver-checker:check -Dbaseline.ref=<old> -Dcurrent.ref=<new>

# check against the main branch 
mvn semver-checker:check -Dbaseline.ref=main

# check two commits 
mvn semver-checker:check -Dbaseline.ref=204f884 -Dcurrent.ref=10d058a

# check commit against tag
mvn semver-checker:check -Dbaseline.ref=v4.0.0 -Dcurrent.ref=10d058a

# only check a specific package
mvn semver-checker:check \
  -Dbaseline.path=src/main/java/public \
  -Dbaseline.ref=main
```

To include the semver-check directly into your build process, simply add it to the  `build` tag in the `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>at.haesslerkirschner.semverchecker</groupId>
            <artifactId>semver-checker-maven-plugin</artifactId>
            <version>0.1.0</version>
            <configuration>
                <!-- optional, defaults to: . -->
                <baselinePath>.</baselinePath>
                <baselineBranch>main</baselineBranch>
                <!-- optional, defaults to: . (or, if provided, baselinePath) -->
                <currentPath>.</currentPath>
                <currentRef>feature</currentRef>
            </configuration>
            <executions>
                <execution>
                    <id>check-semver</id>
                    <goals>
                        <goal>check</goal>
                    </goals>
                    <phase>verify</phase>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Rules

As the goal of Semver is to indicate compatibility between library version changes, we can perform an automated check
whether changes made in a library's public API are allowed within the desired Semver version increment (major, minor,
patch). For example, removing a class would mandate a major version increment as projects depending on the class would
fail to
compile using the new version.

Below is a list of all rules along with their required version increment.

[//]: # (WARNING: Whenever renaming these section titles, check the rule implementations so that no links are broken.)

### Major: renaming/moving/removing any public elements

```java
// MAJOR CHANGE

///////////////////////////////////////////////////////////
// Before
public class Foo {
}

///////////////////////////////////////////////////////////
// After
// ... item has been removed

///////////////////////////////////////////////////////////
// Example usage that will break
public class Downstream {
    void doSomething() {
        var foo = new Foo(); // Cannot find class `Foo`
    }
}
```

### Minor: adding new public elements

```java
// MAJOR CHANGE

///////////////////////////////////////////////////////////
// Before
// ... absence of item

///////////////////////////////////////////////////////////
// After
public class Foo {
}

///////////////////////////////////////////////////////////
// Example use of the library that will safely work.
// `Foo` is not used since it didn't previously exist.
```

### Major: adding/removing method parameters

```java
// MAJOR CHANGE

///////////////////////////////////////////////////////////
// Before
public class Library {
    public void foo() {
    }
}

///////////////////////////////////////////////////////////
// After
public class Library {
    public void foo(String parameter) {
    }
}

///////////////////////////////////////////////////////////
// Example usage that will break.
public class Downstream {
    public void doSomething() {
        new Library().foo(); // Method requires 1 parameter but 0 are provided
    }
}
```

### Major: adding new enum values

Historically adding new enum values was a non-breaking operation because `switch` statements did not have to be
exhaustive, meaning not all values had to be matched. The newer `switch` _expressions_, however, have to be exhaustive,
which means if a new enum value is added then existing switch expressions stop to compile.

```java
// MAJOR CHANGE

///////////////////////////////////////////////////////////
// Before
public enum LibraryEnum {
    FOO
}

///////////////////////////////////////////////////////////
// After
public enum LibraryEnum {
    FOO,
    BAR
}

///////////////////////////////////////////////////////////
// Example usage that will break.
public class Downstream {
    public void doSomething(LibraryEnum libraryEnum) {
        var str = switch (libraryEnum) { // Non-exhaustive switch expression (without default case)
            case LibraryEnum.FOO -> "foo";
        };
    }
}
```