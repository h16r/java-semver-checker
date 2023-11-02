# java-semver-checker

Check your library for [Semver](https://semver.org/) violations before releasing updates.

Heavily inspired by Rust's [cargo-semver-checks](https://github.com/obi1kenobi/cargo-semver-checks).

## Checker Implementation

1. Parse public APIs of previous and current library release
2. Check rules on diff according to desired semver increment
3. If violations are found, display them to library author

Todo: Replace List with Map; Signature as identifier

Signature=Object (Int==Long eg)
