package unl.soc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class Sale {

    private String uniqueCode = generateSaleUniqueCode();
    private Store store;
    private Item item;
    private Person salesman;
    private LocalDateTime dateTime = LocalDateTime.now();

    public Sale(Store store, Item item, Person salesman) {
        this.store = store;
        this.item = item;
        this.salesman = salesman;
    }

    private String generateSaleUniqueCode() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}

