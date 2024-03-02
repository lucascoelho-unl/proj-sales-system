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

    public void addSale(Sale sale){
        this.sales.add(sale);
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
        String managerFullName = getManager().getFirstName() + " " + getManager().getLastName();
        List<Sale> saleList = getSales();
        int numSales = saleList.size();
        double total = 0;
        for (Sale sale : saleList) {
            total += sale.getNetPrice();
        }
        String formatString = "%-9s  %-20s  %5d  %3s  %8.2f";
        return String.format(formatString, store, managerFullName, numSales, "$", total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeCode, manager, address, sales);
    }
}