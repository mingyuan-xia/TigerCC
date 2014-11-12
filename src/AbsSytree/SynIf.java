package AbsSytree;

public class SynIf extends AbsSynNode {
	public AbsSynNode cond, ife, elsee;

	public SynIf(AbsSynNode cond, AbsSynNode ife, AbsSynNode elsee) {
		this.cond = cond;
		this.ife = ife;
		this.elsee = elsee;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<IF$" + exprType + "$>{");
		cond.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "<THEN>{");
		ife.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "<ELSE>{");
		if (elsee != null)
			elsee.dumpNode(indent + 2);
		dumpFormat(indent, "}");

	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.cond.clearAttr();
		this.ife.clearAttr();
		if (this.elsee != null)
			this.elsee.clearAttr();
	}

}
