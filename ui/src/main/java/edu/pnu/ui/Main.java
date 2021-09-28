package edu.pnu.ui;
import java.io.File;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import edu.pnu.core.Clock;
import edu.pnu.core.Generator;
import edu.pnu.io.SimpleMovingFeaturesCSVExporter;
import edu.pnu.model.movingobject.ClientObject;
import edu.pnu.model.movingobject.EmployeeObject;
import edu.pnu.model.movingobject.MovingObject;
import edu.pnu.util.StateDijkstraPathFinder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.FillLayout;
import edu.pnu.io.SimpleIndoorGMLImporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;


public class Main {
    private Shell shell;
    private GridLayout layout = new GridLayout(2, true);
    private Text txtInputFilePath;
    private Text txtTimeDuration;
    private Text txtMaxMOCount;
    private Text txtGenerateProb;
    private Text txtOutputFilePath;
    private Button btnBrowse;
    private Button btnSubmit;
    private Button btnCancel;
    private Table entranceTable;
    private File igmlFile;
    private SpaceLayer layer;

    
    /* Statistics Groups */
    private Group statisticsGroup;
    private Group moOptionGroup;
    private Group grpTime;
    private Group grpMOCount;
    private Group grpGenProb;
    private Group grpEntrances;
    private Group grpOutput;
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
    
   public State getRandomState(State s) {
  List<State> sameSection = layer.getNodesBySection(
          (String) s.getDuality().getUserData().get("SECTION"));
  List<State> roomStates = layer.getNodesByUsage("ROOM");
  List<State> states = new ArrayList<State>(sameSection);
  states.retainAll(roomStates);
  
  int stateSize = states.size();
  
  StateDijkstraPathFinder finder = new StateDijkstraPathFinder(layer);
  State random = null;
  List path = null;
  do {
      int randNumber = new Random().nextInt(stateSize - 1);
      random = states.get(randNumber);
      path = finder.getShortestPath(s, random);
  } while(path.size() == 0);
  return random;
 }
    
    
    public void open() {
        Display display = Display.getDefault();
        shell = new Shell();
        shell.setSize(450, 472);
        shell.setText("IndoorGML based Moving Object Generator");
        shell.setLayout(layout);

        txtInputFilePath = new Text(shell, SWT.BORDER);
        GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_text.widthHint = 192;
        txtInputFilePath.setLayoutData(gd_text);
        
        btnBrowse = new Button(shell, SWT.NONE);
        btnBrowse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btnBrowse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog (shell, SWT.OPEN);
                String [] filterNames = new String [] {"IndoorGML File", "All Files (*)"};
                String [] filterExtensions = new String [] {"*.xml;*.gml;*.igml;", "*"};
                String filterPath = System.getProperty("user.dir");
                dialog.setFilterNames (filterNames);
                dialog.setFilterExtensions (filterExtensions);
                dialog.setFilterPath (filterPath);
                
                String path = dialog.open();
                if(path != null)
                    txtInputFilePath.setText(path);
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
                igmlFile = new File(txtInputFilePath.getText());
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
                            if(s.getUserData().containsKey("USAGE"))
                                item.setText(2, (String) s.getUserData().get("USAGE"));
                            if(s.getUserData().containsKey("FLOOR"))
                                item.setText(3, (String) s.getUserData().get("FLOOR"));
                            else if(s.getUserData().containsKey("STOREY"))
                                item.setText(3, (String) s.getUserData().get("STOREY"));
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
                txtInputFilePath.setText("");
                txtInputFilePath.setEnabled(true);
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

        /* Option */

        moOptionGroup = new Group(shell, SWT.NONE);
        moOptionGroup.setLayout(new FillLayout(SWT.VERTICAL));
        moOptionGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        moOptionGroup.setText("Moving Object Generation Options");

        grpTime = new Group(moOptionGroup, SWT.NONE);
        grpTime.setText("Time duration");
        grpTime.setLayout(new FillLayout(SWT.FILL));
        txtTimeDuration = new Text(grpTime, SWT.BORDER);
        txtTimeDuration.setText("300");

        grpMOCount = new Group(moOptionGroup, SWT.NONE);
        grpMOCount.setText("Max moving object count");
        grpMOCount.setLayout(new FillLayout(SWT.FILL));
        txtMaxMOCount = new Text(grpMOCount, SWT.BORDER);
        txtMaxMOCount.setText("30");
        
        grpGenProb = new Group(moOptionGroup, SWT.NONE);
        grpGenProb.setText("Generation probability");
        grpGenProb.setLayout(new FillLayout(SWT.FILL));
        txtGenerateProb = new Text(grpGenProb, SWT.BORDER);
        txtGenerateProb.setText("0.3");

        grpOutput = new Group(moOptionGroup, SWT.NONE);
        grpOutput.setText("Output file path");
        grpOutput.setLayout(new FillLayout(SWT.FILL));
        txtOutputFilePath = new Text(grpOutput, SWT.BORDER);
        txtOutputFilePath.setText("MF.csv");

        statisticsGroup.setEnabled(false);

        btnNewButton = new Button(shell, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(layer != null) {
                    int TIME_DURATION = 0;
                    int MAX_MO_COUNT = 0;
                    double GENERATE_PROBABILITY = 0;
                    try {
                        TIME_DURATION = Integer.parseInt(txtTimeDuration.getText());
                    } catch (NumberFormatException nfe) {
                        txtTimeDuration.setText("Time duration value should be Integer");
                    }
                    try {
                        MAX_MO_COUNT = Integer.parseInt(txtMaxMOCount.getText());
                    } catch (NumberFormatException nfe) {
                        txtMaxMOCount.setText("Moving object count value should be Integer");
                    }
                    try {
                        GENERATE_PROBABILITY = Double.parseDouble(txtGenerateProb.getText());
                    } catch (NumberFormatException nfe) {
                        txtGenerateProb.setText("MO generation value should be Double");
                    }

                    Generator gen = new Generator(layer);
                    
                    Iterator sit = layer.getEntrances().iterator();
                    int moCount = 0;
                    Clock clock = gen.getClock();
                    while(gen.advance()) {
                        if(clock.getTime() < TIME_DURATION) {
                            if(clock.getTime() % 5 == 0) {
                                sit = layer.getEntrances().iterator();
                                while(sit.hasNext() &&  moCount < MAX_MO_COUNT) {
                                    State ent = (State) sit.next();
                                    // creating employee  
                                    double randomNum = new Random().nextDouble();
                                    if (randomNum <= GENERATE_PROBABILITY) {
                                    	State random = getRandomState(ent);
                                    	MovingObject employee = new EmployeeObject(gen, ent, random);
                                        gen.addMovingObject(employee);
                                        moCount++;
                                        System.out.println("employee created");
                                        System.out.println("MO Count : " + moCount);
                                    }
                                    // creating client
                                    if (randomNum <= (1-GENERATE_PROBABILITY)) { 
                                    	MovingObject client = new ClientObject(gen, ent);
                                    	gen.addMovingObject(client);
                                    	moCount++;
                                    	System.out.println("client created");
                                    	System.out.println("MO Count : " + moCount);
                                    }
                                }
                            }
                        }
                    }

                    SimpleMovingFeaturesCSVExporter csvExt = new SimpleMovingFeaturesCSVExporter("Real");
                    Iterator<MovingObject> it = gen.getMovingObjectIterator();
                    while(it.hasNext()) {
                        MovingObject m = it.next();
                        csvExt.addHistory(m.getId(), m.getHistory());
                    }
                    String outputPath = txtOutputFilePath.getText();
                    csvExt.bufferedExport(outputPath);
                }
            }
        });
        btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        btnNewButton.setText("Generate");

        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
