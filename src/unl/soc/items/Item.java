package unl.soc.items;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import unl.soc.Store;
import java.util.List;
import java.util.Objects;

public class Item {
    @Expose
    private String uniqueCode;
    @Expose
    private String name;
    @XStreamOmitField
    private double tax;
    @XStreamOmitField
    private double totalPrice;
    @XStreamOmitField
    private List<Store> storesAvailable;

    public Item(String uniqueCode, String name) {
        this.uniqueCode = uniqueCode;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTax() { return tax; }

    public String toString() {
        StringBuilder item = new StringBuilder(String.format("Item{" +
                "\n  Unique identifier: " + uniqueCode +
                "\n  Name: " + name +
                "\n  Total tax: $" + tax +
                "\n  Total price: $" + totalPrice));
        for (Store store : this.storesAvailable) {
            item.append("\n  Stores available: ").append(store);
        }
        return item + "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(tax, item.tax) == 0 && Double.compare(totalPrice, item.totalPrice) == 0 && Objects.equals(uniqueCode, item.uniqueCode) && Objects.equals(name, item.name) && Objects.equals(storesAvailable, item.storesAvailable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, name, tax, totalPrice, storesAvailable);
    }
}