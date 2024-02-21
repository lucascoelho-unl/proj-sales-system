package com.yrl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import unl.soc.items.*;
import unl.soc.person.Person;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataConverter {
    public static void createJsonFile(List<?> listOfObject, String filePath) {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        String json = gson.toJson(listOfObject);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void  createXMLFile(List<?> listOfObject, String filePath) {
        if (listOfObject.isEmpty()){
            throw new RuntimeException();
        }

        XStream xStream = new XStream();

        // Process annotations from item classes, change label in XML file
        xStream.processAnnotations(ProductPurchase.class);
        xStream.processAnnotations(ProductLease.class);
        xStream.processAnnotations(Service.class);
        xStream.processAnnotations(VoicePlan.class);
        xStream.processAnnotations(DataPlan.class);

        var listType = listOfObject.get(0).getClass();
        // Process annotations for several other classes. (FIGURED IT OUT ::: This annotation was not working with item classes as it is a list of various classes)
        xStream.processAnnotations(listType);
        xStream.alias(String.format(listType.getSimpleName() + "s").toLowerCase(), List.class);
        if (listType.getSimpleName().equals("Person")){
            xStream.alias("email", String.class);
        }

        String xmlConversion = xStream.toXML(listOfObject);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(xmlConversion);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
