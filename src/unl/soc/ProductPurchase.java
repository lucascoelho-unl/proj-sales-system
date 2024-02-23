package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.Objects;

/**
 * The ProductPurchase class represents a purchased product.
 * It extends the Item class and includes a field for the price of the product.
 * It includes Getters, ToString, HashCode and Equals methods
 */
@XStreamAlias("productPurchase")
public class ProductPurchase extends Item {
    @XStreamOmitField
    private static final double TAX_PERCENTAGE = 0.065;
    @Expose
    private double price;

    public ProductPurchase(String uniqueCode, String name, double basePrice) {
        super(uniqueCode, name);
        this.price = basePrice;
    }

    @Override
    public double getGrossPrice() {
        return price;
    }

    @Override
    public double getTotalTax() {
        return price * TAX_PERCENTAGE;
    }

    @Override
    public double getNetPrice() {
        return getGrossPrice() + getTotalTax();
    }

    @Override
    public String toString() {
        return String.format("""
                Product Purchase{
                Total price: $%.2f
                }""", getGrossPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductPurchase that = (ProductPurchase) o;
        return Double.compare(price, that.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), price);
    }
}