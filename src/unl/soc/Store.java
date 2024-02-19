package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import unl.soc.items.Item;
import unl.soc.person.Manager;

@XStreamAlias("store")
public class Store {
    @Expose
    private String storeCode;
    @Expose
    private Manager manager;
    @Expose
    private Address address;
    @Expose
    private Item items;

    public Store(String storeCode, Address address, Manager manager) {
        this.storeCode = storeCode;
        this.manager = manager;
        this.address = address;
    }

}
