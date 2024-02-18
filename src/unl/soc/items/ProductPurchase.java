package unl.soc.items;

import com.google.gson.annotations.Expose;

public class ProductPurchase extends Item {
    private static final double TAX_PERCENTAGE = 0.065;
    @Expose
    private double price;

    public ProductPurchase(String uniqueCode, String type, String name, double basePrice) {
        super(uniqueCode, type, name);
        this.price = basePrice;
    }
}

