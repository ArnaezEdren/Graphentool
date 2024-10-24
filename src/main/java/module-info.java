module at.spengergasse.fx_template {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.spengergasse.fx_template to javafx.fxml;
    exports at.spengergasse.fx_template;
}