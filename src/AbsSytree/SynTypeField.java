package AbsSytree;

public class SynTypeField extends AbsSynNode {
	public String name;
	public String tyid;

	public SynTypeField(String name, String tyid) {
		this.name = name;
		this.tyid = tyid;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<TYPEFIELD>" + name + ":" + tyid);

	}

	@Override
	public void clearAttr() {
		this.attr = null;
	}

}
