package unl.soc.items;

public class ProductPurchase extends Item {
    private static final double TAX_PERCENTAGE = 0.065;


    public ProductPurchase(String uniqueCode, String type, String name, double basePrice) {
        super(uniqueCode, type, name, basePrice);
    }
}

