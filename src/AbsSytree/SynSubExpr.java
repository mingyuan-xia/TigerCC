package AbsSytree;

public class SynSubExpr extends AbsSynNode {
	public AbsSynNode expr = null;

	public SynSubExpr(AbsSynNode e) {
		this.expr = e;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<->{");
		expr.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.expr.clearAttr();

	}

}
