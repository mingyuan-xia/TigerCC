package AbsSytree;

public class SynString extends AbsSynNode {

	public String val;

	public SynString(String val) {
		this.val = val;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<STRING_LITERAL$" + exprType + "$>{\"" + val
				+ "\"}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;

	}

}
