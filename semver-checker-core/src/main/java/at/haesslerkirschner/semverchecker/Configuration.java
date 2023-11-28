package at.haesslerkirschner.semverchecker;

import at.haesslerkirschner.semverchecker.source.FileSource;

public record Configuration(FileSource baseline, FileSource current) {
}
