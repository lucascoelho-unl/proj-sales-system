package unl.soc;

import jdk.jshell.execution.Util;
import unl.soc.items.*;

import java.util.List;
import java.util.Map;

public class teste {
    public static void main(String[] args) {
        List<Item> teste = Utils.readCSVItems("data/Items.csv");
        Map teste2 = Utils.itemsDictParse(teste);
        System.out.println(teste);

    }
}
