package at.leonk.semverchecker;

public record Diff(String sourcePath, String message, Type type, Criticality criticality) {

    public static Diff Removed(String sourcePath, String sourceType, String sourceName) {
        return new Diff(sourcePath, "%s \"%s\" removed".formatted(sourceType.toLowerCase(), sourceName), Type.REMOVED, Criticality.BREAKING);
    }

    public static Diff Renamed(String sourcePath, java.lang.reflect.Type sourceType, String sourceName, String targetName) {
        return null;
    }

    enum Criticality {
        BREAKING,
        WARNING,
        INFO
    }

    enum Type {
        UNKNOWN,
        REMOVED,
        RENAMED,
    }

}