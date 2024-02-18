package unl.soc;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import unl.soc.items.Item;
import unl.soc.person.Manager;

@XStreamAlias("Store")
public class Store {
    private String storeCode;
    private Manager manager;
    private Address address;
    private Item items;

    public Store(String storeCode, Address address, Manager manager) {
        this.storeCode = storeCode;
        this.manager = manager;
        this.address = address;
    }
}
