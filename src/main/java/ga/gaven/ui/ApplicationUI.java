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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
    private TextArea txtEBCDIC;
    @FXML
    private Label txtStatus;
    @FXML
    private ComboBox<String> cmbEncoding;
    @FXML
    private ComboBox<String> cmbEncodingValues;
    @FXML
    private HBox panelEncode;

    private ObservableList<String> values = FXCollections.observableArrayList(
            "ASCII",
            "EBCDIC"
    );

    private ObservableList<String> encodings = FXCollections.observableArrayList(
            "Manchester",
            "Bipolar NRZL",
            "Bipolar NRI",
            "Differential Manchester",
            "Bipolar AMI"
    );

    public ApplicationUI() {

    }

    @FXML
    private void initialize() {
        txtStatus.setText("");

        txtDataInput.textProperty().addListener((observable, oldValue, newValue) -> {
            txtStatus.setText("Encoding...");
            txtASCII.setText(toASCIIBinary(newValue));
            txtEBCDIC.setText(toEBCDICBinary(newValue));
            txtStatus.setText("");
        });

        cmbEncoding.setItems(encodings);
        cmbEncoding.setValue(encodings.get(0));
        cmbEncodingValues.setItems(values);
        cmbEncodingValues.setValue(values.get(0));
    }

    public void attachApplication(Application application) {
        this.application = application;
    }

    public String toEBCDICBinary(String s) {
        byte[] bytes = s.getBytes(Charset.forName("Cp500"));

        String[] arr = new String[bytes.length];

        for(int i = 0; i < bytes.length; i++) {
            arr[i] = Integer.toBinaryString(bytes[i] & 0xFF);
        }

        return Arrays.toString(arr);
    }

    public String toASCIIBinary(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.US_ASCII);
        String[] arr = new String[bytes.length];

        for(int i = 0; i < bytes.length; i++) {
            arr[i] = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
        }

        return Arrays.toString(arr);
    }
}
