package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The VoicePlan class represents a voice plan item in the system.
 * It extends the Item class and includes attributes specific to voice plans.
 * It includes Getters, ToString, HashCode and Equals methods
 */
@XStreamAlias("voicePlan")
public class VoicePlan extends Item {
    @XStreamOmitField
    private static final double TAX_PERCENTAGE = 0.065;
    @XStreamOmitField
    private int totalPeriod; //Target Time - Purchase time.
    @Expose
    private double periodPrice;

    public VoicePlan(String uniqueCode, String name, double periodPrice) {
        super(uniqueCode, name);
        this.periodPrice = periodPrice;
    }

    @Override
    public double getGrossPrice() { return (periodPrice / 30) * totalPeriod; }

    @Override
    public double getTotalTax() { return getGrossPrice() * TAX_PERCENTAGE; }

    @Override
    public double getNetPrice() { return getGrossPrice() + getTotalTax(); }

    @Override
    public String toString() {
        return "Data Plan{" +
                "\n  Unique identifier: " + super.getUniqueCode() +
                "\n  Plan name: " + super.getName() +
                "\n  Period price: " + periodPrice +
                "\n  Time period: " + totalPeriod +
                "\n  Total tax: $" + getTotalTax() +
                "\n  Total price: " + getGrossPrice() +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VoicePlan voicePlan = (VoicePlan) o;
        return Double.compare(periodPrice, voicePlan.periodPrice) == 0 && Double.compare(getGrossPrice(), voicePlan.getGrossPrice()) == 0 && Objects.equals(totalPeriod, voicePlan.totalPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalPeriod, periodPrice, getGrossPrice());
    }
}