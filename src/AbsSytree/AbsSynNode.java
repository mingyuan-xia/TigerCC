package AbsSytree;

import util.TypeDesc;

public abstract class AbsSynNode {
	public TypeDesc exprType;
	public Object attr;

	public abstract void dumpNode(int indent);

	public abstract Object visit(SynNodeVisitor visitor);

	public abstract void clearAttr();

	protected void dumpFormat(int indent, String msg) {
		int i;
		for (i = 0; i < indent; ++i)
			System.out.print(" ");
		System.out.println(msg);
	}
}
