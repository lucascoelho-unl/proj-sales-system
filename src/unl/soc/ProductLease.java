package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.time.LocalDateTime;
import java.util.Objects;

@XStreamAlias("productLease")
public class ProductLease extends Item{
    @XStreamOmitField
    private static final byte TAX_PERCENTAGE = 0;
    @XStreamOmitField
    private LocalDateTime totalTime;
    @Expose
    private double price;
    @XStreamOmitField
    private double firstMonthPrice;
    @XStreamOmitField
    private double markupPrice;

    public ProductLease(String uniqueCode, String name, double basePrice) {
        super(uniqueCode, name);
        this.price = basePrice;
    }

    public LocalDateTime getTotalTime() {
        return totalTime;
    }

    public double getPrice() {
        return price;
    }

    public double getFirstMonthPrice() {
        return firstMonthPrice;
    }

    public double getMarkupPrice() {
        return markupPrice;
    }

    @Override
    public String toString() {
        return "Product Lease{" +
                "\n  Unique identifier: " + super.getUniqueCode() +
                "\n  Name: " + super.getName() +
                "\n  Total lease time: " + totalTime +
                "\n  First month price: $" + firstMonthPrice +
                "\n  Markup price: $" + markupPrice +
                "\n  Total tax: $" + super.getTax() +
                "\n  Total upfront price: $" + (firstMonthPrice + markupPrice) +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductLease that = (ProductLease) o;
        return Double.compare(price, that.price) == 0 && Double.compare(firstMonthPrice, that.firstMonthPrice) == 0 && Double.compare(markupPrice, that.markupPrice) == 0 && Objects.equals(totalTime, that.totalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalTime, price, firstMonthPrice, markupPrice);
    }
}