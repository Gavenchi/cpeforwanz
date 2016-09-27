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

package ga.gaven;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Gavenchi <johnjoshuaferrer@gmail.com>
 */
public class StringByteEncoder {

    public class Result {
        final String[] bits;
        final String[] hex;
        final String[] octal;

        private Result(String[] bits, String[] hex, String[] octal) {
            this.bits = bits;
            this.hex = hex;
            this.octal = octal;
        }

        public String[] inBits() {
            return bits;
        }

        public String[] inHex() {
            return hex;
        }

        public String[] inOctal() {
            return octal;
        }
    }

    public static class BitResult {
        final byte[] bits;

        public BitResult(Result result) {
            String[] sBits = result.inBits();
            bits = new byte[sBits.length * 8];

            int idx = 0;

            for(int i = 0; i < sBits.length; i++) {
                for(char c : sBits[i].toCharArray()) {
                    bits[idx++] = (byte)((c == '0') ? 0 : 1);
                }
            }
        }

        public byte[] bitResult() {
            return bits;
        }
    }

    public Result toEBCDIC(String s) {
        byte[] bytes = toEBCDICBytes(s);
        String[] b = new String[bytes.length];
        String[] h = new String[bytes.length];
        String[] o = new String[bytes.length];

        for(int i = 0; i < bytes.length; i++) {
            b[i] = Integer.toBinaryString(bytes[i] & 0xFF);
            h[i] = Integer.toHexString(bytes[i] & 0xFF).toUpperCase();
            o[i] = Integer.toOctalString(bytes[i] & 0xFF);
        }

        return new Result(b, h, o);
    }

    public Result toASCII(String s) {
        byte[] bytes = toASCIIBytes(s);
        String[] b = new String[bytes.length];
        String[] h = new String[bytes.length];
        String[] o = new String[bytes.length];

        for(int i = 0; i < bytes.length; i++) {
            b[i] = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
            h[i] = Integer.toHexString(bytes[i]).toUpperCase();
            o[i] = Integer.toOctalString(bytes[i]);
        }

        return new Result(b, h, o);
    }

    public byte[] toEBCDICBytes(String s) {
        return s.getBytes(Charset.forName("Cp500"));
    }

    public byte[] toASCIIBytes(String s) {
        return s.getBytes(StandardCharsets.US_ASCII);
    }

}
