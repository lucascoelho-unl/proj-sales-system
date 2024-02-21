package com.yrl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import unl.soc.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataConverter {
    public static void main(String[] args) {
        //Testing Items
        var itemMap = Utils.readItemsCSVtoMap("data/Items.csv");
        var itemList = Utils.readItemsCSVtoList("data/Items.csv");

        Utils.createXMLFile(itemList, "data/itemTest.xml");
        Utils.createJsonFile(itemList, "outputTest/ItemTestList.json");

        //Testing Person
        var personMap = Utils.readPersonCSVtoMap("data/Persons.csv");
        var personList = Utils.readPersonCSVtoList("data/Persons.csv");

        Utils.createXMLFile(personList, "outputTest/PersonTest.xml");
        Utils.createJsonFile(personList, "outputTest/PersonTestList.json");

        //Testing Store
        var storeMap = Utils.readStoreCSVtoMap("data/Stores.csv");
        var storeList = Utils.readStoreCSVtoList("data/Stores.csv");
        Utils.createXMLFile(storeList, "outputTest/StoreTest.xml");
        Utils.createJsonFile(storeList, "outputTest/StoreTestList.json");
    }
}