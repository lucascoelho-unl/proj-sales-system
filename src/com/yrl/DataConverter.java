package com.yrl;

import unl.soc.*;

import java.util.List;

/**
 * The DataConverter class converts data from CSV files to XML and JSON formats.
 */
public class DataConverter {
    public static void main(String[] args) {
        List<Item> itemList = DataProcessor.readItemsCSVtoList("data/Items.csv");

        DataWriter.createXMLFile(itemList, "data/ItemsOutput.xml");
        DataWriter.createJsonFile(itemList, "Items", "data/ItemsOutput.json");

        List<Person> personList = DataProcessor.readPersonCSVtoList("data/Persons.csv");

        DataWriter.createXMLFile(personList, "data/PersonsOutput.xml");
        DataWriter.createJsonFile(personList, "Persons", "data/PersonsOutput.json");

        List<Store> storeList = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        DataWriter.createXMLFile(storeList, "data/StoresOutput.xml");
        DataWriter.createJsonFile(storeList, "Stores", "data/StoresOutput.json");
    }
}