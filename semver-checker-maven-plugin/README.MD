## java-semver-checker-plugin

This plugin is a wrapper for the [java-semver-checker](https://github.com/trpouh/java-semver-checker). It can be used to
detect semver violations directly in your build pipeline.

### usage

Include it in the plugin section of your pom.xml:

```xml

<project xmlns="http://maven.apache.org/POM/4.0.0">
    <build>
        <plugins>
            <plugin>
                
            </plugin>
        </plugins>
    </build>
</project>
```

```shell
BASELINE_PATH=/Users/leonkirschner/Documents/work/os/java-semver-checker/src/test/resources/test-projects/class-missing/baseline
CURRENT_PATH=/Users/leonkirschner/Documents/work/os/java-semver-checker/src/test/resources/test-projects/class-missing/current

mvn at.haesslerkirschner.semverchecker:plugin:0.1.0:check \
    -Dbaseline.path=$BASELINE_PATH \
    -Dcurrent.path=$CURRENT_PATH
```