package at.leonk;

import java.util.List;

public record Report(boolean breaking, List<Diff> differences) {

}
