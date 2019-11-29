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

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * @author hgryoo
 *
 */
public class CalendarSnippet {
    public static void main (String [] args) {
        Display display = new Display ();
        final Shell shell = new Shell (display);
        shell.setLayout(new FillLayout());

        Button open = new Button (shell, SWT.PUSH);
        open.setText ("Open Dialog");
        open.addSelectionListener (new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM);
                dialog.setLayout (new GridLayout (3, false));

                final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
                final DateTime date = new DateTime (dialog, SWT.DATE | SWT.SHORT);
                final DateTime time = new DateTime (dialog, SWT.TIME | SWT.SHORT);

                new Label (dialog, SWT.NONE);
                new Label (dialog, SWT.NONE);
                Button ok = new Button (dialog, SWT.PUSH);
                ok.setText ("OK");
                ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
                ok.addSelectionListener (new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                                System.out.println ("Calendar date selected (MM/DD/YYYY) = " + (calendar.getMonth () + 1) + "/" + calendar.getDay () + "/" + calendar.getYear ());
                                System.out.println ("Date selected (MM/YYYY) = " + (date.getMonth () + 1) + "/" + date.getYear ());
                                System.out.println ("Time selected (HH:MM) = " + time.getHours () + ":" + (time.getMinutes () < 10 ? "0" : "") + time.getMinutes ());
                                dialog.close ();
                    }
                });
                dialog.setDefaultButton (ok);
                dialog.pack ();
                dialog.open ();
            }
                
        
        
        }
        );
        shell.pack ();
        shell.open ();

        while (!shell.isDisposed ()) {
                if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
}
}
