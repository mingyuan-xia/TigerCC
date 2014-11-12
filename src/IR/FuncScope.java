package IR;

import java.util.ArrayList;

import util.FuncDesc;
import util.TypeDesc;

public class FuncScope extends Scope {
	public String name;
	public TypeDesc funcRetType;
	public ArrayList<Variable> args;
	
	public FuncScope(Scope parent, String name, FuncDesc desc) {
		super(parent);
		// parse the descriptor
		int cnt = desc.paraNames.length;
		this.args = new ArrayList<Variable>();
		for (int i = 0; i < cnt; ++i)
			args.add(ScopeItem.makeVariable(this, desc.paraNames[i], desc.paraTypes[i]));
		this.name = name;
		this.funcRetType = desc.retType;
	}
	@Override
	public String toString()
	{
		return name;
	}
	@Override
	public Variable findVar(String name)
	{
		Variable retVal = this.findArg(name); 
		return (retVal != null ? retVal : super.findVar(name));
	}
	public Variable findArg(String name)
	{
		for (int i = this.args.size() - 1; i >= 0; --i)
			if (name.equals(this.args.get(i).toString()))
					return this.args.get(i);
		return null;
	}
	@Override
	public String outputAll()
	{
		String s = "function "+this.name+" of "+this.funcRetType.toString()+"\n";
		s += super.outputAll();
		return s;
	}
	@Override
	protected int getItemCount()
	{
		return this.locals.size()+this.temps.size()+this.args.size();
	}
	@Override
	protected int refItem(ScopeItem u)
	{
		for (int i = 0; i < this.locals.size(); ++i)
			if (u == this.locals.get(i))
				return i;
		for (int i = 0; i < this.temps.size(); ++i)
			if (u == this.temps.get(i))
				return i + this.locals.size();
		for (int i = 0; i < this.args.size(); ++i)
			if (u == this.args.get(i))
				return i + this.locals.size() + this.temps.size();
		return -1;
	}
}
