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
    private final String uniqueCode;
    private final Store store;
    private final Person customer;
    private final Person salesman;
    private final LocalDate dateTime;
    private List<Item> itemsList;

    public Sale(String uniqueCode, Store store, Person customer, Person salesman, String dateString) {
        this.uniqueCode = uniqueCode;
        this.store = store;
        this.customer = customer;
        this.salesman = salesman;
        this.dateTime = LocalDate.parse(dateString);
        this.itemsList = new ArrayList<>();
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

    public Person getCustomer() {
        return customer;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Sale    #").append(this.uniqueCode).append("\n");
        sb.append("Store   #").append(this.getStore().getStoreCode()).append("\n");
        sb.append("Date     ").append(this.getDateTime()).append("\n");
        sb.append("Customer:\n").append(customer).append("\n");
        sb.append("Sales Person:\n").append(salesman).append("\n");
        sb.append(String.format("Items (%d) %61s %10s\n" , getItemsList().size(), "Tax", "Total"));
        sb.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                    -=-=-=-=-=-= -=-=-=-=-=\n");
        for (Item item : itemsList) {
            sb.append(item).append("\n");
        }
        sb.append("                                                           -=-=-=-=-=-= -=-=-=-=-=\n");
        sb.append(String.format("%58s %2s %9.2f %1s %8.2f\n", "Subtotals", "$", getTotalTax(), "$", getGrossPrice()));
        sb.append(String.format("%58s %14s %8.2f\n", "Grand total", "$", getNetPrice()));
        return  sb.toString();
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