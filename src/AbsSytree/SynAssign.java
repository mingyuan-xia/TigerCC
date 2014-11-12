package AbsSytree;

public class SynAssign extends AbsSynNode {
	public AbsSynNode lv = null;
	public AbsSynNode expr = null;

	public SynAssign(AbsSynNode lv, AbsSynNode e) {
		this.lv = lv;
		this.expr = e;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<=" + exprType + ">{");
		lv.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "{");
		expr.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.lv.clearAttr();
		this.expr.clearAttr();
	}

}
