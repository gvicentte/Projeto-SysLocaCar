package org.projetosyslocacar.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SysLocaCarController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
