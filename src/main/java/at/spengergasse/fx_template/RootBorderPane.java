package at.spengergasse.fx_template;

import at.spengergasse.model.Graph;
import at.spengergasse.model.GraphException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;

public class RootBorderPane extends BorderPane {

    public GridPane textFeldGrid;
    public HBox untenLeiste, obenLeiste;
    public Button ladeButton, berechneButton, beendenButton;
    public Graph graph;
    public TextArea berechnungText, graphTextArea, untenText;


    public RootBorderPane() {
        initComponents();
        addComponents();
        addHandlers();
    }

    private void initComponents() {
        textFeldGrid = new GridPane();
        obenLeiste = new HBox();
        untenLeiste = new HBox();



        graph = new Graph();
        berechnungText = new TextArea();
        graphTextArea = new TextArea();
        berechneButton = new Button("Berechnen");
        ladeButton = new Button("Laden");
        beendenButton = new Button("Beenden");


        berechnungText.setPrefSize(400d, 800d);
        berechnungText.setDisable(true);
        graphTextArea.setPrefSize(600d, 800d);
        graphTextArea.setDisable(true);
        obenLeiste.setPrefSize(1000d, 30d);
        berechneButton.setDisable(true);
        obenLeiste.setPadding(new Insets(20));
        obenLeiste.setSpacing(10d);
        ladeButton.setPrefSize(200d, 20d);
        berechneButton.setPrefSize(200d, 20d);
        beendenButton.setPrefSize(200d, 20d);
        untenText = new TextArea("Graphentool von Edren Arnaez, 4BAIF");
        untenText.setPrefSize(1000d, 5d);
        untenLeiste.setDisable(true);

        setTop(obenLeiste);
        setCenter(textFeldGrid);
        setBottom(untenLeiste);
    }

    private void addComponents() {
        obenLeiste.getChildren().addAll(ladeButton, berechneButton, beendenButton);
        textFeldGrid.addColumn(0, graphTextArea);
        textFeldGrid.addColumn(1, berechnungText);
        untenLeiste.getChildren().add(untenText);

    }

    private void addHandlers() {
        ladeButton.setOnAction(event -> dateiLaden());
        berechneButton.setOnAction(event -> dieBerechnung());
        beendenButton.setOnAction(event -> dasBeenden());
    }

    private void dasBeenden() {
        Platform.exit();
    }

    private void dieBerechnung() {
        graph.berechnungDistanzMatrix();
        graph.herauslesenradiusdurchmesser();
        graph.kompWegMatrix();
        graph.findeBruecken();
        graph.findeArtikulation();
        String textBerechnung = graph.toStringBerechnung();
        berechnungText.setText(textBerechnung);
        berechnungText.setDisable(false);
        berechnungText.setEditable(false);
    }

    private void dateiLaden() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Laden");
        File selected = fileChooser.showOpenDialog(null);
        if (selected != null) {
            try {
                graph.einlesenCSV(selected.getAbsolutePath());
                String stringGraph = graph.toString();
                graphTextArea.setText(stringGraph);
                graphTextArea.setDisable(false);
                graphTextArea.setEditable(false);
                berechneButton.setDisable(false);
                berechnungText.clear();
            } catch (GraphException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
