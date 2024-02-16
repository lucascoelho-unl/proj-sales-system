package unl.soc;

public class Item {
    private String uniqueCode;
    private String type;
    private String name;
    private double basePrice;

    public Item(String uniqueCode, String type, String name, double basePrice) {
        this.uniqueCode = uniqueCode;
        this.type = type;
        this.name = name;
        this.basePrice = basePrice;
    }
}
