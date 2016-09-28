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

package ga.gaven.charts;

import ga.gaven.SignalEncoding;
import ga.gaven.StringByteEncoder;
import javafx.scene.chart.XYChart;

/**
 * @author Gavenchi <johnjoshuaferrer@gmail.com>
 */
public class DifferentialManchester extends Manchester {

    public DifferentialManchester() {
        series.setName(SignalEncoding.DIFFERENTIAL_MANCHESTER);
    }

    @Override
    public void lowToHigh() {
        series.getData().add(new XYChart.Data(idx, 1));
        series.getData().add(new XYChart.Data(idx, 1));
    }

    @Override
    public void highToLow() {
        series.getData().add(new XYChart.Data(idx, 0));
        series.getData().add(new XYChart.Data(idx, 0));
    }

    @Override
    public void low() {
        series.getData().add(new XYChart.Data(idx, 0));
        series.getData().add(new XYChart.Data(idx += 0.5, 0));
    }

    @Override
    public void high() {
        series.getData().add(new XYChart.Data(idx, 1));
        series.getData().add(new XYChart.Data(idx += 0.5, 1));
    }

    @Override
    public XYChart.Series generateSeries(StringByteEncoder.BitResult result) {
        this.idx = 0;
        this.series.getData().clear();

        byte prevBit = -1;
        boolean toggled = false;

        for(byte b : result.bitResult()) {
            if(prevBit == -1) {
                toggled = b != 0;
            } else if (prevBit != b) {
                toggled = !toggled;
            }

            if(toggled) manchesterLow(prevBit);
            else manchesterHigh(prevBit);

            prevBit = b;
        }

        return series;
    }

}
