package com.hit.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hit.model.UserObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserObjectDAO implements IDAO<UserObject> {
    private static final String FILE_PATH = "usersData.json";

    public static boolean isUserExists(String username, String password) {
        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            UserObject[] userObjects = gson.fromJson(fileReader, UserObject[].class);
            if (userObjects != null) {
                for (UserObject userObject : userObjects) {
                    if (userObject.getUsername().equals(username) && userObject.getPassword().equals(password)) {
                        return true; // User exists and password is correct
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User does not exist or password is incorrect
    }

    public static boolean isUsernameExists(String username) {
        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            UserObject[] userObjects = gson.fromJson(fileReader, UserObject[].class);
            if (userObjects != null) {
                for (UserObject userObject : userObjects) {
                    if (userObject.getUsername().equals(username)) {
                        return true; // Username already exists
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Username does not exist
    }

    public static void appendDataToFile(UserObject userObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            UserObject[] existingData = gson.fromJson(fileReader, UserObject[].class);
            List<UserObject> updatedData = new ArrayList<>();

            if (existingData != null) {
                Collections.addAll(updatedData, existingData);
            }

            updatedData.add(userObject);

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

    public static boolean isQuestionRight(String username, String question, String answer) {
        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            UserObject[] userObjects = gson.fromJson(fileReader, UserObject[].class);
            if (userObjects != null) {
                for (UserObject userObject : userObjects) {
                    if (userObject.getUsername().equals(username) && userObject.getQuestion().equals(question) && userObject.getAnswer().equals(answer)) {
                        return true; // User exists and question/answer are correct
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User does not exist or question/answer are incorrect
    }

    public static boolean changeNewPassword(String username, String password) {
        if (password.length() < 8) {
            return false;
        }

        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            UserObject[] userObjects = gson.fromJson(fileReader, UserObject[].class);
            if (userObjects != null) {
                for (UserObject userObject : userObjects) {
                    if (userObject.getUsername().equals(username)) {
                        userObject.setPassword(password);
                        String updatedJson = gson.toJson(userObjects, UserObject[].class);

                        // Write the updated JSON data back to the file
                        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
                            fileWriter.write(updatedJson);
                            System.out.println("Password updated successfully");
                            return true; // Password has been changed
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User does not exist or failed to update password
    }

    public static String getFilePath() {
        return FILE_PATH;
    }

    @Override
    public void appendData(UserObject object) {

    }

    @Override
    public List<UserObject> readData() {
        return null;
    }

    @Override
    public void deleteData(UserObject object) {

    }
}
