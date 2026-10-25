module com.example.miageholding {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    // Exemples de bibliothèques optionnelles (à ajuster selon ton projet)
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // Exportation et ouverture des modules
    opens com.example.miageholding to javafx.fxml;
    exports com.example.miageholding;

    opens com.example.miageholding.controllers to javafx.fxml;
    exports com.example.miageholding.controllers;

    opens com.example.miageholding.controllers.administrateur to javafx.fxml;
    exports com.example.miageholding.controllers.administrateur;

    opens com.example.miageholding.controllers.auth to javafx.fxml;
    exports com.example.miageholding.controllers.auth;

    opens com.example.miageholding.controllers.responsablelogistique to javafx.fxml;
    exports com.example.miageholding.controllers.responsablelogistique;

    opens com.example.miageholding.core.dao to javafx.fxml;
    exports com.example.miageholding.core.dao;

    opens com.example.miageholding.models to javafx.base;
    exports com.example.miageholding.models;

    opens com.example.miageholding.models.dto to javafx.base;
    exports com.example.miageholding.models.dto;

    opens com.example.miageholding.utils to javafx.fxml;
    exports com.example.miageholding.utils;
}
