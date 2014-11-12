package AbsSytree;

public class SynTypeDecl extends AbsSynNode {
	public String name;
	public AbsSynNode ty;

	public SynTypeDecl(String name, AbsSynNode ty) {
		this.name = name;
		this.ty = ty;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		dumpFormat(indent, "<TYPEDEF$" + exprType + "$ " + name + ">{");
		ty.dumpNode(indent + 2);
		dumpFormat(indent, "}");
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		this.ty.clearAttr();
	}

}
