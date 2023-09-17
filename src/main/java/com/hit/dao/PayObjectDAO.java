package com.hit.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hit.model.PayObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayObjectDAO implements IDAO<PayObject> {

    private static final String FILE_PATH = "payData.json";

    public void appendCustomerToFile(PayObject payObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            PayObject[] existingData = gson.fromJson(fileReader, PayObject[].class);
            List<PayObject> updatedData = new ArrayList<>();

            if (existingData != null) {
                Collections.addAll(updatedData, existingData);
            }

            updatedData.add(payObject);

            try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
                gson.toJson(updatedData.toArray(), fileWriter);
                System.out.println("Data appended to file successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearPayDataFile() {
        try (FileWriter fileWriter = new FileWriter("payData.json", false)) {
            // Write an empty JSON array to clear the file
            fileWriter.write("[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void appendData(PayObject object) {

    }

    @Override
    public List<PayObject> readData() {
        return null;
    }

    @Override
    public void deleteData(PayObject object) {

    }
}
