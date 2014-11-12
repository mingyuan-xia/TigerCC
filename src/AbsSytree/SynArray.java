package AbsSytree;

public class SynArray extends AbsSynNode {
	public String aname;
	public AbsSynNode indexExpr;
	public AbsSynNode valExpr;

	public SynArray(String aname, AbsSynNode indexExpr, AbsSynNode valExpr) {
		this.aname = aname;
		this.indexExpr = indexExpr;
		this.valExpr = valExpr;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<ARRAY " + aname + "$" + exprType + "$>[");
		indexExpr.dumpNode(indent + 2);
		dumpFormat(indent, "]");
		dumpFormat(indent, "{");
		valExpr.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.indexExpr.clearAttr();
		this.valExpr.clearAttr();
	}

}
