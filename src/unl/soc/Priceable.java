package unl.soc;

/**
 * Interface for objects that have price-related information.
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
