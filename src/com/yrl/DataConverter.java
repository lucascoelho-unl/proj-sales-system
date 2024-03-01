package com.yrl;

import unl.soc.*;

import java.util.List;

/**
 * The DataConverter class converts data from CSV files to XML and JSON formats.
 */
public class DataConverter {
    public static void main(String[] args) {
        List<Item> itemList = DataProcessor.readItemsCSVtoList("data/Items.csv");

        DataWriter.createXMLFile(itemList, "data/itemOutput.xml");
        DataWriter.createJsonFile(itemList, "Items", "data/ItemOutput.json");

        List<Person> personList = DataProcessor.readPersonCSVtoList("data/Persons.csv");

        DataWriter.createXMLFile(personList, "data/PersonOutput.xml");
        DataWriter.createJsonFile(personList, "Persons", "data/PersonOutput.json");

        List<Store> storeList = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        DataWriter.createXMLFile(storeList, "data/StoreOutput.xml");
        DataWriter.createJsonFile(storeList, "Stores", "data/StoreOutput.json");
    }
}