package unl.soc;

public class Store {
    private String storeCode;
    private Person manager;
    private Address address;

    public Store(String storeCode, Person manager, Address address) {
        this.storeCode = storeCode;
        this.manager = manager;
        this.address = address;
    }
}
