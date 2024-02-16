package unl.soc;

public class Store {
    private String storeCode;
    private Manager manager;
    private Address address;
    private Item items;

    public Store(String storeCode, Manager manager, Address address) {
        this.storeCode = storeCode;
        this.manager = manager;
        this.address = address;
    }
}
