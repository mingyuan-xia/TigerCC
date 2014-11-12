package AbsSytree;

public class SynInteger extends AbsSynNode {

	public int val;

	public SynInteger(String val) {
		this.val = Integer.parseInt(val);
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<INTEGER_LITERAL " + val + "$" + exprType + "$>");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
	}

}
