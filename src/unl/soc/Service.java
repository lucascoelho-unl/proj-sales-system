package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.time.LocalDateTime;
import java.util.Objects;

@XStreamAlias("service")
public class Service extends Item {
    @XStreamOmitField
    private static final double TAX_PERCENTAGE = 0.035;
    @Expose
    private double hourlyRate;
    @XStreamOmitField
    private Employee employee;
    @XStreamOmitField
    private LocalDateTime totalTime; //Target Time - Purchased time

    public Service(String uniqueCode, String name, double basePrice) {
        super(uniqueCode, name);
        this.hourlyRate = basePrice;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDateTime getTotalTime() {
        return totalTime;
    }

    @Override
    public String toString() {
        return "Service{" +
                "\n  Unique identifier: " + super.getUniqueCode() +
                "\n  Plan name: " + super.getName() +
                "\n  Employee: " + employee +
                "\n  Hourly rate: $" + hourlyRate +
                "\n  TotalTime: " + totalTime +
                "\n  Total tax: $" + super.getTax() +
                "\n  Total price: $" + super.getTotalPrice() +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Service service = (Service) o;
        return Double.compare(hourlyRate, service.hourlyRate) == 0 && Objects.equals(employee, service.employee) && Objects.equals(totalTime, service.totalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hourlyRate, employee, totalTime);
    }
}