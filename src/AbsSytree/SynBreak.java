package AbsSytree;

public class SynBreak extends AbsSynNode {
	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<BREAK$" + exprType + "$>");
	}

	@Override
	public void clearAttr() {
		this.attr = null;

	}

}
