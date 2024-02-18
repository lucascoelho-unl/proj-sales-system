package unl.soc.items;

import com.google.gson.annotations.Expose;

public class DataPlan extends Item{
    private static final double TAX_PERCENTAGE = 0.055;
    @Expose
    private double pricePerGB;
    private double consumedGB;
    private double priceBeforeTax;

    public DataPlan(String uniqueCode, String type, String name, double basePrice) {
        super(uniqueCode, type, name);
        this.pricePerGB = basePrice;
    }
}
