# java-semver-checker

Check your library for [Semver](https://semver.org/) violations before releasing updates.

Heavily inspired by Rust's [cargo-semver-checks](https://github.com/obi1kenobi/cargo-semver-checks) and the [cargo book](https://doc.rust-lang.org/cargo/reference/semver.html#item-remove).

## Rules

As the goal of Semver is to indicate compatibility between library version changes, we can perform an automated check
whether changes made in a library's public API are allowed within the desired Semver version increment (major, minor,
patch). For example, removing a class would mandate a major version increment as projects depending on the class would
fail to
compile using the new version.

Below is a list of all rules along with their required version increment.

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
    public void foo() {}
}

///////////////////////////////////////////////////////////
// After
public class Library {
    public void foo(String parameter) {}
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

## Checker Implementation

1. Parse public APIs of previous and current library release
2. Check rules on diff according to desired semver increment
3. If violations are found, display them to library author

Todo: Replace List with Map; Signature as identifier
