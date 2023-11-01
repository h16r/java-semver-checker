package v1;

public class PublicAPI {

    private String field;

    public PublicAPI(String field) {
        this.field = field;
    }

    PublicAPI() {

    }

    public void test() {
    }

    public int testWithParameters(int abc) {
        return 10 * abc;
    }

    public void test(String test) {

    }
}
