package AbsSytree;

public class SynRecord extends AbsSynNode {

	public String rname;
	public SynFieldList flist;

	public SynRecord(String rname, SynFieldList flst) {
		this.rname = rname;
		this.flist = flst;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<RECORD$" + exprType + "$" + rname + ">{");
		flist.dumpNode(indent + 2);
		dumpFormat(indent, "}");

	}

	@Override
	public void clearAttr() {
		this.attr = null;
		if (this.flist != null)
			this.flist.clearAttr();

	}

}
