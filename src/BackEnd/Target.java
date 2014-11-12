package BackEnd;

import IR.*;

public abstract class Target {
	public TargetFrame curFrame;
	public Register[] regSet;

	protected abstract TargetFrame makeFrame(Scope scope, TargetFrame parent);

	protected abstract void parsePlacement();
	protected abstract TargetBlock parseBlock(BasicBlock b);
	protected abstract TargetFrame parseFunction(FuncScope u);
	protected abstract TargetFrame parseSubscope(Scope scope);
	
	protected abstract Register spillReg(TargetBlock tb, Register reg);
	protected abstract Register allocReg(TargetBlock tb);
	public TargetFrame generate(Scope global)
	{
		return generate(makeFrame(global, null));
	}
	protected TargetFrame generate(TargetFrame s)
	{
		this.curFrame = s;
		this.parsePlacement();
		FuncScope f;
		for (int i = 0; i < s.absScope.funcs.size(); ++i)
		{
			f = s.absScope.funcs.get(i);
			String fname = f.toString();
			if (fname.equals("print") || fname.equals("ord") || fname.equals("getchar") || fname.equals("chr") || fname.equals("printi"))
				continue;
			else
				this.parseFunction(f);
		}
		for (int i = 0; i < s.absScope.blocks.size(); ++i)
			this.parseBlock(s.absScope.blocks.get(i));
		for (int i = 0; i < s.absScope.scopes.size(); ++i)
			this.parseSubscope(s.absScope.scopes.get(i));
		s.update();
		this.genHook(s);
		return s;		
	}
	protected abstract void genHook(TargetFrame s);
}
