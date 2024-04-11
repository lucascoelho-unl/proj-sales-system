package unl.soc;

/**
 * Interface for objects that are price-related.
 */
public interface Priceable {
    /**
     * Gets the gross price of the item.
     *
     * @return The gross price.
     */
    double getGrossPrice();

    /**
     * Gets the total tax of the item.
     *
     * @return The total tax.
     */
    double getTotalTax();
}
