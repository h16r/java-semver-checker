package at.leonk;

import java.util.ArrayList;
import java.util.List;

public record Exposed (String type, String name, int hash, List<Exposed> children) {

    Exposed() {
        this(null, "root", "root".hashCode(), new ArrayList<>());
    }

    Exposed(String name){
        this(null, name, name.hashCode(), new ArrayList<>());
    }

    Exposed(String name, String type){
        this(type, name, name.hashCode(), new ArrayList<>());
    }

    Exposed addChild(Exposed exposed) {
        children.add(exposed);
        return this;
    }

    @Override
    public String toString() {
        return "{\n"
                + "    \"type\":" + "\""  + type + "\"\n"
                + ",     \"name\":\"" + name + "\"\n"
                + ",     \"hash\":\"" + hash + "\"\n"
                + ",     \"children\":" + children + "\n"
                + "}";
    }
}
