package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Item class is an abstract class representing an item.
 * It includes fields for unique code, name, tax, total price, and stores available.
 * It includes Getters, ToString, HashCode and Equals methods
 */
public abstract class Item {
    @Expose
    private String uniqueCode;
    @Expose
    private String name;

    @XStreamOmitField
    private final List<Store> storesAvailable;

    public Item(String uniqueCode, String name) {
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.storesAvailable = new ArrayList<>();
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

    public abstract double getGrossPrice();

    public abstract double getTotalTax();

    public double getNetPrice() {
        return getGrossPrice() + getTotalTax();
    }

    public String toString() {
        StringBuilder item = new StringBuilder(String.format("Item{" +
                "\n  Unique identifier: " + uniqueCode +
                "\n  Name: " + name +
                "\n  Total tax: $" + getTotalTax() +
                "\n  Total price: $" + getNetPrice()));
        for (Store store : this.storesAvailable) {
            item.append("\n  Stores available: ").append(store);
        }
        return item + "\n}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, name, getTotalTax(), getNetPrice(), storesAvailable);
    }
}