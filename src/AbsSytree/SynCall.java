package AbsSytree;

public class SynCall extends AbsSynNode {
	public String procName;
	public SynExprList elst;

	public SynCall(String procName, SynExprList elst) {
		this.procName = procName;
		this.elst = elst;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<CALL " + procName + "$" + exprType + "$>{");
		if (elst != null)
			elst.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		if (this.elst != null)
			this.elst.clearAttr();
	}

}
