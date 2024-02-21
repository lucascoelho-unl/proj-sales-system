package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.Objects;

@XStreamAlias("dataPlan")
public class DataPlan extends Item{
    @XStreamOmitField
    private static final double TAX_PERCENTAGE = 0.055;
    @Expose
    private double pricePerGB;
    @XStreamOmitField
    private double consumedGB;
    @XStreamOmitField
    private double priceBeforeTax;

    public DataPlan(String uniqueCode, String name, double basePrice) {
        super(uniqueCode, name);
        this.pricePerGB = basePrice;
    }

    public double getPricePerGB() {
        return pricePerGB;
    }

    public double getConsumedGB() {
        return consumedGB;
    }

    public double getPriceBeforeTax() {
        return priceBeforeTax;
    }

    @Override
    public String toString() {
        return "Data Plan{" +
                "\n  Unique identifier: " + super.getUniqueCode() +
                "\n  Plan name: " + super.getName() +
                "\n  Price per GB: " + pricePerGB +
                "\n  Consumed GB: " + consumedGB +
                "\n  Total tax: $" + super.getTax() +
                "\n  Total price: " + super.getTotalPrice() +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPlan dataPlan = (DataPlan) o;
        return Double.compare(pricePerGB, dataPlan.pricePerGB) == 0 && Double.compare(consumedGB, dataPlan.consumedGB) == 0 && Double.compare(priceBeforeTax, dataPlan.priceBeforeTax) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pricePerGB, consumedGB, priceBeforeTax);
    }
}