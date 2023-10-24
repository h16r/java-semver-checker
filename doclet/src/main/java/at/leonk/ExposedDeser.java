package at.leonk;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public record ExposedDeser(String path, String name, String type, String returnType, List<ExposedDeser> children) implements Serializable {

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
