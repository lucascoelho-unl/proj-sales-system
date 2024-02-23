package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.Objects;

/**
 * The DataPlan class represents a data plan item.
 * It extends the Item class and includes additional fields specific to data plans.
 * It includes Getters, ToString, HashCode and Equals methods
 */
@XStreamAlias("dataPlan")
public class DataPlan extends Item{
    @XStreamOmitField
    private static final double TAX_PERCENTAGE = 0.055;
    @Expose
    private double pricePerGB;
    @XStreamOmitField
    private double totalGigabyte;

    public DataPlan(String uniqueCode, String name, double pricePerGB) {
        super(uniqueCode, name);
        this.pricePerGB = pricePerGB;
    }

    public double getPricePerGB() {
        return pricePerGB;
    }

    public double getTotalGigabyte() {
        return totalGigabyte;
    }

    @Override
    public double getGrossPrice() { return pricePerGB * totalGigabyte; }

    @Override
    public double getTotalTax() { return getGrossPrice() * TAX_PERCENTAGE; }

    @Override
    public double getNetPrice() { return getGrossPrice() + getTotalTax(); }

    @Override
    public String toString() {
        return "Data Plan{" +
                "\n  Unique identifier: " + getUniqueCode() +
                "\n  Plan name: " + getName() +
                "\n  Price per GB: " + pricePerGB +
                "\n  Total Bought GB: " + totalGigabyte +
                "\n  Total tax: $" + getTotalTax() +
                "\n  Total price: " + getGrossPrice() +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPlan dataPlan = (DataPlan) o;
        return Double.compare(pricePerGB, dataPlan.pricePerGB) == 0 && Double.compare(totalGigabyte, dataPlan.totalGigabyte) == 0 && Double.compare(getGrossPrice(), dataPlan.getGrossPrice()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pricePerGB, totalGigabyte, getGrossPrice());
    }
}