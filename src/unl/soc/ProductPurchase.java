package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.Objects;

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

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product Purchase{" +
                "\n  Total price: $" + price +
                "\n}";
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