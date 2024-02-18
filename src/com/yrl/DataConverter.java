package com.yrl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import unl.soc.items.Item;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataConverter {
    public static void createJsonFile(Object object, String filePath) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void  createXMLFile(Object object, String filePath) {
        XStream xStream = new XStream();
        xStream.alias("Item", Item.class);
        String xmlConversion = xStream.toXML(object);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(xmlConversion);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
