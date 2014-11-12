package AbsSytree;

public class SynFor extends AbsSynNode {
	public String iname;
	public AbsSynNode efrom, eto, e;

	public SynFor(String iname, AbsSynNode efrom, AbsSynNode eto, AbsSynNode e) {
		this.iname = iname;
		this.efrom = efrom;
		this.eto = eto;
		this.e = e;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<FOR " + iname + "$" + exprType + "$>{");
		efrom.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "<TO>{");
		eto.dumpNode(indent + 2);
		dumpFormat(indent, "}");
		dumpFormat(indent, "<DO>{");
		e.dumpNode(indent + 2);
		dumpFormat(indent, "}");

	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.efrom.clearAttr();
		this.eto.clearAttr();
		this.e.clearAttr();
	}

}
