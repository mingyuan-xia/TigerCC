package AbsSytree;

public class SynArrayType extends AbsSynNode {
	public SynTypeID arrayeeType;

	public SynArrayType(SynTypeID arrayeeType) {
		this.arrayeeType = arrayeeType;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<ARRAY_OF$" + exprType + "$>");
		this.arrayeeType.dumpNode(indent + 2);
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.arrayeeType.clearAttr();

	}

}
