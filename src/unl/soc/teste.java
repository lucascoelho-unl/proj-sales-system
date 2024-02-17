package unl.soc;

import unl.soc.items.Item;

import java.util.List;

public class teste {
    public static void main(String[] args) {
        List<Item> teste = Utils.readCSVItems("data/Items.csv");
        System.out.println(teste);
    }
}
