package unl.soc.items;

import com.google.gson.annotations.Expose;
import unl.soc.person.Employee;

import java.time.LocalDateTime;

public class Service extends Item{

    private static final double TAX_PERCENTAGE = 0.035;
    @Expose
    private double hourlyRate;
    private Employee employee;
    private LocalDateTime totalTime; //Target Time - Purchased time

    public Service(String uniqueCode, String type, String name, double basePrice) {
        super(uniqueCode, type, name);
        this.hourlyRate = basePrice;
    }
}
