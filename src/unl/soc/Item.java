package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.Objects;

/**
 * The Item class is an abstract class representing an item.
 * It includes fields for unique code, name, tax, total price, and stores available.
 * It includes Getters, ToString, HashCode and Equals methods
 */
public abstract class Item implements Priceable {
    @Expose
    private String uniqueCode;
    @Expose
    private String name;
    @Expose
    private double basePrice;
    @XStreamOmitField
    private String itemType;

    public Item(String uniqueCode, String itemType, String name, double basePrice) {
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.basePrice = basePrice;
        this.itemType = itemType;
    }

    public double getBasePrice() {
        return this.basePrice;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public String getName() {
        return name;
    }

    public String getItemType() {
        return itemType;
    }

    public final double getNetPrice() {
        return getGrossPrice() + getTotalTax();
    }

    public String toString() {
        String item = String.format("Item{" +
                "\n  Unique identifier: " + uniqueCode +
                "\n  Name: " + name +
                "\n  Total tax: $" + getTotalTax() +
                "\n  Total price: $" + getNetPrice());
        return item + "\n}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, name, getTotalTax(), getNetPrice());
    }
}