package AbsSytree;

import java.util.ArrayList;

public class SynFuncDeclList extends AbsSynNode {
	public ArrayList<SynFuncDecl> decls;

	public SynFuncDeclList(SynFuncDecl fdecl) {
		this.decls = new ArrayList<SynFuncDecl>();
		this.decls.add(fdecl);
	}

	@Override
	public void clearAttr() {
		this.attr = null;
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

	public SynFuncDeclList append(SynFuncDecl fdecl) {
		this.decls.add(fdecl);
		return this;
	}
}
