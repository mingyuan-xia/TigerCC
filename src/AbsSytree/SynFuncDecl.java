package AbsSytree;

import util.TypeDesc;

public class SynFuncDecl extends AbsSynNode {
	public String name;
	public SynTypeFieldList para;
	public String tyid;
	public AbsSynNode e;

	public SynFuncDecl(String name, SynTypeFieldList para, AbsSynNode e) {
		this.name = name;
		this.para = para;
		this.tyid = null;
		this.e = e;
	}

	public SynFuncDecl(String name, String tyid, SynTypeFieldList para,
			AbsSynNode e) {
		this.name = name;
		this.para = para;
		this.tyid = tyid;
		this.e = e;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<FUNCDEF " + name + " of " + tyid + ">{");
		if (para != null)
			para.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "{");
		e.dumpNode(indent + 2);
		dumpFormat(indent, "}");

	}

	@Override
	public void clearAttr() {
		this.attr = null;
		if (this.para != null)
			this.para.clearAttr();
		this.e.clearAttr();
	}

	public TypeDesc getFuncDesc() {
		return (TypeDesc) this.attr;
	}
}
