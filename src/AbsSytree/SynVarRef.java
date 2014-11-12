package AbsSytree;

public class SynVarRef extends AbsSynNode {
	public String varName;

	public SynVarRef(String varName) {
		this.varName = varName;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<VARREF$" + exprType + "$>{" + this.varName + "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
	}
}
