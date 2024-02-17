package unl.soc.items;

import java.time.LocalDateTime;

public class ProductLease extends Item{
    private static final byte TAX_PERCENTAGE = 0;
    private LocalDateTime totalTime;
    private double firstMonthPrice;
    private double markupPrice;

    public ProductLease(String uniqueCode, String type, String name, double basePrice) {
        super(uniqueCode, type, name, basePrice);
    }
}
