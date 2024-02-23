package unl.soc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The Purchase class represents a purchase made at a store.
 * It includes information about the store, items purchased, sales representative,
 * date and time of purchase, total tax, and total price.
 * It includes Getters, ToString, HashCode and Equals methods
 */
public class Purchase {
    private static final byte GROSS_PRICE = 0;
    private static final byte TAX = 1;
    private final UUID uniqueCode;
    private Store store;
    private List<Item> itemsList;
    private Employee salesman;
    private LocalDateTime dateTime;
    private double totalTax;
    private double netPrice;
    private double grossPrice;

    public Purchase(Store store, List<Item> itemsList, Employee salesman) {
        this.uniqueCode = UUID.randomUUID();
        this.store = store;
        this.itemsList = itemsList;
        this.salesman = salesman;
        this.dateTime = LocalDateTime.now();
        this.totalTax = calculateTotal(TAX);
        this.grossPrice = calculateTotal(GROSS_PRICE);
        this.netPrice = this.totalTax + this.grossPrice;
    }

    /**
     * Calculates the total price or total tax of the purchase..
     *
     * @param variableToCalculate A short variable indicating the type of calculation:
     *                            - If variableToCalculate is TOTAL_PRICE, calculates the total price of all items.
     *                            - Otherwise, calculates the total tax of all items.
     * @return The calculated total value based on the specified variable.
     */
    private double calculateTotal(short variableToCalculate) {
        double total = 0;

        if (variableToCalculate == GROSS_PRICE) {
            for (Item item : this.itemsList) {
                total += item.getGrossPrice();
            }
        } else {
            for (Item item : this.itemsList) {
                total += item.getTotalTax();
            }
        }

        return total;
    }

    public UUID getUniqueCode() {
        return uniqueCode;
    }

    public Store getStore() {
        return store;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public Employee getSalesman() {
        return salesman;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public double getNetPrice() { return netPrice; }

    public double getGrossPrice() { return grossPrice; }

    @Override
    public String toString() {
        StringBuilder items = new StringBuilder();
        for (Item item : itemsList) {
            items.append(", " + item);
        }
        return "Purchase number " + uniqueCode + "\nEmployee " + salesman + "\nSold " + items + "\nSub Total Price $" + grossPrice + "\nTaxes $" + totalTax + "\nTime " + dateTime + "\nStore " + store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Double.compare(totalTax, purchase.totalTax) == 0 && Double.compare(getNetPrice(), purchase.getGrossPrice()) == 0 && Objects.equals(uniqueCode, purchase.uniqueCode) && Objects.equals(store, purchase.store) && Objects.equals(itemsList, purchase.itemsList) && Objects.equals(salesman, purchase.salesman) && Objects.equals(dateTime, purchase.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, store, itemsList, salesman, dateTime, totalTax, getNetPrice());
    }
}