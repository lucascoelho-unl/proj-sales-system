package unl.soc.items;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.time.LocalDateTime;
import java.util.Objects;

@XStreamAlias("voicePlan")
public class VoicePlan extends Item {
    @XStreamOmitField
    private static final double TAX_PERCENTAGE = 0.065;
    @XStreamOmitField
    private LocalDateTime timePeriod; //Target Time - Purchase time.
    @Expose
    private double periodPrice;
    @XStreamOmitField
    private double priceBeforeTax;

    public VoicePlan(String uniqueCode, String name, double basePrice) {
        super(uniqueCode, name);
        this.periodPrice = basePrice;
    }

    @Override
    public String toString() {
        return "Data Plan{" +
                "\n  Unique identifier: " + super.getUniqueCode() +
                "\n  Plan name: " + super.getName() +
                "\n  Period price: " + periodPrice +
                "\n  Time period: " + timePeriod +
                "\n  Total tax: $" + super.getTax() +
                "\n  Total price: " + super.getTotalPrice() +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VoicePlan voicePlan = (VoicePlan) o;
        return Double.compare(periodPrice, voicePlan.periodPrice) == 0 && Double.compare(priceBeforeTax, voicePlan.priceBeforeTax) == 0 && Objects.equals(timePeriod, voicePlan.timePeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timePeriod, periodPrice, priceBeforeTax);
    }
}