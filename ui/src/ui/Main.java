package ui;

import java.awt.FlowLayout;
import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;

import edu.pnu.io.SimpleIndoorGMLImporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;

public class Main {
    private Shell shell;
    private GridLayout layout = new GridLayout(2, true);
    private Text txtfilePath;
    private Button btnBrowse;
    private Button btnSubmit;
    private Button btnCancel;
    private Label lblNewLabel;
    private File igmlFile;
    private Tree tree;
    private SpaceLayer layer;
    private Table entranceTable;
    
    /* Statistics Groups */
    private Group statisticsGroup;
    private Group subStatisticsGroup;
    private Group grpSection;
    private Group grpType;
    private Group grpFloor;
    private Group grpEntrances;
    private Button btnNewButton;
    
    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            Main window = new Main();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        shell = new Shell();
        shell.setSize(450, 472);
        shell.setText("SWT Application");
        shell.setLayout(layout);
        
        txtfilePath = new Text(shell, SWT.BORDER);
        GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_text.widthHint = 192;
        txtfilePath.setLayoutData(gd_text);
        
        btnBrowse = new Button(shell, SWT.NONE);
        btnBrowse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btnBrowse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog (shell, SWT.OPEN);
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
                
                String path = dialog.open();
                if(path != null)
                    txtfilePath.setText(path);
            }
        });
        btnBrowse.setText("Browse IndoorGML");

        btnSubmit = new Button(shell, SWT.NONE);
        GridData gd_btnNewButton_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_btnNewButton_1.widthHint = 205;
        btnSubmit.setLayoutData(gd_btnNewButton_1);
        btnSubmit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                igmlFile = new File(txtfilePath.getText());
                if(igmlFile.canRead()) {
                    SimpleIndoorGMLImporter importer;
                    try {
                        importer = new SimpleIndoorGMLImporter(igmlFile);
                        layer = importer.getSpaceLayer();
                        
                        statisticsGroup.setEnabled(true);
                        String[] titles = { "ID", "Duality", "Type", "Floor" };
                        for (int i = 0; i < titles.length; i++) {
                            TableColumn column = new TableColumn(entranceTable, SWT.NONE);
                            column.setText(titles[i]);
                            entranceTable.getColumn(i).pack();
                        }
                        for (int i=0; i<titles.length; i++) {
                            entranceTable.getColumn (i).pack ();
                        }

                        Set<State> entrances = layer.getEntrances();
                        Iterator it = entrances.iterator();
                        while(it.hasNext()) {
                            TableItem item = new TableItem(entranceTable, SWT.NONE);
                            State s = (State) it.next();
                            item.setText(0, s.getId());
                            item.setText(1, s.getDuality().getId());
                            item.setText(2, (String) s.getUserData().get("USAGE"));
                            item.setText(3, (String) s.getUserData().get("FLOOR"));
                        }
                        
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                   //TODO : error dialog 
                }
            }
        });
        btnSubmit.setText("Submit");
        
        btnCancel = new Button(shell, SWT.NONE);
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtfilePath.setText("");
                txtfilePath.setEnabled(true);
                btnBrowse.setEnabled(true);
            }
        });
        GridData gd_btnNewButton_2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_btnNewButton_2.widthHint = 36;
        btnCancel.setLayoutData(gd_btnNewButton_2);
        btnCancel.setText("Cancel");
        
        statisticsGroup = new Group(shell, SWT.NONE);
        statisticsGroup.setLayout(new FillLayout(SWT.VERTICAL));
        statisticsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        statisticsGroup.setText("IndoorGML Statistics");
        
        grpEntrances = new Group(statisticsGroup, SWT.NONE);
        grpEntrances.setLayout(new FillLayout(SWT.HORIZONTAL));
        grpEntrances.setText("Entrances");
        
        /* Table */
        entranceTable = new Table(grpEntrances, SWT.BORDER | SWT.FULL_SELECTION);
        entranceTable.setHeaderVisible(true);
        entranceTable.setLinesVisible(true);
        
        subStatisticsGroup = new Group(statisticsGroup, SWT.NONE);
        subStatisticsGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
        subStatisticsGroup.setText("Cell Statistics");
        
        grpSection = new Group(subStatisticsGroup, SWT.NONE);
        grpSection.setText("Section");
        grpSection.setLayout(new FillLayout(SWT.VERTICAL));
        
        Button btnSectionAvenuel = new Button(grpSection, SWT.CHECK);
        btnSectionAvenuel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        btnSectionAvenuel.setText("All");
        
        grpType = new Group(subStatisticsGroup, SWT.NONE);
        grpType.setText("Usage");
        grpType.setLayout(new FillLayout(SWT.VERTICAL));
        
        Button btnTypeAll = new Button(grpType, SWT.CHECK);
        btnTypeAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        btnTypeAll.setText("All");
        
        grpFloor = new Group(subStatisticsGroup, SWT.NONE);
        grpFloor.setText("Floor");
        grpFloor.setLayout(new FillLayout(SWT.VERTICAL));
        
        Button btnFloorAll = new Button(grpFloor, SWT.CHECK);
        btnFloorAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        btnFloorAll.setText("All");
        
        statisticsGroup.setEnabled(false);
        
        btnNewButton = new Button(shell, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        btnNewButton.setText("Generate");

        
        /* Table 
        table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 200;
        table.setLayoutData(data);*/

        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
