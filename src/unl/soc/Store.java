package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Store class represents a store where items are sold.
 * It includes information such as the store code, manager,
 * address, and list of items available in the store.
 * It includes Getters, ToString, HashCode and Equals methods
 */
@XStreamAlias("store")
public class Store {
    @Expose
    private String storeCode;
    @Expose
    private Person manager;
    @Expose
    private Address address;
    @Expose
    private List<Sale> sales;

    public Store(String storeCode, Address address, Person manager) {
        this.storeCode = storeCode;
        this.manager = manager;
        this.address = address;
        this.sales = new ArrayList<>();
    }

    public void addSale(Sale sale) {
        this.sales.add(sale);
    }

    public double getTotalSalePrice() {
        double total = 0;
        for (Sale sale : this.sales) {
            total += sale.getNetPrice();
        }
        return total;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public Person getManager() {
        return manager;
    }

    public Address getAddress() {
        return address;
    }

    public List<Sale> getSales() {
        return sales;
    }

    @Override
    public String toString() {
        String store = getStoreCode();
        String managerFullName;
        if (getManager() == null) {
            managerFullName = "No manager.";
        } else {
            managerFullName = getManager().getFirstName() + " " + getManager().getLastName();
        }
        List<Sale> saleList = getSales();
        int numSales = saleList.size();
        double totalPrice = getTotalSalePrice();
        String formatString = "%-9s  %-20s  %5d  %3s  %8.2f";
        return String.format(formatString, store, managerFullName, numSales, "$", totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeCode, manager, address, sales);
    }

    public static int compareStores(Store store1, Store store2) {
        // Compare by manager's last name and first name before comparing by total sale value
        int lastNameComparison = store1.getManager().getLastName().compareTo(store2.getManager().getLastName());
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }
        int firstNameComparison = store1.getManager().getFirstName().compareTo(store2.getManager().getFirstName());
        if (firstNameComparison != 0) {
            return firstNameComparison;
        }
        // If last names and first names are equal, compare by total sale value
        return Double.compare(store2.getTotalSalePrice(), store1.getTotalSalePrice());
    }
}