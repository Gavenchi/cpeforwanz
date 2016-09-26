package ga.gaven;

/**
 * Created by t11_412ng4 on 9/26/2016.
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CpEForWanz extends Application {

    private Stage primaryStage;
    private VBox root;

    public static void main(String[] args) {
        launch(args);
    }

    public void addLowToHigh(XYChart.Series series) {
        int nextIndex = series.getData().size();

        series.getData().add(new XYChart.Data(nextIndex, 0));
        series.getData().add(new XYChart.Data(nextIndex, 1));
        series.getData().add(new XYChart.Data(nextIndex + 1, 1));
    }

    public void addHigh(XYChart.Series series) {
        int nextIndex = series.getData().size();

//        XYChart.Data prevData = (XYChart.Data) series.getData().get(nextIndex - 1);
//        if(prevData != null && (Integer) prevData.getYValue() == 0) {
//
//        }


        series.getData().add(new XYChart.Data(nextIndex, 1));
        series.getData().add(new XYChart.Data(nextIndex + 1, 1));
    }

    public void addLow(XYChart.Series series) {
        int nextIndex = series.getData().size();

        series.getData().add(new XYChart.Data(nextIndex, 0));
        series.getData().add(new XYChart.Data(nextIndex + 1, 0));
    }

    public void addHighToLow(XYChart.Series series) {
        int nextIndex = series.getData().size();

        series.getData().add(new XYChart.Data(nextIndex, 1));
        series.getData().add(new XYChart.Data(nextIndex, 0));
        series.getData().add(new XYChart.Data(nextIndex + 1, 0));
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CPE 410");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/cpeforwanz.fxml"));
            root = loader.load();

            //defining the axes
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis(0, 5, 1);
            xAxis.setLabel("Time");
            //creating the chart
            final LineChart<Number,Number> chart =
                    new LineChart<Number,Number>(xAxis,yAxis);

            chart.setTitle("xxx Encoding");
            chart.setCreateSymbols(false);

            //defining a series
            XYChart.Series series = new XYChart.Series();
            series.setName("Signal");
            //populating the series with data
            for(int i = 0; i < 10; i++) {
                //addHigh(series);
                addHighToLow(series);
                addLowToHigh(series);
                //addHigh(series);
                //addHigh(series);
            }


            SplitPane pane = (SplitPane) root.getChildren().get(1);
            ScrollPane middlePane = (ScrollPane) pane.getItems().get(1);
            middlePane.setFitToHeight(true);
            middlePane.setFitToWidth(true);

            HBox hbox = (HBox) middlePane.getContent();
            hbox.setAlignment(Pos.CENTER);

            hbox.getChildren().add(chart);

            chart.getData().add(series);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
    }
}
