package unl.soc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.yrl.DataConverter;
import unl.soc.items.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class teste {
    public static void main(String[] args){
        List<Item> teste = Utils.readCSVItems("data/Items.csv");
        Map teste2 = Utils.itemsDictParse(teste);
        System.out.println(teste);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        var json = gson.toJson(teste);
        System.out.println(json);

        DataConverter.createJsonFile(teste, "data/testFile.json");
    }
}
