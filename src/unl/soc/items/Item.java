package unl.soc.items;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import unl.soc.Store;

import java.util.List;

@XStreamAlias("item")
public class Item {
    @Expose
    private String uniqueCode;
    @Expose
    private String name;
    private String type;
    private double tax;
    private double totalPrice;
    private List<Store> storesAvailable;

    public Item(String uniqueCode, String type, String name) {
        this.uniqueCode = uniqueCode;
        this.type = type;
        this.name = name;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public String getName() {
        return name;
    }

    public List<Store> getStoresAvailable() {
        return storesAvailable;
    }

    public String getType() {
        return type;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTax() {
        return tax;
    }
}
