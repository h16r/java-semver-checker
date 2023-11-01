package at.leonk.semverchecker;

import java.util.ArrayList;
import java.util.List;

public class Checker {

    public static Report check(ExposedDeser from, ExposedDeser to) {

        return compare(from, to);
    }

    static Report compare(ExposedDeser from, ExposedDeser to) {

        List<Diff> diffs = new ArrayList<>();
        boolean isBreaking = false;

        if (!from.name().equals(to.name())) {
            isBreaking = true;
            diffs.add(new Diff(from.path(), "name changed from %s to %s".formatted(from.name(), to.name()), Diff.Type.UNKNOWN, Diff.Criticality.WARNING));
        }

        if ((from.type() != null && to.type() != null) && !from.type().equals(to.type())) {
            isBreaking = true;
            diffs.add(new Diff(from.path(), "type changed from %s to %s".formatted(from.name(), to.name()), Diff.Type.UNKNOWN, Diff.Criticality.WARNING));
        }

        for (int i = 0; i < from.children().size(); i++) {

            ExposedDeser current = from.children().get(i);

            if (to.children().size() > i) {

                Report childReport = compare(current, to.children().get(i));
                diffs.addAll(childReport.differences());
                isBreaking |= childReport.breaking();

            } else {


                if (current.name().equals("testWithParameters")) {
                    current.children().forEach(System.out::println);
                }


                diffs.add(Diff.Removed(current.path(), current.type(), current.name()));
                isBreaking = true;

            }

        }

        return new Report(isBreaking, diffs);

    }

}
