package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;
import java.util.Objects;

@XStreamAlias("store")
public class Store {
    @Expose
    private String storeCode;
    @Expose
    private Manager manager;
    @Expose
    private Address address;
    @Expose
    private List<Item> items;

    public Store(String storeCode, Address address, Manager manager) {
        this.storeCode = storeCode;
        this.manager = manager;
        this.address = address;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public Manager getManager() {
        return manager;
    }

    public Address getAddress() {
        return address;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Store{" +
                "\n  Store code: " + storeCode +
                "\n  Manager: " + manager +
                "\n  Address: " + address +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(storeCode, store.storeCode) && Objects.equals(manager, store.manager) && Objects.equals(address, store.address) && Objects.equals(items, store.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeCode, manager, address, items);
    }
}