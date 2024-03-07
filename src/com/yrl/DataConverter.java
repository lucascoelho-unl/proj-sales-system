package com.yrl;

import unl.soc.*;

import java.util.List;

/**
 * The DataConverter class converts data from CSV files to XML and JSON formats.
 */
public class DataConverter {
    public static void main(String[] args) {
        List<Item> itemList = DataProcessor.readItemsCSVtoList("data/Items.csv");

        DataWriter.createXMLFile(itemList, "testOutput/itemOutput.xml");
        DataWriter.createJsonFile(itemList, "Items", "testOutput/ItemOutput.json");

        List<Person> personList = DataProcessor.readPersonCSVtoList("data/Persons.csv");

        DataWriter.createXMLFile(personList, "testOutput/PersonOutput.xml");
        DataWriter.createJsonFile(personList, "Persons", "testOutput/PersonOutput.json");

        List<Store> storeList = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        DataWriter.createXMLFile(storeList, "testOutput/StoreOutput.xml");
        DataWriter.createJsonFile(storeList, "Stores", "testOutput/StoreOutput.json");
    }
}