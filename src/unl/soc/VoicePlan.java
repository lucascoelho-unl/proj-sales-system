package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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
    private double totalPeriod;
    @XStreamOmitField
    private String phoneNumber;
    @Expose
    private double periodCost;

    public VoicePlan(String uniqueCode, String itemType ,String name, double periodPrice) {
        super(uniqueCode, itemType, name);
        this.periodCost = periodPrice;
    }

    public VoicePlan(Item item, String phoneNumber, double totalPeriod) {
        super(item.getUniqueCode(), item.getItemType(), item.getName());
        this.phoneNumber = phoneNumber;
        this.totalPeriod = totalPeriod;
        this.periodCost = item.getBasePrice();
    }

    public double getTotalPeriod() {
        return totalPeriod;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    @Override
    public double getGrossPrice() { return periodCost * (totalPeriod / 30); }

    @Override
    public double getTotalTax() { return getGrossPrice() * TAX_PERCENTAGE; }

    @Override
    public double getBasePrice() {
        return periodCost;
    }

    @Override
    public String toString() {
        return String.format("%s - %s \n %20.2f days @ $%.2f / %s \n %60s %9.2f $%9.2f", getName() + " (" + getUniqueCode() + ")", "Voice " + getPhoneNumber(), getTotalPeriod(), periodCost, "30 days", "$", getTotalTax(), getGrossPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VoicePlan voicePlan = (VoicePlan) o;
        return Double.compare(periodCost, voicePlan.periodCost) == 0 && Double.compare(getGrossPrice(), voicePlan.getGrossPrice()) == 0 && Objects.equals(totalPeriod, voicePlan.totalPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalPeriod, periodCost, getGrossPrice());
    }
}