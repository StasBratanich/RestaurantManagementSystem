package com.hit.controller;

import com.hit.dao.UserObjectDAO;
import com.hit.driver.AppController;
import com.hit.model.UserObject;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EntranceFormController implements Initializable {

    @FXML
    private PasswordField change_password_confirmation_password;

    @FXML
    private PasswordField change_password_new_password;

    @FXML
    private Button create_already_have_btn;

    @FXML
    private Button create_create_btn;

    @FXML
    private TextField forgot_answer;

    @FXML
    private TextField forgot_username;

    @FXML
    private ComboBox<?> forgot_question;

    @FXML
    private AnchorPane form_change_password;

    @FXML
    private AnchorPane form_create;

    @FXML
    private AnchorPane form_forgot_password;

    @FXML
    private AnchorPane form_login;

    @FXML
    private Button login_login_btn;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_username;

    @FXML
    private TextField register_answer;

    @FXML
    private PasswordField register_password;

    @FXML
    private ComboBox<?> register_question;

    @FXML
    private TextField register_username;

    private Alert alert;

    private String[] questionList = {"What is your favorite color?", "What is your pet's name?", "What is your favorite food?", "Witch CS course is your favorite?"};

    // --------------------------------------------------------------------------------------------------------

    // FUNCTION TO REGISTER USERS VIA **LOGIN** BUTTON

    public void loginBtn() throws IOException {
        if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else {
            String username = login_username.getText();
            String password = login_password.getText();

            if (UserObjectDAO.isUserExists(username, password)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Login!");
                alert.showAndWait();

                FXMLLoader fxmlLoader = new FXMLLoader(AppController.class.getResource("MainForm.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
                stage.setTitle("Restaurant Management System");
                stage.setMinHeight(720);
                stage.setMinWidth(1280);
                stage.setScene(scene);
                stage.show();

                login_login_btn.getScene().getWindow().hide();
                stage = null;

            } else {
                if(!UserObjectDAO.isUsernameExists(username)) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Username does not exist!");
                    alert.showAndWait();

                    TranslateTransition slider = new TranslateTransition();
                    slider.setNode(form_create);
                    slider.setToX(300);
                    slider.setDuration(Duration.seconds(.5));

                    slider.setOnFinished((ActionEvent e) -> {
                        create_already_have_btn.setVisible(true);
                        create_create_btn.setVisible(false);

                        form_login.setVisible(true);

                        registerQuestionList();
                    });
                    slider.play();
                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect password!");
                    alert.showAndWait();
                }
            }
        }
    }

    public void registerBtn() {
        if (register_username.getText().isEmpty()
                || register_password.getText().isEmpty()
                || register_answer.getText().isEmpty()
                || register_question.getSelectionModel().getSelectedItem() == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String username = register_username.getText();
            String password = register_password.getText();
            String answer = register_answer.getText();
            String question = register_question.getSelectionModel().getSelectedItem().toString();

            if (password.length() < 8) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Password must be at least 8 characters long");
                alert.showAndWait();
            } else {
                if (UserObjectDAO.isUsernameExists(username)) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Username already exists");
                    alert.showAndWait();
                } else {
                    UserObject userObject = new UserObject(username, password, answer, question);

                    File file = new File(UserObjectDAO.getFilePath());
                    if (file.exists()) {
                        UserObjectDAO.appendDataToFile(userObject);
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Registered Account!");
                        alert.showAndWait();
                    }
                }
            }
        }
    }

    // FUNCTION THAT SWITCHES THE FORMS WHEN **FORGOT PASSWORD** BTN IS PRESSED

    public void switchForgotPass(){
        form_forgot_password.setVisible(true);
        form_login.setVisible(false);

        forgotPasswordQuestionList();
    }

    public void switchChangePass(){
        form_change_password.setVisible(true);
        form_forgot_password.setVisible(false);
        form_login.setVisible(false);
    }

    public void proceedBtn() {
        if (forgot_username.getText().isEmpty() || forgot_answer.getText().isEmpty() || forgot_question.getSelectionModel().getSelectedItem() == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else{
            String username = forgot_username.getText();
            String answer = forgot_answer.getText();
            String question = forgot_question.getSelectionModel().getSelectedItem().toString();

            if (UserObjectDAO.isQuestionRight(username, question, answer)) { // the answer is right
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("The answer was right!");
                alert.showAndWait();

                switchChangePass();
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Wrong information!");
                alert.showAndWait();
            }
        }
    }

    public void changePasswordBtn(){
        String newPassword = change_password_confirmation_password.getText();

        if(change_password_new_password.getText().isEmpty() || change_password_confirmation_password.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else if (newPassword.length() < 8) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 8 characters long");
            alert.showAndWait();
        } else if (!change_password_new_password.getText().equals(change_password_confirmation_password.getText())) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match!");
            alert.showAndWait();
        }else {
            String username = forgot_username.getText(); // Use the correct username field
            boolean passwordChanged = UserObjectDAO.changeNewPassword(username, newPassword);
            if (passwordChanged) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Password has been changed successfully");
                alert.showAndWait();
                loginNewPassword();
            }
        }
    }

    public void changePasswordBackBtn(){
        form_change_password.setVisible(false);
        form_forgot_password.setVisible(true);
        form_login.setVisible(false);
    }

    public void forgotPasswordBackBtn(){
        form_change_password.setVisible(false);
        form_forgot_password.setVisible(false);
        form_login.setVisible(true);
    }

    public void loginNewPassword(){
        form_change_password.setVisible(false);
        form_forgot_password.setVisible(false);
        form_login.setVisible(true);
    }

    public void forgotPasswordQuestionList(){
        List<String> Qlist = new ArrayList<>();

        for(String data: questionList){
            Qlist.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(Qlist);
        forgot_question.setItems(listData);
    }

    // FUNCTION THAT SWITCHES THE FORMS WHEN **CREATE NEW ACCOUNT** BTN IS PRESSED

    // FUNCTION FOR THE COMBO BOX QUESTION
    public void registerQuestionList(){
        List<String> Qlist = new ArrayList<>();

        for(String data: questionList){
            Qlist.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(Qlist);
        register_question.setItems(listData);
    }

    @FXML
    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == create_create_btn) {
            slider.setNode(form_create);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                create_already_have_btn.setVisible(true);
                create_create_btn.setVisible(false);

                form_login.setVisible(true);

                registerQuestionList();
            });
            slider.play();

        } else if (event.getSource() == create_already_have_btn){
            slider.setNode(form_create);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                create_already_have_btn.setVisible(false);
                create_create_btn.setVisible(true);

                form_login.setVisible(true);

            });
            slider.play();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

    }
}