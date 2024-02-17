package unl.soc.items;

import unl.soc.Store;

import java.util.List;

public class Item {
    private String uniqueCode;
    private String name;
    private double basePrice;
    private double tax;
    private double totalPrice;
    private List<Store> storesAvailable;

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTax() {
        return tax;
    }
}
