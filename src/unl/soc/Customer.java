package unl.soc;

import java.util.List;
import java.util.Objects;

/**
 * The Customer class represents a customer who has purchased items.
 * It contains information about the customer and the list of items they have purchased.
 * It includes Getters, ToString, HashCode and Equals methods
 */
public class Customer {
    private Person customer;
    private List<Purchase> purchasedItems;

    public Customer(Person customer) {
        this.customer = customer;
    }

    public Customer(Person customer, List<Purchase> purchaseList) {
        this.customer = customer;
        this.purchasedItems = purchaseList;
    }

    public Customer(Purchase purchase) {
        this.purchasedItems.add(purchase);
    }

    public Person getCustomer() {
        return customer;
    }

    public List<Purchase> getPurchasedItems() {
        return purchasedItems;
    }

    @Override
    public String toString() {
        StringBuilder customerToString = new StringBuilder();
        customerToString.append("List of purchased items: {");
        for (Purchase purchase : purchasedItems) {
            customerToString.append(purchase);
        }
        return "Customer: " + customer + "\n" + customerToString + "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer1 = (Customer) o;
        return Objects.equals(customer, customer1.customer) && Objects.equals(purchasedItems, customer1.purchasedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, purchasedItems);
    }
}