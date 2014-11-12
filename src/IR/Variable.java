package IR;

import util.TypeDesc;

public class Variable extends ScopeItem {
	public String name;
	public TypeDesc ty;
	public Variable(Scope ctx, String name, TypeDesc ty) {
		super(ctx);
		this.name = name;
		this.ty = ty;
	}

	@Override
	public String toString() {
		return name;
	}

}
