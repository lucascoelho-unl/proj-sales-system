package unl.soc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.thoughtworks.xstream.XStream;
import com.yrl.DataConverter;
import unl.soc.items.*;

import java.util.List;
import java.util.Map;

public class teste {
    public static void main(String[] args){
        List<Item> teste = Utils.readCSVItems("data/Items.csv");
        Map<String, Object> teste2 = Utils.itemsDictParse(teste);

        System.out.println(teste);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        var json = gson.toJson(teste);
        System.out.println(json);

        XStream xStream = new XStream();
        xStream.alias("Item", Item.class);
        String xmlConversion = xStream.toXML(teste);
        System.out.println(xmlConversion);

        var personList = Utils.readCSVPerson("data/Persons.csv");
        System.out.println(personList);
    }
}
