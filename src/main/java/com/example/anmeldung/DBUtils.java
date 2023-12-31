package com.example.anmeldung;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.Connection;

import java.sql.*;


import java.io.IOException;


public class DBUtils {

    public static void changeScene(ActionEvent actionEvent, String fxmlFile, String title, String username, String favServices) {
        Parent root = null;

        if (username != null && favServices != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username, favServices);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signUpUser(ActionEvent actionEvent, String username, String password, String favServices) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/anmeldung", "root", "Konserativ1!");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("Benutzer existiert bereits!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Sie können diesen Benutzernamen nicht verwenden");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, favServices) VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, favServices);
                psInsert.executeUpdate();

                changeScene(actionEvent, "logged-in.fxml", "Welcome!", username, favServices);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {

                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            if (psCheckUserExists != null){
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psInsert != null) {
                try  {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    } public static void logInUser(ActionEvent actionEvent, String username, String password) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/anmeldung","root", "Konserativ1!");
            preparedStatement =connection.prepareStatement("SELECT password, favServices FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else{
                while (resultSet.next()){
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedServices = resultSet.getString("favServices");
                    if (retrievedPassword.equals(password)) {
                        changeScene(actionEvent, "logged-in.fxml", "Welcome!", username, retrievedServices);
                    } else {
                        System.out.println("Password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        alert.show();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }


}
