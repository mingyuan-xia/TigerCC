/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package syntax_analyzer.compiler.model;

import org.eclipse.draw2d.geometry.Rectangle;

import syntax_analyzer.compiler.NodeBorder;

/**
 * @author zhanghao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseNode {
	protected String name;
	private NodeBorder border;
	
	public void setBorder(NodeBorder border){
		this.border = border;
	}
	
	public int getBeginLine(){
		return border.getBeginLine();
	}
	
	public int getBeginColumn(){
		return border.getBeginColumn();
	}
	
	public int getEndLine(){
		return border.getEndLine();
	}
	
	public int getEndColumn(){
		return border.getEndColumn();
	}
}