package com.yrl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import unl.soc.items.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataConverter {
    public static void createJsonFile(List<?> listOfObject, String filePath) {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            String json = gson.toJson(listOfObject);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createJsonFile(Map<String,?> listOfObject, String filePath) {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            String json = gson.toJson(listOfObject);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void createXMLFile(List<?> listOfObject, String filePath) {
        XStream xStream = new XStream();

        if (listOfObject.isEmpty()){
            try {
                xStream.toXML(listOfObject, new FileWriter(filePath));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Class<?> listType = listOfObject.get(0).getClass();

        if (listType.getSuperclass().isInstance(listOfObject.get(0)) && listType.getSuperclass() != Object.class){
            // Process annotations from item classes, change label in XML file
            xStream.processAnnotations(ProductPurchase.class);
            xStream.processAnnotations(ProductLease.class);
            xStream.processAnnotations(Service.class);
            xStream.processAnnotations(VoicePlan.class);
            xStream.processAnnotations(DataPlan.class);

            //Changes the root of the list to the plural of the superclass name
            xStream.alias(String.format(listType.getSuperclass().getSimpleName() + "s").toLowerCase(), List.class);
        }
        else {
            xStream.processAnnotations(listType);
            //Changes the root of the list to the plural of the class name
            xStream.alias(String.format(listType.getSimpleName() + "s").toLowerCase(), List.class);
        }

        if (listType.getSimpleName().equals("Person")){
            xStream.alias("email", String.class);
        }

        try {
            xStream.toXML(listOfObject, new FileWriter(filePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
