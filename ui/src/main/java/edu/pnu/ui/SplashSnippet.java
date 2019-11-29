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
package edu.pnu.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * @author hgryoo
 *
 */
public class SplashSnippet {
    public static void main(String[] args) {
        final Display display = new Display();
        final int [] count = new int [] {4};
        final Image image = new Image(display, 300, 300);
        GC gc = new GC(image);
        gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
        gc.fillRectangle(image.getBounds());
        gc.drawText("Splash Screen", 10, 10);
        gc.dispose();
        final Shell splash = new Shell(SWT.ON_TOP);
        final ProgressBar bar = new ProgressBar(splash, SWT.NONE);
        bar.setMaximum(count[0]);
        Label label = new Label(splash, SWT.NONE);
        label.setImage(image);
        FormLayout layout = new FormLayout();
        splash.setLayout(layout);
        FormData labelData = new FormData ();
        labelData.right = new FormAttachment (100, 0);
        labelData.bottom = new FormAttachment (100, 0);
        label.setLayoutData(labelData);
        FormData progressData = new FormData ();
        progressData.left = new FormAttachment (0, 5);
        progressData.right = new FormAttachment (100, -5);
        progressData.bottom = new FormAttachment (100, -5);
        bar.setLayoutData(progressData);
        splash.pack();
        Rectangle splashRect = splash.getBounds();
        Rectangle displayRect = display.getBounds();
        int x = (displayRect.width - splashRect.width) / 2;
        int y = (displayRect.height - splashRect.height) / 2;
        splash.setLocation(x, y);
        splash.open();
        display.asyncExec( new Runnable() {

            public void run() {
                Shell [] shells = new Shell[count[0]];
                for (int i1=0; i1<count[0]; i1++) {
                        shells [i1] = new Shell(display);
                        shells [i1].setSize (300, 300);
                        shells [i1].addListener(SWT.Close, new Listener() {
                            public void handleEvent(Event e) {
                                --count[0];
                            }  
                        });
                        
                        bar.setSelection(i1+1);
                        try {Thread.sleep(1000);} catch (Throwable e) {}
                }
                splash.close();
                image.dispose();
                for (int i2=0; i2<count[0]; i2++) {
                        shells [i2].open();
                }
            }
        });
                
        while (count [0] != 0) {
                if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose();
}
}
