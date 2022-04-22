module queuemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports queuemanagement;
    opens queuemanagement to javafx.graphics;

    opens queuemanagement.model to javafx.fxml;
    exports queuemanagement.model;

    exports queuemanagement.controller;
    opens queuemanagement.controller to javafx.fxml;

}