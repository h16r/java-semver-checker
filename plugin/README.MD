## usage

```shell
BASELINE_PATH=/Users/leonkirschner/Documents/work/os/java-semver-checker/src/test/resources/test-projects/class-missing/baseline
CURRENT_PATH=/Users/leonkirschner/Documents/work/os/java-semver-checker/src/test/resources/test-projects/class-missing/current

mvn at.leonk.semver-checker:plugin:0.1.0:check \
    -Dbaseline.path=$BASELINE_PATH \
    -Dcurrent.path=$CURRENT_PATH
```