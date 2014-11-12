/**
 * The abstract example of a simple editor.
 * 
 * @author Jessie
 * @since 5/10/2008
 */

package syntax_analyzer.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.draw2d.ColorConstants;


public abstract class AbstractExample {

protected static final Font COURIER = new Font(null, "Courier", 9, 0);//$NON-NLS-1$
protected static final Font BOLD = new Font(null, "Helvetica", 10, SWT.BOLD);//$NON-NLS-1$
protected static final Font ITALICS = new Font(null, "Helvetica", 10, SWT.ITALIC);//$NON-NLS-1$
protected static final Font HEADING_1 = new Font(null, "Helvetica", 15, SWT.BOLD);//$NON-NLS-1$
protected FigureCanvas fc;
protected IFigure contents;
protected String fileName;
protected Shell shell;
protected StyledText source;
protected TabFolder tabFolder;
protected Label highlighter;

protected void run(){
	Display d = Display.getDefault();
	shell = new Shell(d, getShellStyle());	
	shell.setText("Visual Syntax Analyzer");
	GridLayout layout = new GridLayout(3, false);
	layout.horizontalSpacing = 10;
	shell.setLayout(layout);
	
	tabFolder=new TabFolder(shell,SWT.NONE);
	
	TabItem sourceEditor = new TabItem(tabFolder,SWT.NONE);
	sourceEditor.setText("Source Code");
	
	source = new StyledText(tabFolder,SWT.MULTI | SWT.V_SCROLL | 
            SWT.H_SCROLL | SWT.WRAP | SWT.BORDER);
	sourceEditor.setControl(source);
	fileName = null;
	
	TabItem treeEditor = new TabItem(tabFolder,SWT.NONE);
	treeEditor.setText("Tree View");
	setFigureCanvas(new FigureCanvas(tabFolder));
	treeEditor.setControl(getFigureCanvas());
	
	//Menu Bar
	final Menu menu = new Menu(shell, SWT.BAR);
	shell.setMenuBar(menu);

	//File MenuItem
	final MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
	fileMenuItem.setText("&File");

	//File DROP_DOWN Menu
	Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	fileMenuItem.setMenu(fileMenu);
	{
		//New menuItem
		MenuItem newMenuItem = new MenuItem(fileMenu,SWT.PUSH);
		newMenuItem.setText("New File");
		newMenuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){ 
				System.out.println("new");
				source.setText("");
				fileName = null;
				tabFolder.setSelection(0);
				getFigureCanvas().setContents(contents = getInitialContents());
			}
		});
		
		//Open file menuItem
		MenuItem openMenuItem = new MenuItem(fileMenu,SWT.PUSH);
		openMenuItem.setText("Open File...");
		openMenuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){ 
				FileDialog fileDialog = new FileDialog(shell,SWT.OPEN);
				fileDialog.setText("Open");
				fileName = fileDialog.open();
				if (fileName==null)
	                return;
	            // code here to open the file and display
	            FileReader file = null;
	            try {
	                file = new FileReader(fileName);
	            } 
	            catch (FileNotFoundException ex) {
	                MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR 
	                        | SWT.OK );
	                messageBox.setMessage("Could not open file.");
	                messageBox.setText("Error");
	                messageBox.open( );
	                return;
	            }
	            BufferedReader fileInput = new BufferedReader(file);
	            String text = null;
	            StringBuffer sb = new StringBuffer( );
	            try {
	                do{
	                    if(text!=null)
	                        sb.append(text + "\n");
	                }
	                while((text = fileInput.readLine( ))!=null);                        
	            } 
	            catch (IOException e1) {
	            MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR 
	                        | SWT.OK );
	                messageBox.setMessage("Could not write to file.");
	                messageBox.setText("Error");
	                messageBox.open( );
	                return;
	            }
	            source.setText(sb.toString( ));
	//            System.out.println(sb.toString());
	            tabFolder.setSelection(0);
	            getFigureCanvas().setContents(contents = getInitialContents());
			   }
		});
		
		//Save MenuItem
		MenuItem saveMenuItem = new MenuItem(fileMenu,SWT.PUSH);
		saveMenuItem.setText("Save");
		saveMenuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){ 
	            if(fileName==null)
	            {                  
	            	FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
	            	fileName = fileDialog.open( );
	            }
	            if(fileName == null)
	            	return;
	            
	            File file = new File(fileName); 
	            try {
	                FileWriter fileWriter = new FileWriter( file );
	                fileWriter.write( source.getText( ) ); 
	                fileWriter.close( ); 
	            } catch (IOException ex) {
	                MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR 
	                        | SWT.OK ); 
	               messageBox.setMessage("File I/O Error.");
	                messageBox.setText("Error");
	                messageBox.open( );
	                return;
	            }
	            tabFolder.setSelection(0);
			}
		});	
		
		//SaveAs MenuItem
		MenuItem saveAsMenuItem = new MenuItem(fileMenu,SWT.PUSH);
		saveAsMenuItem.setText("Save As...");
		saveAsMenuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){ 
				FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
	            fileDialog.setText("Save As...");
	            fileName = fileDialog.open( );
	            if(fileName==null)
	                return;                  
	            
	            File file = new File(fileName); 
	            try {
	                FileWriter fileWriter = new FileWriter( file );
	                fileWriter.write( source.getText( ) ); 
	                fileWriter.close( ); 
	            } catch (IOException ex) {
	                MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR 
	                        | SWT.OK ); 
	               messageBox.setMessage("File I/O Error.");
	                messageBox.setText("Error");
	                messageBox.open( );
	                return;
	            }
	            tabFolder.setSelection(0);
			}
		});	
	}
	
	//Compile Menu
	final MenuItem compileMenuItem = new MenuItem(menu, SWT.CASCADE);
	compileMenuItem.setText("&Compile");
	
	//Compile DROP_DOWN menu
	Menu compileMenu = new Menu(shell, SWT.DROP_DOWN);
	compileMenuItem.setMenu(compileMenu);
	{
		//Syntax menuItem
		MenuItem analyzerMenuItem = new MenuItem(compileMenu,SWT.PUSH);
		analyzerMenuItem.setText("Run Syntax Analyzer");
		analyzerMenuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){ 
				if (fileName != null) 
			    {
					try
					{
						setContents(fileName);
						System.out.println(fileName);
						getFigureCanvas().setContents(contents = getContents());
						tabFolder.setSelection(1);
					}catch(Exception ex)
					{
					//	ex.printStackTrace();
					}
			    }
			}
		});
	}	
	
	getFigureCanvas().setContents(contents = getInitialContents());
	getFigureCanvas().getViewport().setContentsTracksHeight(true);
	getFigureCanvas().getViewport().setContentsTracksWidth(true);
	tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
	
	hookShell();
	sizeShell();
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

protected void setFileName(String fileName){
	this.fileName = fileName;
}

protected String getFileName(){
	return fileName;
}

protected int getShellStyle() {
	return SWT.SHELL_TRIM;
}

protected void sizeShell() {
	shell.setBounds(100,50,1000,700);
}

protected abstract void setContents(String fileName) throws FileNotFoundException;
protected abstract IFigure getContents();
protected abstract IFigure getInitialContents();

protected FigureCanvas getFigureCanvas(){
	return fc;
}

protected void hookShell(){
}

protected void setFigureCanvas(FigureCanvas canvas){
	this.fc = canvas;
}
}
