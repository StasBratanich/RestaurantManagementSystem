package com.hit.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hit.model.ProductObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductObjectDAO implements IDAO<ProductObject> {
    private static final String FILE_PATH = "productData.json";

    public List<ProductObject> loadProductData() {
        Gson gson = new Gson();
        List<ProductObject> products = new ArrayList<>();

        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            products = gson.fromJson(fileReader, new TypeToken<List<ProductObject>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void saveProductData(List<ProductObject> products) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            gson.toJson(products, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendDataToFile(ProductObject productObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            ProductObject[] existingData = gson.fromJson(fileReader, ProductObject[].class);
            List<ProductObject> updatedData = new ArrayList<>();

            if (existingData != null) {
                Collections.addAll(updatedData, existingData);
            }

            updatedData.add(productObject);

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

    public void deleteProduct(ProductObject product) {
        Gson gson = new Gson();
        List<ProductObject> products = loadProductData();
        products.removeIf(p -> p.getProduct_id().equals(product.getProduct_id()));
        saveProductData(products);
    }

    @Override
    public void appendData(ProductObject object) {

    }

    @Override
    public List<ProductObject> readData() {
        return null;
    }

    @Override
    public void deleteData(ProductObject object) {

    }
}
