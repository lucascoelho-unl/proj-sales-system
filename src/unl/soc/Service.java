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
    @XStreamOmitField
    private Person employee;
    @XStreamOmitField
    private double totalHours;
    @Expose
    private double costPerHours;

    public Service(String uniqueCode, String name, double hourlyRate) {
        super(uniqueCode, name);
        this.costPerHours = hourlyRate;
    }

    public Service(Item item, double totalHours, Person employee) {
        super(item.getUniqueCode(), item.getName());
        this.employee = employee;
        this.totalHours = totalHours;
        this.costPerHours = item.getBasePrice();
    }

    public Person getEmployee() {
        return employee;
    }

    public double getTotalHours() {
        return totalHours;
    }

    @Override
    public double getTotalTax() {
        return Math.round(100 * getGrossPrice() * TAX_PERCENTAGE)/100.0;
    }

    @Override
    public double getGrossPrice() {
        return Math.round(100 * costPerHours * totalHours)/100.0;
    }


    @Override
    public double getBasePrice() {
        return Math.round(100 * costPerHours)/100.0;
    }

    @Override
    public String toString() {
        return String.format("%s - Served by %s  \n %20.2f hours @ $%6.2f / hour  \n %60s %9.2f $%9.2f", getName() + " (" + getUniqueCode() + ")", getEmployee().getLastName() + ", " + getEmployee().getFirstName(), getTotalHours(), costPerHours, "$", getTotalTax(), getGrossPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Service service = (Service) o;
        return Double.compare(costPerHours, service.costPerHours) == 0 && Objects.equals(employee, service.employee) && Objects.equals(totalHours, service.totalHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), costPerHours, employee, totalHours);
    }
}