/*
 * MIT License
 *
 * Copyright (c) 2016 John Joshua Ferrer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ga.gaven.ui;

import ga.gaven.Application;
import ga.gaven.SignalEncoding;
import ga.gaven.StringByteEncoder;
import ga.gaven.StringEncoding;
import ga.gaven.charts.Clock;
import ga.gaven.charts.DifferentialManchester;
import ga.gaven.charts.DigitalSignal;
import ga.gaven.charts.Manchester;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Gavenchi <johnjoshuaferrer@gmail.com>
 */
public class ApplicationUI {

    private Application application;

    @FXML
    private TextArea txtDataInput;
    @FXML
    private TextArea txtASCII;
    @FXML
    private TextArea txtASCIIHex;
    @FXML
    private TextArea txtASCIIOctal;
    @FXML
    private TextArea txtEBCDIC;
    @FXML
    private TextArea txtEBCDICHex;
    @FXML
    private TextArea txtEBCDICOctal;
    @FXML
    private Label txtStatus;
    @FXML
    private ComboBox<String> cmbEncoding;
    @FXML
    private ComboBox<String> cmbEncodingValues;
    @FXML
    private HBox panelEncode;
    @FXML
    private ScrollBar scrollLineChart;

    private final StringByteEncoder encoder = new StringByteEncoder();

    private final ObservableList<String> values = FXCollections.observableArrayList(
            StringEncoding.ASCII,
            StringEncoding.EBCDIC
    );

    private final ObservableList<String> encodings = FXCollections.observableArrayList(
            SignalEncoding.MANCHESTER,
            SignalEncoding.DIFFERENTIAL_MANCHESTER,
            SignalEncoding.BIPOLAR_NRI,
            SignalEncoding.BIPOLAR_NRZL,
            SignalEncoding.BIPOLAR_AMI
    );

    private final NumberAxis time = new NumberAxis();
    private final NumberAxis signal = new NumberAxis(-2, 5, 1);
    private final LineChart<Number,Number> chart = new LineChart<>(time, signal);

    // signals
    private final Clock clock = new Clock();

    private static final int RANGE = 10;

    @FXML
    private void initialize() {
        // initialize axes
        time.setAutoRanging(false);
        time.setUpperBound(RANGE);
        time.setTickUnit(1);

        txtStatus.setText("");

        // default values
        cmbEncoding.setItems(encodings);
        cmbEncoding.setValue(encodings.get(0));
        cmbEncodingValues.setItems(values);
        cmbEncodingValues.setValue(values.get(0));

        // chart
        time.setLabel("Time");
        signal.setLabel("Signal");
        chart.setCreateSymbols(false);
        chart.setPadding(new Insets(0, 10, 0, 0));
        chart.setPrefWidth(Integer.MAX_VALUE);
        chart.setMaxWidth(Integer.MAX_VALUE);
        chart.setAnimated(false);
        panelEncode.getChildren().add(chart);
        scrollLineChart.setVisible(false);

        // listeners
        cmbEncodingValues.valueProperty().addListener((observable, oldValue, newValue) -> updateChart(dataResult(newValue), cmbEncoding.getValue()));
        cmbEncoding.valueProperty().addListener((observable, oldValue, newValue) -> updateChart(dataResult(cmbEncodingValues.getValue()), newValue));

        scrollLineChart.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(clock.maximum() > RANGE) {
                time.setLowerBound(newValue.doubleValue());
                time.setUpperBound(newValue.doubleValue() + RANGE);
                scrollLineChart.setMax(clock.maximum() - RANGE);
            }
        });

        chart.setOnScroll(event -> {
            double negativity = event.getDeltaY() / Math.abs(event.getDeltaY());
            double result = scrollLineChart.getValue() - ((RANGE / 2) * negativity);
            if(result < scrollLineChart.getMin()) {
                scrollLineChart.setValue(scrollLineChart.getMin());
            } else if (result > scrollLineChart.getMax()) {
                scrollLineChart.setValue(scrollLineChart.getMax());
            } else {
                scrollLineChart.setValue(result);
            }
        });

        txtDataInput.textProperty().addListener((observable, oldValue, newValue) -> {
            // ascii
            StringByteEncoder.Result ascii = encoder.toASCII(newValue);

            txtASCII.setText(Arrays.toString(ascii.inBits()));
            txtASCIIHex.setText(Arrays.toString(ascii.inHex()));
            txtASCIIOctal.setText(Arrays.toString(ascii.inOctal()));

            // ebcdic
            StringByteEncoder.Result ebcdic = encoder.toEBCDIC(newValue);

            txtEBCDIC.setText(Arrays.toString(ebcdic.inBits()));
            txtEBCDICHex.setText(Arrays.toString(ebcdic.inHex()));
            txtEBCDICOctal.setText(Arrays.toString(ebcdic.inOctal()));

            // chart
            updateChart(dataResult(cmbEncodingValues.getValue()), cmbEncoding.getValue());
        });
    }

    private StringByteEncoder.BitResult dataResult(String type) {
        return type.equals(StringEncoding.ASCII) ? new StringByteEncoder.BitResult(encoder.toASCII(txtDataInput.getText())) : new StringByteEncoder.BitResult(encoder.toEBCDIC(txtDataInput.getText()));
    }

    private void updateChart(StringByteEncoder.BitResult result, String encoding) {
        chart.getData().clear();
        chart.getData().add(clock.generateSeries(result));
        chart.getData().add(new DigitalSignal().generateSeries(result));

        switch(encoding) {
            case SignalEncoding.MANCHESTER:
                chart.getData().add(new Manchester().generateSeries(result));
                break;
            case SignalEncoding.DIFFERENTIAL_MANCHESTER:
                chart.getData().add(new DifferentialManchester().generateSeries(result));
                break;
            case SignalEncoding.BIPOLAR_NRZL:
                break;
            case SignalEncoding.BIPOLAR_NRI:
                break;
            case SignalEncoding.BIPOLAR_AMI:
                break;
        }

    }

    public void attachApplication(Application application) {
        this.application = application;
    }

    @FXML
    public void onApplicationQuit(ActionEvent event) throws Exception {
        application.stop();
    }

    @FXML
    public void onAboutAction(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        alert.setTitle("About");
        alert.setHeaderText("CpEForWanZ - A CPE 410 Project");

        Label desc = new Label("An open source project, visit: https://github.com/Gavenchi/cpeforwanz/ to contribute.");

        desc.setPadding(new Insets(0, 0, 10, 0));

        Label lics = new Label("Software License");

        //using stream to avoid file not found exception when built as executable
        String license = new Scanner(getClass().getResourceAsStream("/LICENSE"), "UTF-8")
                .useDelimiter("\\Z").next();

        TextArea textArea = new TextArea(license);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(desc, 0, 0);
        expContent.add(lics, 0, 1);
        expContent.add(textArea, 0, 2);

        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();
    }
}
