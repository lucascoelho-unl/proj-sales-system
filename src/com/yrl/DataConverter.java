package com.yrl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataConverter {

    public static void createJsonFile(Object object, String output){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

    }
}
