package IR;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import util.*;

public class Scope extends ScopeItem{
	// name mangling:
	// basic block (BasicBlock) : !n
	// temporary(TypeDesc) : $n
	// sub scope(Scope) : *n

	protected static int ScopeCount = 0;

	protected int tmpVarCount = 0;
	protected int myID;
	public ArrayList<FuncScope> funcs;
	public ArrayList<BasicBlock> blocks;
	public ArrayList<Temporary> temps;
	public ArrayList<Variable> locals;
	public ArrayList<Scope> scopes;
	protected Map<String, TypeDesc> typeList;

	public BasicBlock entry, exit;
	
	Scope(Scope parent)
	{
		super(parent);
		this.myID = Scope.ScopeCount++;
		this.typeList = new HashMap<String, TypeDesc>();
		this.funcs = new ArrayList<FuncScope>();
		this.blocks = new ArrayList<BasicBlock>();
		this.temps = new ArrayList<Temporary>();
		this.locals = new ArrayList<Variable>();
		this.scopes = new ArrayList<Scope>();
	}
	
	@Override
	public String toString() {
		return "scope" + this.myID;
	}

	public BasicBlock setEntry(BasicBlock b) {
		return this.entry = b;
	}

	public BasicBlock setExit(BasicBlock b) {
		return this.exit = b;
	}

	public Scope addSubScope() {
		Scope u = ScopeItem.makeSubScope(this);
		this.scopes.add(u);
		return u;
	}

	public BasicBlock addBlock() {
		BasicBlock u = ScopeItem.makeBasicBlock(this);
		this.blocks.add(u);
		return u;
	}

	public FuncScope addFunc(String name, FuncDesc desc) {
		FuncScope u = ScopeItem.makeFunction(this, name, desc);
		this.funcs.add(u);
		return u;
	}

	public Variable addLocal(String name, TypeDesc ty) {
		Variable u = ScopeItem.makeVariable(this, name, ty);
		this.locals.add(u);
		return u;
	}

	public Temporary addTemp(TypeDesc ty) {
		Temporary u = ScopeItem.makeTemp(this, ty);
		this.temps.add(u);
		return u;
	}

	public TypeDesc addType(String name, TypeDesc desc) {
		this.typeList.put(name, desc);
		return desc;
	}
	
	public Variable findLocal(String name)
	{
		for (int i = this.locals.size() - 1; i >= 0; --i)
			if (this.locals.get(i).name.equals(name))
				return this.locals.get(i);
		return null;
	}
	
	public Variable findVar(String name)
	{
		Variable retVal = this.findLocal(name); 
		if (retVal != null)
			return retVal;
		Scope s = this.ctx;
		while (s != null)
		{
			if ((retVal = s.findVar(name)) != null)
			{
				// TODO retVal.escape = true;
				return retVal;
			}
			s = s.ctx;
		}
		return null;
	}
	
	public FuncScope findFunc(String name)
	{
		for (int i = this.funcs.size() - 1; i >= 0; --i)
			if (this.funcs.get(i).name.equals(name))
				return this.funcs.get(i);
		return (this.ctx == null ? null : this.ctx.findFunc(name));
	}
	
	public TypeDesc findType(String name)
	{
		Scope s = this;
		while (s != null)
			if (s.typeList.get(name) != null)
				return s.typeList.get(name);
			else
				s = s.ctx;
		return null;
	}

	public String outputAll() {
		if (this.entry == null)
			return "";
		String s = new String();
		s += this.toString()+"{\n";
		for (int i = 0; i < this.funcs.size(); ++i)
			s += this.funcs.get(i).outputAll();
		s += this.toString() + ".entry" +entry.toString()+":\n"+ entry.outputAll();
		BasicBlock cur;
		for (int i = 0; i < this.blocks.size(); ++i)
		{
			cur = this.blocks.get(i);
			if (entry != cur && exit != cur)
				s += cur.toString()+":\n"+cur.outputAll();
		}
		for (int i = 0; i < this.scopes.size(); ++i)
			s += this.scopes.get(i).outputAll();
		if (this.exit != this.entry)
			s += this.toString() + ".exit" +exit.toString()+":\n"+ exit.outputAll();
		s += "}\n";
		return s;
	}
	public void doLivenessAnalysis()
	{
		// TODO need new data structure and rewriting!
		for (int i = 0; i < this.funcs.size(); ++i)
		{
			if (this.funcs.get(i).entry != null)
				this.funcs.get(i).doLivenessAnalysis();
		}
		for (int i = 0; i < this.scopes.size(); ++i)
			this.scopes.get(i).doLivenessAnalysis();
		BasicBlock b;
		AbsInstr ins;
		int itemCount = this.getItemCount();
		for (int i = 0; i < this.blocks.size(); ++i)
		{
			b = this.blocks.get(i);
			b.inBlocks = new BitSet(this.blocks.size());
			b.outBlocks = new BitSet(this.blocks.size());
			b.defs = new BitSet(itemCount);
			b.uses = new BitSet(itemCount);
			b.inLiveness = new BitSet(itemCount);
			b.outLiveness = new BitSet(itemCount);
		}
		for (int i = 0; i < this.blocks.size(); ++i)
		{
			b = this.blocks.get(i);
			ins = b.irIns.get(b.irIns.size() - 1);
			if (ins.op != AbsInstr.INS_MISC_FRAME_LEVAVE)
			{
				if (ins.op == AbsInstr.INS_JUMP)
				{ // jump
					int idx = refBlock((BasicBlock)ins.opr0);
					b.outBlocks.set(idx);
					this.blocks.get(idx).inBlocks.set(i);
				}
				else
				{ // conditional jump
					int idx = refBlock((BasicBlock)ins.opr1);
					b.outBlocks.set(idx);
					this.blocks.get(idx).inBlocks.set(i);
					idx = refBlock((BasicBlock)ins.opr2);
					b.outBlocks.set(idx);
					this.blocks.get(idx).inBlocks.set(i);
				}
			}
			for (int j = b.irIns.size() - 1; j >= 0; --j)
			{
				// determine uses and defs
				ins = b.irIns.get(j);
				switch (ins.op)
				{
				case AbsInstr.INS_MISC_FRAME_ENTER:
				case AbsInstr.INS_MISC_FRAME_LEVAVE:
				case AbsInstr.INS_JUMP:
					break;
					
				case AbsInstr.INS_OP_PUSH:
				case AbsInstr.INS_JUMP_EQ:
				case AbsInstr.INS_JUMP_NEQ:
				case AbsInstr.INS_JUMP_LESS:
				case AbsInstr.INS_JUMP_LESSEQ:
				case AbsInstr.INS_JUMP_GREATER:
				case AbsInstr.INS_JUMP_GREATER_EQ:
					if (!(ins.opr0 instanceof Constant))
						b.uses.set(this.refItem(ins.opr0));
					break;

				case AbsInstr.INS_MISC_NEWRECORD:
				case AbsInstr.INS_OP_ASSIGN:
				case AbsInstr.INS_OP_NEG:
					b.defs.set(this.refItem(ins.opr0));
					b.uses.clear(this.refItem(ins.opr0));
					if (!(ins.opr1 instanceof Constant))
						b.uses.set(this.refItem(ins.opr1));
					break;

				case AbsInstr.INS_MISC_NEWARRAY:
				case AbsInstr.INS_OP_ADD:
				case AbsInstr.INS_OP_SUB:
				case AbsInstr.INS_OP_MUL:
				case AbsInstr.INS_OP_DIV:
				case AbsInstr.INS_OP_AND:
				case AbsInstr.INS_OP_OR:

				case AbsInstr.INS_OP_GREATER:
				case AbsInstr.INS_OP_GREATEREQ:
				case AbsInstr.INS_OP_NEQ:
				case AbsInstr.INS_OP_LESS:
				case AbsInstr.INS_OP_LESSEQ:
				case AbsInstr.INS_OP_EQ:
				case AbsInstr.INS_OP_INDEX:
				case AbsInstr.INS_OP_GETFIELD:
					b.defs.set(this.refItem(ins.opr0));
					b.uses.clear(this.refItem(ins.opr0));
					if (!(ins.opr1 instanceof Constant))
						b.uses.set(this.refItem(ins.opr1));
					if (!(ins.opr2 instanceof Constant))
						b.uses.set(this.refItem(ins.opr2));
					break;

				case AbsInstr.INS_OP_CALL:
					b.defs.set(this.refItem(ins.opr0));
					b.uses.clear(this.refItem(ins.opr0));
					break;
					
				case AbsInstr.INS_OP_SUBSCRIPT:
				case AbsInstr.INS_OP_SETFIELD:
					if (!(ins.opr0 instanceof Constant))
						b.uses.set(this.refItem(ins.opr0));
					if (!(ins.opr1 instanceof Constant))
						b.uses.set(this.refItem(ins.opr1));
					if (!(ins.opr2 instanceof Constant))
						b.uses.set(this.refItem(ins.opr2));
					break;
				}
			}
		}
		boolean analysisDone = false;
		BitSet tempin, tempout;

		while (!analysisDone)
		{
			analysisDone = true;
			for (int i = 0; i < this.blocks.size(); ++i)
			{
				b = this.blocks.get(i);
				tempin = (BitSet) b.inLiveness.clone();
				tempout = (BitSet) b.outLiveness.clone();
				b.inLiveness.or(b.uses);
				b.inLiveness.or(b.outLiveness);
				b.inLiveness.andNot(b.defs);
				int idx = b.outBlocks.nextSetBit(0);
				while (idx != -1)
				{
					b.outLiveness.or(this.blocks.get(idx).inLiveness);
					idx = b.outBlocks.nextSetBit(idx+1);
				}
				if (!tempin.equals(b.inLiveness) || !tempout.equals(b.outLiveness))
					analysisDone = false;
			}
		}
	}

	private int refBlock(BasicBlock b) {
		for (int i = 0; i < this.blocks.size(); ++i)
			if (b == this.blocks.get(i))
				return i;
		return -1;
	}
	protected int getItemCount()
	{
		return this.locals.size()+this.temps.size();
	}
	protected int refItem(ScopeItem u)
	{
		for (int i = 0; i < this.locals.size(); ++i)
			if (u == this.locals.get(i))
				return i;
		for (int i = 0; i < this.temps.size(); ++i)
			if (u == this.temps.get(i))
				return i + this.locals.size();
		return -1;
	}
}
