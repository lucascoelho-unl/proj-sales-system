package unl.soc.items;

public class DataPlan extends Item{
    private static final double TAX_PERCENTAGE = 0.055;
    private double pricePerGB;
    private double consumedGB;
    private double priceBeforeTax;

    public DataPlan(String uniqueCode, String type, String name, double basePrice) {
        super(uniqueCode, type, name, basePrice);
    }
}
