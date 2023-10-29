package com.example.anmeldung;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_logout;

    @FXML
    Label label_welcome;

    @FXML
    private Label label_fav_services;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "hello-view.fxml", "Log in!", null, null);

            }
        });

    }

    public void setUserInformation(String username, String favServices) {
        label_welcome.setText("Welcome " + username + "!");
        label_fav_services.setText("Please take your time to explore our services " + favServices + "!");
    }
}
