package AbsSytree;

public class SynAgg extends AbsSynNode {
	public SynDeclList dlist;
	public SynExprSeq eseq;

	public SynAgg(SynDeclList dlst, SynExprSeq eseq) {
		this.dlist = dlst;
		this.eseq = eseq;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<LET$" + exprType + "$>{");
		this.dlist.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "{");
		if (this.eseq != null)
			this.eseq.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		if (this.dlist != null)
			this.dlist.clearAttr();
		if (this.eseq != null)
			this.eseq.clearAttr();

	}
}
