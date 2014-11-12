package IR;

import util.FuncDesc;
import util.TypeDesc;

public abstract class ScopeItem {

	public Scope ctx;

	// name mangling:
	// basic block (BasicBlock) : !n
	// args(TypeDesc) : @name
	// function(Frame) : &name
	// temp variables(TypeDesc) : %n
	// local variables(TypeDesc) : $name
	// sub scope(Scope) : *n
	@Override
	public abstract String toString();

	protected ScopeItem(Scope scope) {
		this.ctx = scope;
	}

	public static BasicBlock makeBasicBlock(Scope scope) {
		return new BasicBlock(scope);
	}

	public static Scope makeSubScope(Scope parent) {
		return new Scope(parent);
	}

	public static FuncScope makeFunction(Scope parent, String name, FuncDesc desc) {
		return new FuncScope(parent, name, desc);
	}

	public static Variable makeVariable(Scope ctx, String name, TypeDesc ty) {
		return new Variable(ctx, name, ty);
	}

	public static Temporary makeTemp(Scope ctx, TypeDesc ty) {
		return new Temporary(ctx, ty);
	}

	public static Constant makeConst(int v) {
		return new Constant(v);
	}

	public static Constant makeConst(String s) {
		return new Constant(s);
	}
}
