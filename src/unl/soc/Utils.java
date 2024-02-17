package unl.soc;

import unl.soc.items.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Utils {
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
            return items;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not find on" + path + e);
        }
    }
}
