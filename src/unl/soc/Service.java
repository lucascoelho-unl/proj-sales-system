package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.Objects;

/**
 * The Service class represents a service offered by the store.
 * It extends the Item class and includes information about the hourly rate,
 * employee providing the service, and the total time of service.
 * It includes Getters, ToString, HashCode and Equals methods
 */
@XStreamAlias("service")
public class Service extends Item {
    @XStreamOmitField
    private static final double TAX_PERCENTAGE = 0.035;
    @Expose
    private double hourlyRate;
    @XStreamOmitField
    private Employee employee;
    @XStreamOmitField
    private double totalHours; //Target Time - Purchased time

    public Service(String uniqueCode, String name, double hourlyRate) {
        super(uniqueCode, name);
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public double getTotalHours() {
        return totalHours;
    }

    @Override
    public double getNetPrice() {
        return getGrossPrice() + getTotalTax();
    }

    @Override
    public double getTotalTax() {
        return getGrossPrice() * TAX_PERCENTAGE;
    }

    @Override
    public double getGrossPrice() {
        return hourlyRate * totalHours;
    }


    @Override
    public String toString() {
        return String.format("Service{" +
                "\n  Unique identifier: " + getUniqueCode() +
                "\n  Plan name: " + getName() +
                "\n  Employee: " + employee +
                "\n  Hourly rate: $%.2f" +
                "\n  Total time: " + totalHours +
                "\n  Total tax: $%.2f" +
                "\n  Total price: $%.2f" +
                "\n}", Math.round(hourlyRate * 100) / 100.0, Math.round(getTotalTax() * 100) / 100.00, Math.round(getNetPrice() * 100) / 100.00);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Service service = (Service) o;
        return Double.compare(hourlyRate, service.hourlyRate) == 0 && Objects.equals(employee, service.employee) && Objects.equals(totalHours, service.totalHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hourlyRate, employee, totalHours);
    }
}