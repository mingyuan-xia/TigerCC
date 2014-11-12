package AbsSytree;

public class SynRecordRef extends AbsSynNode {
	public AbsSynNode lv;
	public String id;

	public SynRecordRef(AbsSynNode lv, String id) {
		this.lv = lv;
		this.id = id;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<LVALUE$" + exprType + "$." + this.id + ">{");
		lv.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.lv.clearAttr();

	}

}
