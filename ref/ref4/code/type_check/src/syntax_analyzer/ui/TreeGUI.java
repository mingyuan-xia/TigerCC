/**
 * Main Interface class.
 * 
 * @author Jessie
 * @since 5/10/2008
 */

package syntax_analyzer.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import syntax_analyzer.compiler.MiniJavaParser;
import syntax_analyzer.compiler.model.ASTRoot;
import syntax_analyzer.compiler.model.Node;
import syntax_analyzer.compiler.model.SimpleNode;
import syntax_analyzer.compiler.*;
public class TreeGUI extends AbstractExample {

boolean animate;

public static void main(String[] args) {
	new TreeGUI().run();
}

TreeRoot root;
ASTRoot astRoot;
PageNode selected;
Text errorText;
private int startLine;
private int endLine;
public static boolean parseSucc;
IFigure createPageNode(SimpleNode simplenode) {
	final PageNode node = new PageNode(simplenode);
	node.addMouseListener(new MouseListener.Stub() {
		public void mousePressed(MouseEvent me) {
			setSelected(node);
		}
		public void mouseDoubleClicked(MouseEvent me) {
			doExpandCollapse();
		}
	});
	return node;
}

void doAlignCenter() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.setAlignment(PositionConstants.CENTER);
}

void doAlignLeft() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.setAlignment(PositionConstants.LEFT);
}

void doExpandCollapse() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	if (parent.getContentsPane().getChildren().isEmpty())
		return;
	if (animate) {
		if (parent.isExpanded())
			parent.collapse();
		else
			parent.expand();
	} else
		parent.setExpanded(!parent.isExpanded());
}

void doStyleHanging() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.setStyle(TreeBranch.STYLE_HANGING);
}

void doStyleNormal() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.setStyle(TreeBranch.STYLE_NORMAL);
}

protected void setContents(String fileName) throws FileNotFoundException {
//	System.out.println(fileName);
	MiniJavaParser parser = new MiniJavaParser(new FileInputStream(fileName));
	parser.setText(errorText);
	errorText.setText("");
	boolean reinit = false;
//	while(reinit)
//	{
			parseSucc=true;
			astRoot = parser.Goal();
			if(parseSucc){
				MiniJavaTypeCheck mjtc = new MiniJavaTypeCheck(fileName, astRoot,errorText);
				mjtc.doTypeChecking();
			}
			
	//		astRoot.dump(" ");
	
//	}	
}

protected IFigure getInitialContents(){
	getFigureCanvas().setBackground(ColorConstants.white);
	root = null;
	return root;
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	getFigureCanvas().setBackground(ColorConstants.white);
	root = new TreeRoot(createPageNode(astRoot));
	setNodeContent(root,astRoot);
	return root;
}

private void setNodeContent(TreeBranch branch,Node node){
	if(node != null){
		Node[] children = ((SimpleNode)node).getChildren();
		if(children != null){
			for(int i = 0;i < children.length;i++){
				SimpleNode child = (SimpleNode)children[i];
				if(child != null)
				{	
					TreeBranch newbranch = new TreeBranch(createPageNode(child));
					branch.getContentsPane().add(newbranch);
					setNodeContent(newbranch,child);
				}	
			}
		}
	}	
}
/**
 * @see org.eclipse.draw2d.examples.AbstractExample#run()
 */
protected void hookShell() {
	super.hookShell();
	
	Composite localShell = new Composite(shell, 0);
	localShell.setLayoutData(new GridData(GridData.FILL_VERTICAL));

	GridLayout layout = new GridLayout();
	layout.verticalSpacing = 30;
	localShell.setLayout(layout);
	Group rootGroup = new Group(localShell, 0);
	rootGroup.setText("Tree Palatte");
	FontData data = rootGroup.getFont().getFontData()[0];
	data.setStyle(SWT.BOLD);
	rootGroup.setLayout(new GridLayout());

	final Button orientation = new Button(rootGroup, SWT.CHECK);
	orientation.setText("Horizontal Orientation");
	orientation.setSelection(true);
	orientation.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			root.setHorizontal(orientation.getSelection());
		}
	});

	final Button useAnimation = new Button(rootGroup, SWT.CHECK);
	useAnimation.setText("Use Animation");
	useAnimation.setSelection(false);
	useAnimation.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			animate = useAnimation.getSelection();
		}
	});

	final Button compress = new Button(rootGroup, SWT.CHECK);
	compress.setText("Compress Tree");
	compress.setSelection(false);
	compress.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			root.setCompression(compress.getSelection());
			root.invalidateTree();
			root.revalidate();
		}
	});



	final Label majorLabel = new Label(rootGroup, 0);
	majorLabel.setText("Major Spacing: 10");
	final Scale major = new Scale(rootGroup, 0);
	major.setMinimum(5);
	major.setIncrement(5);
	major.setMaximum(50);
	major.setSelection(10);
	major.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			root.setMajorSpacing(major.getSelection());
			majorLabel.setText("Major Spacing: " + root.getMajorSpacing());
		}
	});
	
	final Label minorLabel = new Label(rootGroup, 0);
	minorLabel.setText("Minor Spacing: 10");
	final Scale minor = new Scale(rootGroup, 0);
	minor.setMinimum(5);
	minor.setIncrement(5);
	minor.setMaximum(50);
	minor.setSelection(10);
	minor.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			root.setMinorSpacing(minor.getSelection());
			minorLabel.setText("Minor Spacing: " + root.getMinorSpacing());
		}
	});
	
	TabFolder tabFolder = new TabFolder(localShell,SWT.NONE);
	TabItem errorEditor = new TabItem(tabFolder,SWT.NONE);
	errorEditor.setText("Errors And Warnings");
	errorText = new Text(tabFolder,SWT.MULTI | SWT.V_SCROLL | 
            SWT.H_SCROLL | SWT.WRAP | SWT.BORDER);
	errorText.setForeground(ColorConstants.red);
	errorEditor.setControl(errorText);
	tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
}

void setSelected(PageNode node) {
	if (selected != null) {
		selected.setSelected(false);
	}
	selected = node;
	selected.setSelected(true);
	
	source.setLineBackground(startLine, endLine - startLine + 1, ColorConstants.white);
	startLine = selected.getSimpleNode().getBeginLine() - 1;
	endLine = selected.getSimpleNode().getEndLine() - 1;
	source.setLineBackground(startLine, endLine - startLine + 1, new Color(null,222, 222, 222));
	System.out.println("(" + node.getSimpleNode().getBeginLine() + "," + node.getSimpleNode().getBeginColumn()
			+ ") (" + node.getSimpleNode().getEndLine() + "," + node.getSimpleNode().getEndColumn() + ")");

}
	
}
