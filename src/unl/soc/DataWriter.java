package unl.soc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataWriter {
    /**
     * This method creates a JSON file from a list of objects using the Gson library.
     * The JSON file is written with pretty formatting.
     *
     * @param listOfObject The list of objects to be converted to JSON format.
     * @param filePath     The file path where the JSON file will be created.
     */
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

    /**
     * This method creates an XML file from a list of objects using the XStream library.
     * It processes annotations for specific classes and handles special cases such as empty lists and specific class names.
     *
     * @param listOfObject The list of objects to be converted to XML format.
     * @param filePath     The file path where the XML file will be created.
     */
    public static void createXMLFile(List<?> listOfObject, String filePath) {
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);

        if (listOfObject.isEmpty()) {
            try {
                xstream.toXML(listOfObject, new FileWriter(filePath));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Class<?> listType = listOfObject.get(0).getClass();

        if (listType.getSuperclass().isInstance(listOfObject.get(0)) && listType.getSuperclass() != Object.class) {
            // Process annotations from item classes, change label in XML file
            xstream.processAnnotations(ProductPurchase.class);
            xstream.processAnnotations(ProductLease.class);
            xstream.processAnnotations(Service.class);
            xstream.processAnnotations(VoicePlan.class);
            xstream.processAnnotations(DataPlan.class);

            //Changes the root of the list to the plural of the superclass name
            xstream.alias(String.format(listType.getSuperclass().getSimpleName() + "s").toLowerCase(), List.class);
        } else {
            xstream.processAnnotations(listType);
            //Changes the root of the list to the plural of the class name
            xstream.alias(String.format(listType.getSimpleName() + "s").toLowerCase(), List.class);
        }

        if (listType.getSimpleName().equals("Person")) {
            xstream.alias("email", String.class);
        }

        try {
            xstream.toXML(listOfObject, new FileWriter(filePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
