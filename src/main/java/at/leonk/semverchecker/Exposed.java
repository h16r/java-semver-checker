package at.leonk.semverchecker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Exposed (Exposed parent, String type, String name, int hash, List<Exposed> children) implements Serializable {

    Exposed() {
        this(null, null, "root", "root".hashCode(), new ArrayList<>());
    }

    Exposed(Exposed parent, String name, String type){
        this(parent, type, name, name.hashCode(), new ArrayList<>());
        parent.children.add(this);
    }

    String absolutePath() {

        String path = Optional.ofNullable(parent)
                .map(parent -> parent.absolutePath() + ":")
                .orElse("");

        return path + name;

    }

    @Override
    public String toString() {
        return "{"
                + "\"parent\":" + "\"" + Optional.ofNullable(parent).map(Exposed::name).orElse("null") + "\","
                + "\"type\":" + "\""  + type + "\""
                + ",\"name\":\"" + name + "\""
                + ",\"hash\":\"" + hash + "\""
                + ",\"children\":" + children
                + "}";
    }
}
