package com.hit.model;

import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;

public class CustomerObject {
    private static int lastCustomerId = 0;

    private static double totalIncome = 0.0;
    private int customerId;
    private double total;
    private double amountPaid;
    private double change;

    public CustomerObject(double total, double amountPaid, double change) {
        this.customerId = ++lastCustomerId;
        this.total = total;
        this.amountPaid = amountPaid;
        this.change = change;
    }

    public static void readLastCustomerIdFromFile() {
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader("customerData.json")) {
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);
            if (jsonArray != null && jsonArray.size() > 0) {
                JsonObject lastCustomer = jsonArray.get(jsonArray.size() - 1).getAsJsonObject();
                lastCustomerId = lastCustomer.get("customerId").getAsInt();
                for (JsonElement element : jsonArray) {
                    JsonObject customer = element.getAsJsonObject();
                    double amountPaid = customer.get("amountPaid").getAsDouble();
                    totalIncome += amountPaid;
                }
            } else {
                lastCustomerId = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLastCustomerId(int lastCustomerId) {
        CustomerObject.lastCustomerId = lastCustomerId;
    }

    public static int getLastCustomerId() {
        return lastCustomerId;
    }

    public static double getTotalIncome() {return totalIncome;}

    public int getCustomerId() {
        return customerId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }
}

