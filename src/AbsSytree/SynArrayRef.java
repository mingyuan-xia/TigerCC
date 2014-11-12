package AbsSytree;

public class SynArrayRef extends AbsSynNode {
	public AbsSynNode lv;
	public AbsSynNode e;

	public SynArrayRef(AbsSynNode lv, AbsSynNode e) {
		this.lv = lv;
		this.e = e;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<LVALUE$" + exprType + "$[]>{");
		lv.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "{");
		e.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.lv.clearAttr();
		this.e.clearAttr();
	}

}
