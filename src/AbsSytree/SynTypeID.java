package AbsSytree;

public class SynTypeID extends AbsSynNode {

	public String typeName;

	public SynTypeID(String typename) {
		this.typeName = typename;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<TYPE$" + exprType + "$:" + typeName + ">");

	}

	@Override
	public void clearAttr() {
		this.attr = null;
	}

}
