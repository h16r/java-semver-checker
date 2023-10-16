package at.leonk;

import java.io.Serializable;
import java.util.List;

public record ExposedDeser(String path, String name, String type, List<ExposedDeser> children) implements Serializable {

    @Override
    public String toString() {
        return "{"
                + "\"path\":\"" + path + "\""
                + ",\"name\":\"" + name + "\""
                + ",\"type\":\"" + type + "\""
                + ",\"children\":" + children
                + "}";
    }
}
