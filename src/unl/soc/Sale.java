package unl.soc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Purchase class represents a purchase made at a store.
 * It includes information about the store, items purchased, sales representative,
 * date and time of purchase, total tax, and total price.
 * It includes Getters, ToString, HashCode and Equals methods
 */
public class Sale implements Priceable{
    private static final byte GROSS_PRICE = 0;
    private static final byte TAX = 1;
    private final String uniqueCode;
    private Store store;
    private Person customer;
    private Person salesman;
    private LocalDate dateTime;
    private List<Item> itemsList = new ArrayList<>();

    public Sale(String uniqueCode, Store store, Person customer, Person salesman, String dateString, List<Item> itemsList) {
        this.uniqueCode = uniqueCode;
        this.store = store;
        this.customer = customer;
        this.salesman = salesman;
        this.dateTime = LocalDate.parse(dateString);
        this.itemsList = itemsList;
    }

    public Sale(String uniqueCode, Store store, Person customer, Person salesman, String dateString) {
        this.uniqueCode = uniqueCode;
        this.store = store;
        this.customer = customer;
        this.salesman = salesman;
        this.dateTime = LocalDate.parse(dateString);
    }

    /**
     * Retrieves the total gross price of the sale,
     *
     * @return The total gross price of the sale.
     */
    @Override
    public double getGrossPrice(){
        double total = 0;
        for (Item item : this.itemsList) {
            total += item.getGrossPrice();
        }
        return total;
    }

    /**
     * Retrieves the total tax of the sale,
     *
     * @return The total tax of the sale.
     */
    @Override
    public double getTotalTax(){
        double total = 0;
        for (Item item : this.itemsList) {
            total += item.getTotalTax();
        }
        return total;
    }

    public void addItem(Item item){
        this.itemsList.add(item);
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public Store getStore() {
        return store;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public Person getSalesman() {
        return salesman;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public double getNetPrice() { return getGrossPrice() + getTotalTax(); }

    @Override
    public String toString() {
        StringBuilder items = new StringBuilder();
        for (Item item : itemsList) {
            items.append(item).append("\n");
        }
        return "\nStore " + store +
                "\nTime " + dateTime +
                "\nEmployee: " + salesman +
                "Purchase number: " + uniqueCode +
                "\nSold: " + items +
                "Subtotal: $" + Math.round(getGrossPrice() * 100) / 100 +
                "\nTaxes: $" + Math.round(getTotalTax() * 100) / 100 +
                "\nTotal: $" + Math.round(getNetPrice() * 100) / 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale purchase = (Sale) o;
        return Double.compare(getTotalTax(), purchase.getTotalTax()) == 0 && Double.compare(getGrossPrice(), purchase.getGrossPrice()) == 0 && Objects.equals(uniqueCode, purchase.uniqueCode) && Objects.equals(store, purchase.store) && Objects.equals(itemsList, purchase.itemsList) && Objects.equals(salesman, purchase.salesman) && Objects.equals(dateTime, purchase.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, store, itemsList, salesman, dateTime, getTotalTax(), getGrossPrice());
    }
}