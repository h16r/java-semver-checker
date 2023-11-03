package at.leonk.semverchecker.checking;

public record Diff(String sourcePath, String message, Type type, Criticality criticality) {

    public static Diff Removed(String sourcePath, String sourceType, String sourceName) {
        return new Diff(sourcePath, "%s \"%s\" removed".formatted(sourceType.toLowerCase(), sourceName), Type.REMOVED, Criticality.BREAKING);
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