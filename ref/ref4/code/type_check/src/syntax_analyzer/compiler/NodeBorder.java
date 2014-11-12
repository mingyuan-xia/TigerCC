package syntax_analyzer.compiler;

public class NodeBorder {
	private int beginLine;
	private int beginColumn;
	private int endLine;
	private int endColumn;
	
	public NodeBorder(){
		beginLine = 0;
		beginColumn = 0;
		endLine = 0;
		endColumn = 0;
	}
	public NodeBorder(int beginLine,int beginColumn,int endLine,int endColumn)
	{
		this.beginLine = beginLine;
		this.beginColumn = beginColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
	}
	
	public int getBeginLine(){
		return this.beginLine;
	}
	
	public int getBeginColumn(){
		return this.beginColumn;
	}
	
	public int getEndLine(){
		return this.endLine;
	}
	
	public int getEndColumn(){
		return this.endColumn;
	}
	
	public void expandBorder(NodeBorder border2){
		this.endColumn = border2.endColumn;
		this.endLine = border2.endLine;
	}
	
	public NodeBorder copy(){
		return new NodeBorder(this.beginLine,this.beginColumn,this.endLine,
				this.endColumn);
	}
}
