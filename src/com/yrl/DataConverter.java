package com.yrl;

import unl.soc.*;

public class DataConverter {
    public static void main(String[] args) {
        //Testing Items
        var itemMap = Utils.readItemsCSVtoMap("data/Items.csv");
        var itemList = Utils.readItemsCSVtoList("data/Items.csv");

        Utils.createXMLFile(itemList, "data/itemOutput.xml");
        Utils.createJsonFile(itemList, "data/ItemOutput.json");

        //Testing Person
        var personMap = Utils.readPersonCSVtoMap("data/Persons.csv");
        var personList = Utils.readPersonCSVtoList("data/Persons.csv");

        Utils.createXMLFile(personList, "data/PersonOutput.xml");
        Utils.createJsonFile(personList, "data/PersonOutput.json");

        //Testing Store
        var storeMap = Utils.readStoreCSVtoMap("data/Stores.csv");
        var storeList = Utils.readStoreCSVtoList("data/Stores.csv");

        Utils.createXMLFile(storeList, "data/StoreOutput.xml");
        Utils.createJsonFile(storeList, "data/StoreOutput.json");
    }
}