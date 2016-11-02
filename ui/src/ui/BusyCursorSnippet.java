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
package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author hgryoo
 *
 */
public class BusyCursorSnippet {
    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());
        final Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));
        final int[] nextId = new int[1];
        Button b = new Button(shell, SWT.PUSH);
        b.setText("invoke long running job");
        b.addSelectionListener(
                new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        Runnable longJob = new Runnable() {
                                boolean done = false;
                                int id;
                                @Override
                                public void run() {
                                        Thread thread = new Thread() {
                                            
                                                id = nextId[0]++;
                                                display.syncExec(() -> {
                                                        if (text.isDisposed()) return;
                                                        text.append("\nStart long running task "+id);
                                                });
                                                for (int i = 0; i < 100000; i++) {
                                                        if (display.isDisposed()) return;
                                                        System.out.println("do task that takes a long time in a separate thread "+id);
                                                }
                                                if (display.isDisposed()) return;
                                                display.syncExec(() -> {
                                                        if (text.isDisposed()) return;
                                                        text.append("\nCompleted long running task "+id);
                                                });
                                                done = true;
                                                display.wake();
                                        });
                                        thread.start();
                                        while (!done && !shell.isDisposed()) {
                                                if (!display.readAndDispatch())
                                                        display.sleep();
                                        }
                                }
                        };
                        BusyIndicator.showWhile(display, longJob);
                }
                });
        shell.setSize(250, 150);
        shell.open();
        while (!shell.isDisposed()) {
                if (!display.readAndDispatch())
                        display.sleep();
        }
        display.dispose();
}
}
