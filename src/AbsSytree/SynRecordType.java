package AbsSytree;

public class SynRecordType extends AbsSynNode {
	public SynTypeFieldList tyfl;

	public SynRecordType(SynTypeFieldList tyfl) {
		this.tyfl = tyfl;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<RECORD TYPE$" + exprType + "$>{");
		tyfl.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.tyfl.clearAttr();
	}
}
