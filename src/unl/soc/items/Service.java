package unl.soc.items;

import unl.soc.person.Employee;

import java.time.LocalDateTime;

public class Service extends Item{

    private static final double TAX_PERCENTAGE = 0.035;
    private double hourlyRate;
    private Employee employee;
    private LocalDateTime totalTime; //Target Time - Purchased time

    public Service(String uniqueCode, String type, String name, double basePrice) {
        super(uniqueCode, type, name, basePrice);
    }
}
