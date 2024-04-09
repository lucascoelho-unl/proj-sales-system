package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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

    public ProductPurchase(int id, String uniqueCode, String name, double basePrice) {
        super(id, uniqueCode, name);
        this.price = basePrice;
    }

    public ProductPurchase(Item item) {
        super(item.getUniqueCode(), item.getName());
        this.price = item.getBasePrice();
    }

    @Override
    public double getGrossPrice() {
        return Math.round(100 * price)/100.0;
    }

    @Override
    public double getTotalTax() {
        return Math.round(100 * price * TAX_PERCENTAGE)/100.0;
    }

    @Override
    public double getBasePrice() {
        return getGrossPrice();
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
        return Double.compare(price, that.price) == 0;
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), price);
//    }
}