package unl.soc;

import java.time.LocalDateTime;
import java.util.UUID;

public class Sale {

    private final UUID uniqueCode;
    private Store store;
    private Item item;
    private Customer salesman;
    private LocalDateTime dateTime;

    public Sale(Store store, Item item, Customer salesman) {
        this.uniqueCode = UUID.randomUUID();
        this.store = store;
        this.item = item;
        this.salesman = salesman;
        this.dateTime = LocalDateTime.now();
    }
    public Sale(Store store, Item item, Customer salesman, LocalDateTime purchaseTime) {
        this(store, item, salesman);
        this.dateTime = purchaseTime;
    }
}

