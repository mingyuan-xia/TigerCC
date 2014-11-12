package AbsSytree;

public class SynNil extends AbsSynNode {
	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<NIL$" + exprType + "$>");
	}

	@Override
	public void clearAttr() {
		this.attr = null;

	}

}
