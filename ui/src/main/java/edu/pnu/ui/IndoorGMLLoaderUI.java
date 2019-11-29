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
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author hgryoo
 *
 */
public class IndoorGMLLoaderUI {
    public static void main (String [] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        
        //initDialog(dialog);
        
        
        
        
        display.dispose();
    }
    
    public void initDialog(FileDialog dialog) {
        String [] filterNames = new String [] {"IndoorGML File", "All Files (*)"};
        String [] filterExtensions = new String [] {"*.xml;*.gml;*.igml;", "*"};
        String filterPath = "/";
        String platform = SWT.getPlatform();
        if (platform.equals("win32")) {
                filterNames = new String [] {"IndoorGML File", "All Files (*.*)"};
                filterExtensions = new String [] {".xml;*.gml;*.igml;", "*.*"};
                filterPath = "c:\\";
        }
        dialog.setFilterNames (filterNames);
        dialog.setFilterExtensions (filterExtensions);
        dialog.setFilterPath (filterPath);
    }
}
