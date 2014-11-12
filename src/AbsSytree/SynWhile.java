package AbsSytree;

public class SynWhile extends AbsSynNode {
	public AbsSynNode cond;
	public AbsSynNode e;

	public SynWhile(AbsSynNode cond, AbsSynNode e) {
		this.cond = cond;
		this.e = e;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<WHILE$" + exprType + "$>{");
		cond.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "<DO>{");
		e.dumpNode(indent + 2);
		dumpFormat(indent, "}");

	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.cond.clearAttr();
		this.e.clearAttr();
	}

}
