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

    private int id;
    @XStreamOmitField
    private double totalPeriod;
    @XStreamOmitField
    private String phoneNumber;
    @Expose
    private double periodCost;

    public VoicePlan(String uniqueCode, String name, double periodPrice) {
        super(uniqueCode, name);
        this.periodCost = periodPrice;
    }

    public VoicePlan(int id, String uniqueCode, String name, double periodPrice) {
        super(id, uniqueCode, name);
        this.periodCost = periodPrice;
    }

    public VoicePlan(Item item, String phoneNumber, double totalPeriod) {
        super(item.getUniqueCode(), item.getName());
        this.phoneNumber = phoneNumber;
        this.totalPeriod = totalPeriod;
        this.periodCost = item.getBasePrice();
    }

    public VoicePlan(int id, Item item, String phoneNumber, double totalPeriod) {
        super(id, item.getUniqueCode(), item.getName());
        this.phoneNumber = phoneNumber;
        this.totalPeriod = totalPeriod;
        this.periodCost = item.getBasePrice();
    }

    public VoicePlan(int id, String uniqueCode, String name, double periodPrice, String phoneNumber, double totalPeriod) {
        super(uniqueCode, name);
        this.id = id;
        this.periodCost = periodPrice;
        this.totalPeriod = totalPeriod;
        this.phoneNumber = phoneNumber;
    }

    public double getTotalPeriod() {
        return totalPeriod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public double getGrossPrice() {
        return Math.round(100 * periodCost * (totalPeriod / 30)) / 100.0;
    }

    @Override
    public double getTotalTax() {
        return Math.round(100 * getGrossPrice() * TAX_PERCENTAGE) / 100.0;
    }

    @Override
    public double getBasePrice() {
        return Math.round(100 * periodCost) / 100.0;
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