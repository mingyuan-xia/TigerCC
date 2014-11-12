package IR;

import util.TypeDesc;

public class Temporary extends ScopeItem {
	private static int tempCounter = 0;
	public int myID;
	public TypeDesc ty;
	public Temporary(Scope ctx, TypeDesc ty) {
		super(ctx);
		this.myID = tempCounter++;
		this.ty = ty;
	}

	@Override
	public String toString() {
		return "$"+this.myID;
	}

}
