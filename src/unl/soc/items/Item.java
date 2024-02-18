package unl.soc.items;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import unl.soc.Store;

import java.util.List;

@XStreamAlias("item")
public class Item {
    private String uniqueCode;
    private String name;
    private String type;
    private double basePrice;
    private double tax;
    private double totalPrice;
    private List<Store> storesAvailable;

    public Item(String uniqueCode, String type, String name, double basePrice) {
        this.uniqueCode = uniqueCode;
        this.type = type;
        this.name = name;
        this.basePrice = basePrice;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
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
