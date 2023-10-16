package at.leonk;

import java.io.*;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        ExposedDeser first = load("specV1");
        System.out.println("first = " + first);
        ExposedDeser second = load("specV2");
        //System.out.println("second = " + second);

        System.out.println("Checker.check(second, first) = " + Checker.check(first, second));


    }

    private static ExposedDeser load(String fileName) {
        try (ObjectInputStream fileInputStream = new ObjectInputStream(new FileInputStream("checker/src/main/resources/" + fileName))) {
            return (ExposedDeser) fileInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}