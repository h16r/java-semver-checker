package at.haesslerkirschner.semverchecker.parsing;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.function.Consumer;

public class OldApiParserDoclet extends ApiParserDoclet {
    static Consumer<Set<Element>> baselineApiConsumer;

    @Override
    protected Consumer<Set<Element>> getElementConsumer() {
        return baselineApiConsumer;
    }
}
