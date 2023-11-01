package at.leonk.semverchecker;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Main {
    public static void main(String[] args) {

        if (args.length < 2) {
            // checker/src/main/resources/
            System.err.println("usage of checker: java -jar <specV1> <specV2>");
        }

        check(args[0], args[1]);
    }

    static Report check(String firstSpec, String secondSpec) {

        ExposedDeser first = load(firstSpec);
        ExposedDeser second = load(secondSpec);

        return Checker.check(first, second);
    }


    private static ExposedDeser load(String fileName) {
        try (ObjectInputStream fileInputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ExposedDeser) fileInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}