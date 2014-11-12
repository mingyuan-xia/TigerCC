package AbsSytree;

import java.util.ArrayList;

public class SynTypeDeclList extends AbsSynNode {
	public ArrayList<SynTypeDecl> decls;

	public SynTypeDeclList(SynTypeDecl tdecl) {
		this.decls = new ArrayList<SynTypeDecl>();
		this.decls.add(tdecl);
	}

	@Override
	public void clearAttr() {
		this.attr = 0;
		for (int i = 0; i < this.decls.size(); ++i)
			this.decls.get(i).clearAttr();
	}

	@Override
	public void dumpNode(int indent) {
		for (int i = 0; i < this.decls.size(); ++i)
			this.decls.get(i).dumpNode(indent);
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	public SynTypeDeclList append(SynTypeDecl tdecl) {
		this.decls.add(tdecl);
		return this;
	}
}
