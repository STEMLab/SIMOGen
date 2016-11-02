/*
 * Indoor Moving Objects Generator
 * Copyright (c) 2016 Pusan National University
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package edu.pnu.util.random;

import java.util.Random;

/**
 * @author hgryoo
 *
 * <pre>
 * Example Code : 
 * <code>
 * double mean = 100.0f;
 * double variance = 5.0f;
 * GaussianRNG gaussian = new GaussianRNG(new Random(), mean, variance);
 * double result = gaussian.getDouble();
 * </code>
 * result :
 *      99.38221153454624
 * </pre>
 * 
 */
public class GaussianRNG {
    private Random rng;
    private double mean;
    private double variance;
    
    public GaussianRNG(Random rng, double mean, double variance) {
        this.rng = rng;
        this.mean = mean;
        this.variance = variance;
    }
    
    public double getDouble() {
        return mean + rng.nextGaussian() * variance;
    }
}
