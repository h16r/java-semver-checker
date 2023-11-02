package at.leonk.semverchecker.parsing;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.function.Consumer;

public class NewApiParserDoclet extends ApiParserDoclet {
    static Consumer<Set<Element>> newApiConsumer;

    @Override
    protected Consumer<Set<Element>> getElementConsumer() {
        return newApiConsumer;
    }
}