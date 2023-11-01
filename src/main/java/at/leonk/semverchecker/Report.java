package at.leonk.semverchecker;

import java.util.List;

public record Report(boolean breaking, List<Diff> differences) {

}
