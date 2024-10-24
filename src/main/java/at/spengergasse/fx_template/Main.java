package at.spengergasse.fx_template;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


public class Main extends Application {
    public void start(Stage primaryStage) {
        try {
            RootBorderPane root = new RootBorderPane();
            Scene scene = new Scene(root, 1000d, 875d);

            primaryStage.setTitle("Graphentool");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            showAlert(e.getMessage());
        }
    }

    public static void showAlert(String message) {
        Alert alert;
        if (message != null && !message.isEmpty()) {
            alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        } else {
            alert = new Alert(AlertType.INFORMATION, "Bitte mit \"Weiter\" bestaetigen", ButtonType.NEXT);
        }
        alert.setHeaderText(null);
        alert.setTitle("Hinweis-Meldung");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
