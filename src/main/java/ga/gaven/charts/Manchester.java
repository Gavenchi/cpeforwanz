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

import ga.gaven.StringByteEncoder;
import javafx.scene.chart.XYChart;

/**
 * @author Gavenchi <johnjoshuaferrer@gmail.com>
 */
public class Manchester implements TimingDiagram {

    private final XYChart.Series series = new XYChart.Series<>();
    private double idx = 0;

    public Manchester() {
        series.setName("Manchester");
    }

    @Override
    public void lowToHigh() {
        series.getData().add(new XYChart.Data(idx, 0.5));
        series.getData().add(new XYChart.Data(idx, 0.5));
    }

    @Override
    public void highToLow() {
        series.getData().add(new XYChart.Data(idx, -0.5));
        series.getData().add(new XYChart.Data(idx, -0.5));
    }

    @Override
    public void low() {
        series.getData().add(new XYChart.Data(idx, -0.5));
        series.getData().add(new XYChart.Data(idx += 0.5, -0.5));
    }

    @Override
    public void high() {
        series.getData().add(new XYChart.Data(idx, 0.5));
        series.getData().add(new XYChart.Data(idx += 0.5, 0.5));
    }

    @Override
    public XYChart.Series generateSeries(StringByteEncoder.BitResult result) {
        this.idx = 0;
        this.series.getData().clear();

        for(byte b : result.bitResult()) {
            switch(b) {
                case 1:
                    low();
                    lowToHigh();
                    high();
                    break;
                default:
                    high();
                    highToLow();
                    low();
                    break;
            }
        }

        return series;
    }
}
