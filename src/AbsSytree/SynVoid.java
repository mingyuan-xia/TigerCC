package AbsSytree;

public class SynVoid extends AbsSynNode {

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<VOID$" + exprType + "$>");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
	}

}
