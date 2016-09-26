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
import ga.gaven.StringByteEncoder;
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

    private StringByteEncoder encoder = new StringByteEncoder();

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

}
