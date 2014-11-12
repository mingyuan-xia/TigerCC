package AbsSytree;

public class SynVarDecl extends AbsSynNode {
	public String vname;
	public String tyid;
	public AbsSynNode e;

	public SynVarDecl(String vname, AbsSynNode expr) {
		this.vname = vname;
		this.tyid = null;
		this.e = expr;
	}

	public SynVarDecl(String vname, String tyid, AbsSynNode expr) {
		this.vname = vname;
		this.tyid = tyid;
		this.e = expr;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<VARDEF$" + exprType + "$" + vname + ">{");
		e.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.e.clearAttr();
	}

}
