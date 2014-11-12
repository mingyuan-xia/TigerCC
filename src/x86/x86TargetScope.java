package x86;

import java.util.ArrayList;

import BackEnd.Access;
import BackEnd.TargetBlock;
import BackEnd.TargetFrame;
import IR.FuncScope;
import IR.Scope;
import IR.ScopeItem;
import IR.Variable;

/* x86 cdecl frame structure
 * LOW
 * < esp
 * ...(child temp)
 * local var 3
 * local var 2
 * local var 1
 * < ebp
 * parent frame pointer
 * return address
 * parameter 1
 * parameter 2
 * parameter 3
 * ...(parent temp)
 * HIGH
 * */

public class x86TargetScope extends TargetFrame {
	public x86Target target;
	public ArrayList<ScopeItem> locals;
	public ArrayList<x86Access> localRefs;
	public ArrayList<Variable> args;
	public ArrayList<x86Access> argRefs;
	public x86Instr entryStackIns, exitStackIns;
	protected x86TargetScope(Scope scope, TargetFrame parent, x86Target target) {
		super(scope, parent);
		this.target = target;
		this.locals = new ArrayList<ScopeItem>();
		this.localRefs = new ArrayList<x86Access>();
		this.args = new ArrayList<Variable>();
		this.argRefs = new ArrayList<x86Access>();
	}

	@Override
	public Access addArg(Variable u) {
		this.args.add(u);
		x86Access retVal = new x86Access(this.target.regEBP, this.args.size() * 4+4);
		this.argRefs.add(retVal);
		return retVal;
	}

	@Override
	public Access addLocal(ScopeItem u) {
		this.locals.add(u);
		x86Access retVal = new x86Access(this.target.regEBP, -this.locals.size() * 4);
		this.localRefs.add(retVal);
		return retVal;
	}

	@Override
	public void generateEntry(TargetBlock block) {
		// push ebp
		// mov ebp, esp
		// push old_fp
		// sub esp, LOCAL_SIZE
		// mov __fname_fp, ebp
		x86Instr.newInsPush(block, new x86Access(this.target.regEBP));
		x86Instr.newInsMov(block, new x86Access(this.target.regEBP), new x86Access(this.target.regESP));
		x86Instr.newInsPush(block, new x86Access("__"+this.toString()+"_fp"));
		this.entryStackIns = x86Instr.newInsSub(block, new x86Access(this.target.regESP), new x86Access(0));
		x86Instr.newInsMov(block, new x86Access("__"+this.toString()+"_fp"), new x86Access(this.target.regEBP));
		this.entry = block;
	}
	
	@Override
	public void generateExit(TargetBlock block) {
		// add esp, LOCAL_SIZE
		// pop old_fp
		// pop ebp
		// ret
		this.exitStackIns = x86Instr.newInsAdd(block, new x86Access(this.target.regESP), new x86Access(0));
		x86Instr.newInsPop(block, new x86Access("__"+this.toString()+"_fp"));
		x86Instr.newInsPop(block, new x86Access(this.target.regEBP));
		x86Instr.newInsRet(block);
		this.exit = block;
	}

	@Override
	public Access refArg(Variable u) {
		for (int i = 0; i < this.args.size(); ++i)
			if (u == this.args.get(i))
				return this.argRefs.get(i);
		return null;
	}

	@Override
	public Access refItem(ScopeItem u) {
		Access retVal = refLocal(u);
		if (retVal != null)
			return retVal;
		if (u instanceof Variable)
			retVal = refArg((Variable)u);
		return retVal;
	}

	@Override
	public Access refLocal(ScopeItem u) {
		for (int i = 0; i < this.locals.size(); ++i)
			if (u == this.locals.get(i))
				return this.localRefs.get(i);
		return null;
	}

	@Override
	public TargetFrame newSubScope(Scope scope) {
		x86TargetScope retVal = new x86TargetScope(scope, this, this.target);
		this.targetSubscopes.add(retVal);
		return retVal;
	}
	@Override
	public TargetFrame newFunction(FuncScope scope) {
		x86TargetScope retVal = new x86TargetScope(scope, this, this.target);
		this.targetFuncs.add(retVal);
		for (int i = 0; i < scope.args.size(); ++i)
			retVal.addArg(scope.args.get(i));
		return retVal;		
	}
	@Override
	public String toString()
	{
		return this.absScope.toString();
	}
	
	@Override
	public void update()
	{
		this.entryStackIns.opr1 = new x86Access(this.locals.size() * 4);
		this.exitStackIns.opr1 = new x86Access(this.locals.size() * 4);
	}
}
