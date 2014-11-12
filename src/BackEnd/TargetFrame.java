package BackEnd;

import java.util.ArrayList;

import IR.*;

public abstract class TargetFrame {
	public TargetFrame parent;
	public Scope absScope;
	protected TargetBlock entry, exit;
	protected ArrayList<TargetBlock> targetBlocks;
	protected ArrayList<TargetFrame> targetFuncs;
	protected ArrayList<TargetFrame> targetSubscopes;

	protected TargetFrame(Scope scope, TargetFrame parent)
	{
		this.parent = parent;
		this.absScope = scope;
		this.targetBlocks = new ArrayList<TargetBlock>();
		this.targetFuncs = new ArrayList<TargetFrame>();
		this.targetSubscopes = new ArrayList<TargetFrame>();
	}
	public abstract Access addLocal(ScopeItem u);
	public abstract Access addArg(Variable u);
	public abstract Access refItem(ScopeItem u);
	public abstract Access refLocal(ScopeItem u);
	public abstract Access refArg(Variable u);	
	public abstract String toString();
	public abstract void update();
	
	public abstract void generateEntry(TargetBlock block);
	public abstract void generateExit(TargetBlock block);
	public String outputAll()
	{
		String s = this.toString()+":\n";
		TargetBlock tb;
		s += this.entry.outputAll()+"\n";
		for (int i = 0; i < this.targetBlocks.size(); ++i)
		{
			tb = this.targetBlocks.get(i);
			if (tb != this.entry && tb != this.exit)
				s += tb.outputAll()+"\n";
		}
		if (this.exit != this.entry)
			s += this.exit.outputAll()+"\n";
		for (int i = 0; i < this.targetFuncs.size(); ++i)
			s += this.targetFuncs.get(i).outputAll()+"\n";
		for (int i = 0; i < this.targetSubscopes.size(); ++i)
			s += this.targetSubscopes.get(i).outputAll()+"\n";
		
		return s;
	}
	public TargetBlock newBlock(BasicBlock b)
	{
		TargetBlock tb = new TargetBlock(b);
		this.targetBlocks.add(tb);
		return tb;
	}
	public abstract TargetFrame newSubScope(Scope scope);
	public abstract TargetFrame newFunction(FuncScope scope);
}
