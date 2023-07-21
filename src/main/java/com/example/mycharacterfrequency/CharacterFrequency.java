package com.example.mycharacterfrequency;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import java.lang.Math;


public class CharacterFrequency extends Application {
    //Instant variables
    Integer N;              // Number of events [chars] to display
    Integer M;              // Max of events [chars] to display [26]

    double startAngle;
    double scale;

    String Title;
    String filename;
    Scanner input;

    //Inputs to dialog boxes
    Boolean isPiechart;
    List<String> piechartInputs = new ArrayList();


    public void toggleGroup() {
        ToggleGroup group = new ToggleGroup();

        RadioButton radioPiechart = new RadioButton("Pie Chart");
        radioPiechart.setToggleGroup(group);

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Chart Picker");
        dialog.setHeaderText(null);

        //Set button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //Username and password labels and fields
        GridPane gridDialog = new GridPane();
        gridDialog.setHgap(10);
        gridDialog.setVgap(10);
        gridDialog.setPadding(new Insets(20, 200, 20, 10));

        gridDialog.add(radioPiechart, 1, 0);

        dialog.getDialogPane().setContent(gridDialog);

        //Request focus on and select radioPieChart by default
        Platform.runLater(() -> radioPiechart.setSelected(true));

        //Convert result to Boolean when OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dialogButton.OK) {
                this.isPiechart = radioPiechart.isSelected();
                if (this.isPiechart) {dialogPiechart();} //else {dialogBarchart();}
            }
            return null;
        });
        Optional<Boolean> Result = dialog.showAndWait();
    }

    public void dialogPiechart() {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Pie Chart");
        dialog.setHeaderText(null);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridDialog = new GridPane();
        gridDialog.setHgap(10);
        gridDialog.setVgap(10);
        gridDialog.setPadding(new Insets(20, 150, 10, 10));

        TextField numberEvents = new TextField();
        TextField totalNumberEvents = new TextField();
        TextField startingAngle = new TextField();

        ComboBox title = new ComboBox();
        title.getItems().addAll("Alice in Wonderland", "A Tale of Two Cities", "David Copperfield",
                "Emma", "Moby Dick", "Oliver Twist", "Pride and Prejudice", "War and Peace", "xWords");

        gridDialog.add(new Label("Display"), 0, 0);
        gridDialog.add(numberEvents, 1, 0);
        gridDialog.add(new Label("Total"), 2, 0);
        gridDialog.add(totalNumberEvents, 3, 0);
        gridDialog.add(new Label("Starting Angle"), 0, 1);
        gridDialog.add(startingAngle, 1, 1);
        gridDialog.add(new Label("Title"), 0, 2);
        gridDialog.add(title, 1, 2);

        dialog.getDialogPane().setContent(gridDialog);

        //Request focus on numberEvents field by default
        Platform.runLater(() -> numberEvents.requestFocus());

        //Convert result to Boolean when OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dialogButton.OK) {
                piechartInputs.add(numberEvents.getText());
                piechartInputs.add(totalNumberEvents.getText());
                piechartInputs.add(startingAngle.getText());
                piechartInputs.add(title.getValue().toString());

                return piechartInputs;
            }
            return null;
        });

        Optional<List<String>> Result = dialog.showAndWait();

        Result.ifPresent(event -> {
            this.N = Integer.parseInt(piechartInputs.get(0));
            this.M = Integer.parseInt(piechartInputs.get(1));
            this.startAngle = Double.parseDouble(piechartInputs.get(2));
            this.Title = piechartInputs.get(3);
            this.filename = "C:\\Users\\rtara\\OneDrive\\Documents\\CCNY\\2023 Summer Term\\CSC 221 Software Design\\" +
                    "Assignment 3 - mycharacterfrequency\\mycharacterfrequency\\Texts\\" + Title + ".txt";
        });
    }


    public void openFile() {
        try {
            input = new Scanner(Paths.get(filename));
        } catch (IOException ioException) {
            System.err.println("File is not found");
        }
    }

    public String readFile() {
        String w = "";

        try {
            //Read in file, taking out all non-alphabet characters
            while (input.hasNext()) {
                w += input.nextLine().replaceAll("[^a-zA-Z]", "").toLowerCase();
            }
        }
        catch (NoSuchElementException elementException) {
            System.err.println("Invalid input! Terminating...");
        }
        catch (IllegalStateException stateException) {
            System.err.println("Error processing file! Terminating...");
        }
        return w;
    }

    public void closeFile() {
        if (input != null) input.close();
    }


    //Add canvases for legend and chart
    public Canvas addCanvasLegend(double widthCanvas, double heightCanvas, HistogramAlphaBet H) {
        String information;

        Canvas CV = new Canvas(widthCanvas, heightCanvas);
        GraphicsContext GC = CV.getGraphicsContext2D();

        //Paint background of canvas
        MyColor colorLeftCanvas = MyColor.LINEN;
        GC.setFill(colorLeftCanvas.getJavaFXColor());
        GC.fillRect(0, 0, widthCanvas, heightCanvas);

        //Output character frequencies
        double xText = 20;
        double yText = 0.03625 * heightCanvas;
        MyColor colorStroke = MyColor.GREY;
        GC.setStroke(colorStroke.invertColor());
        GC.setFont(Font.font("Calibri", 13));
        GC.strokeText("Frequency: Cumulative " + H.getCumulativeFrequency(), xText, yText);

        Map<Character, Integer> sortedFrequency = H.sortDownFrequency();

        double yStep = yText;
        for (Character K : sortedFrequency.keySet()) {
            yText += yStep;
            information = K + ":\t" + sortedFrequency.get(K);
            GC.strokeText(information, xText, yText);
        }

        return CV;
    }

    public Canvas addCanvasPieChart(double widthCanvas, double heightCanvas, HistogramAlphaBet H) {
        Canvas CV = new Canvas(widthCanvas, heightCanvas);
        GraphicsContext GC = CV.getGraphicsContext2D();

        MyPoint center = new MyPoint(0.5 * widthCanvas, 0.5 * heightCanvas, null);

        //Build pie chart
        double diameterPieChart = 0.75 * Math.min(widthCanvas, heightCanvas);
        HistogramAlphaBet.MyPieChart pieChart = H.new MyPieChart(N, M, center, diameterPieChart, diameterPieChart, startAngle);
        Map<Character, Slice> slices = pieChart.getMyPieChart();

        //Output pie chart
        System.out.println("\nPie Chart");
        slices.forEach((K, V) -> System.out.println(K + ": " + slices.get(K)));

        //Check for sum of slice angles == 360
        double sumAngles = 0.0;
        for (Character key : slices.keySet()) {
            sumAngles += slices.get(key).getArcAngle();
        }

        //Draw pie chart
        pieChart.draw(GC);

        return CV;
    }


    @Override
    public void start(Stage stage) {
        //Create custom dialog for data input
        toggleGroup();

        //open, read, close files
        openFile();
        String w = readFile();
        closeFile();

        HistogramAlphaBet H = new HistogramAlphaBet(w);
        Map<Character, Integer> sortedFrequency = H.sortDownFrequency();

        //output calculated and sum of frequencies
        System.out.println("\nFrequency of Characters");
        sortedFrequency.forEach((K, V) -> System.out.println(K + ": " + V));
        System.out.println("\nCumulative Frequency: " + H.getCumulativeFrequency());

        //output calculated and sum of probabilities
        System.out.println("\nSorted Probability of Characters");
        System.out.println(H.sortDownProbability());
        System.out.println("\nSum of Probabilities: " + H.getSumOfProbability());

        BorderPane BP = new BorderPane();
        Pane leftP = new Pane();
        Pane centerP = new Pane();

        double widthCanvas = 800.0; double heightCanvas = 400.0;

        double widthLeftCanvas = 0.275 * widthCanvas;
        double widthCenterCanvas = widthCanvas - widthLeftCanvas;

        leftP.getChildren().add(addCanvasLegend(widthLeftCanvas, heightCanvas, H));
        BP.setLeft(leftP);

       if (isPiechart) {
           centerP.getChildren().add(addCanvasPieChart(widthCenterCanvas, heightCanvas, H));
       }

       BP.setCenter(centerP);

        Scene SC = new Scene(BP, widthCanvas, heightCanvas, MyColor.WHITE.getJavaFXColor());
        stage.setTitle("Frequency of Characters: " + Title);
        stage.setScene(SC);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


