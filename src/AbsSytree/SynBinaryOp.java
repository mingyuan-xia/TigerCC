package AbsSytree;

public class SynBinaryOp extends AbsSynNode {
	public AbsSynNode el;
	public int op;
	public AbsSynNode er;

	public SynBinaryOp(AbsSynNode el, int op, AbsSynNode er) {
		this.el = el;
		this.op = op;
		this.er = er;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<BOP " + op + "$" + exprType + "$>{");
		el.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "{");
		er.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.el.clearAttr();
		this.er.clearAttr();

	}

}
