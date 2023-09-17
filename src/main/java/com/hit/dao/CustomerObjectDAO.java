package com.hit.dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.hit.model.CustomerObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerObjectDAO implements IDAO<CustomerObject> {
    private static final String FILE_PATH = "customerData.json";

    public void appendCustomerToFile(CustomerObject customerObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<CustomerObject> existingData = readCustomerDataFromFile();

        if (existingData == null) {
            existingData = new ArrayList<>();
        }

        int newCustomerId = CustomerObject.getLastCustomerId() + 1;
        customerObject.setLastCustomerId(newCustomerId);
        CustomerObject.setLastCustomerId(newCustomerId);

        existingData.add(customerObject);

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            gson.toJson(existingData, fileWriter);
            System.out.println("Data appended to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CustomerObject> readCustomerDataFromFile() {
        Gson gson = new Gson();
        List<CustomerObject> customerData = null;

        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<CustomerObject>>() {}.getType();
            customerData = gson.fromJson(fileReader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return customerData;
    }

    public static double calculateTotalIncomeFromFile() {
        Gson gson = new Gson();
        double totalIncome = 0.0;
        try (FileReader fileReader = new FileReader("customerData.json")) {
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);
            if (jsonArray != null && jsonArray.size() > 0) {
                for (JsonElement element : jsonArray) {
                    JsonObject customer = element.getAsJsonObject();
                    double amountPaid = customer.get("amountPaid").getAsDouble();
                    totalIncome += amountPaid;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalIncome;
    }

    @Override
    public void appendData(CustomerObject object) {

    }

    @Override
    public List<CustomerObject> readData() {
        return null;
    }

    @Override
    public void deleteData(CustomerObject object) {

    }
}
