package unl.soc;

import unl.soc.items.Item;
import unl.soc.person.Manager;

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
