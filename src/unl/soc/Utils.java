package unl.soc;

import unl.soc.items.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Utils {

    /**
     * Reads data from a CSV file and creates a list of Item objects.
     *
     * @param path The path to the CSV file.
     * @return A List of Item objects read from the CSV file.
     * @throws RuntimeException if the file is not found or if there is an issue reading the file.
     */
    public static List<Item> readCSVItems(String path) {
        try {
            Scanner s = new Scanner(new File(path));
            List<Item> items = new ArrayList<>();

            s.nextLine();
            while (s.hasNext()) {
                String line = s.nextLine();
                List<String> itemsInfo = Arrays.asList(line.split(","));
                Item item = new Item(itemsInfo.get(0), itemsInfo.get(1), itemsInfo.get(2), Double.parseDouble(itemsInfo.get(3)));
                items.add(item);
            }
            s.close();
            return items;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not find on" + path + e);
        }
    }

    /**
     * Parses a list of Item objects into separate lists based on their types and constructs a map
     * containing these lists categorized as product purchases, product leases, voice plans, data plans, and services.
     *
     * @param itemsList The list of Item objects to be parsed.
     * @return A map containing categorized lists of ProductPurchase, ProductLease, VoicePlan, DataPlan, and Service objects.
     */
    public static Map<String, Object> itemsDictParse(List<Item> itemsList) {
        List<ProductPurchase> productPurchaseList = new ArrayList<>();
        List<VoicePlan> voicePlanList = new ArrayList<>();
        List<DataPlan> dataPlanList = new ArrayList<>();
        List<ProductLease> productLeaseList = new ArrayList<>();
        List<Service> serviceList = new ArrayList<>();

        Map<String, Object> result = new HashMap<>(Map.of(
                "purchase", productPurchaseList,
                "lease", productLeaseList,
                "voicePlan", voicePlanList,
                "dataPlan", dataPlanList,
                "service", serviceList));

        for (Item item : itemsList) {
            if (item.getType().compareTo("P") == 0) {
                ProductPurchase purchase = new ProductPurchase(item.getUniqueCode(), item.getType(), item.getName(), item.getBasePrice());
                productPurchaseList.add(purchase);
                result.put("purchase", productPurchaseList);
            }
            else if (item.getType().compareTo("S") == 0) {
                Service service = new Service(item.getUniqueCode(), item.getType(), item.getName(), item.getBasePrice());
                serviceList.add(service);
                result.put("service", serviceList);
            }
            else if (item.getType().compareTo("V") == 0) {
                VoicePlan voicePlan = new VoicePlan(item.getUniqueCode(), item.getType(), item.getName(), item.getBasePrice());
                voicePlanList.add(voicePlan);
                result.put("voicePlan", voicePlanList);
            }
            else if (item.getType().compareTo("D") == 0) {
                DataPlan dataPlan = new DataPlan(item.getUniqueCode(), item.getType(), item.getName(), item.getBasePrice());
                dataPlanList.add(dataPlan);
                result.put("dataPlan", dataPlanList);
            }
        }
        return result;
    }
}

