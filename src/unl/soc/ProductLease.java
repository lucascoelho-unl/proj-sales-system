package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.Objects;

/**
 * The ProductLease class represents a leased product.
 * It extends the Item class and includes fields for total lease time,
 * price, first month price, and markup price.
 * It includes Getters, ToString, HashCode and Equals methods
 */
@XStreamAlias("productLease")
public class ProductLease extends Item {
    @XStreamOmitField
    private double totalMonths;
    @Expose
    private double price;

    public ProductLease(String uniqueCode, String name, double basePrice) {
        super(uniqueCode, name);
        this.price = basePrice;
    }

    public double getTotalTime() {
        return totalMonths;
    }

    public double getBasePrice() {
        return price;
    }

    public double getMarkupPrice() {
        return getBasePrice() / 2;
    }

    public double getFirstMonthPrice() {
        return getMarkupPrice() / getTotalTime();
    }

    @Override
    public double getGrossPrice() {
        return getBasePrice() + getMarkupPrice();
    }

    @Override
    public double getTotalTax() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Product Lease{" +
                "\n  Unique identifier: " + getUniqueCode() +
                "\n  Name: " + getName() +
                "\n  Total lease time: " + getTotalTime() +
                "\n  First month price: $%.2f" +
                "\n  Markup price: $%.2f" +
                "\n  Total tax: $%.2f" +
                "\n  Total upfront price: $%.2f" +
                "\n}", Math.round(getFirstMonthPrice() * 100) / 100, Math.round(getMarkupPrice() * 100) / 100, Math.round(getTotalTax() * 100) / 100, Math.round(getNetPrice() * 100) / 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductLease that = (ProductLease) o;
        return Double.compare(price, that.price) == 0 && Double.compare(getGrossPrice(), that.getGrossPrice()) == 0 && Double.compare(getMarkupPrice(), that.getMarkupPrice()) == 0 && Objects.equals(getTotalTime(), that.getTotalTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTotalTime(), price, getGrossPrice(), getMarkupPrice());
    }
}