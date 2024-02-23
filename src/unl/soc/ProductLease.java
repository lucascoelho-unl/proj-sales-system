package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The ProductLease class represents a leased product.
 * It extends the Item class and includes fields for total lease time,
 * price, first month price, and markup price.
 * It includes Getters, ToString, HashCode and Equals methods
 */
@XStreamAlias("productLease")
public class ProductLease extends Item{
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

    public double getPrice() {
        return price;
    }

    public double getMarkupPrice(){
        return price / 2;
    }

    @Override
    public double getGrossPrice() {
        return (price / 2) * totalMonths;
    }

    @Override
    public double getTotalTax() {
        return 0;
    }

    @Override
    public double getNetPrice() {
        return (price / 2) * totalMonths + getTotalTax();
    }

    @Override
    public String toString() {
        return "Product Lease{" +
                "\n  Unique identifier: " + getUniqueCode() +
                "\n  Name: " + getName() +
                "\n  Total lease time: " + getTotalTime() +
                "\n  First month price: $" + Math.round(getGrossPrice() * 100) / 100 +
                "\n  Markup price: $" + Math.round((price / 2) * 100)/100 +
                "\n  Total tax: $" + Math.round(getTotalTax() * 100) / 100 +
                "\n  Total upfront price: $" + Math.round(getNetPrice() * 100) / 100 +
                "\n}";
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