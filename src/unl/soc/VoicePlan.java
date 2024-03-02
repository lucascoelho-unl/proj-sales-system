package unl.soc;

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

    public VoicePlan(String uniqueCode, String name, double periodPrice) {
        super(uniqueCode, name, periodPrice);
    }


    public VoicePlan(Item item, String phoneNumber, double totalPeriod) {
        super(item.getUniqueCode(), item.getName(), item.getBasePrice());
        this.phoneNumber = phoneNumber;
        this.totalPeriod = totalPeriod;
    }

    public double getTotalPeriod() {
        return totalPeriod;
    }

    public double getPeriodPrice() {
        return super.getBasePrice();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    @Override
    public double getGrossPrice() { return super.getBasePrice() * (totalPeriod / 30); }

    @Override
    public double getTotalTax() { return getGrossPrice() * TAX_PERCENTAGE; }

    @Override
    public String toString() {
        return String.format("%s - %s \n %20.2f days @ $%.2f / %s \n %60s %9.2f $%9.2f", getName() + " (" + getUniqueCode() + ")", "Voice " + getPhoneNumber(), getTotalPeriod(), getPeriodPrice(), "30 days", "$", getTotalTax(), getGrossPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VoicePlan voicePlan = (VoicePlan) o;
        return Double.compare(super.getBasePrice(), voicePlan.getBasePrice()) == 0 && Double.compare(getGrossPrice(), voicePlan.getGrossPrice()) == 0 && Objects.equals(totalPeriod, voicePlan.totalPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalPeriod, super.getBasePrice(), getGrossPrice());
    }
}