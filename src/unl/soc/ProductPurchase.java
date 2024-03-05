package unl.soc;

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

    public ProductPurchase(String uniqueCode, String itemType,  String name, double basePrice) {
        super(uniqueCode, itemType, name,basePrice);
    }

    public ProductPurchase(Item item) {
        super(item.getUniqueCode(), item.getName(), item.getItemType() ,item.getBasePrice());
    }

    @Override
    public double getGrossPrice() {
        return super.getBasePrice();
    }

    @Override
    public double getTotalTax() {
        return super.getBasePrice() * TAX_PERCENTAGE;
    }

    @Override
    public String toString() {
        return String.format("%s \n %60s %9.2f $%9.2f", getName() + " (" + getUniqueCode() + ")", "$", getTotalTax(), getGrossPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductPurchase that = (ProductPurchase) o;
        return Double.compare(super.getBasePrice(), that.getBasePrice()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getBasePrice());
    }
}