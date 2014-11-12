package AbsSytree;

import java.util.ArrayList;

public class SynDeclList extends AbsSynNode {
	public ArrayList<AbsSynNode> decls;

	public SynDeclList(AbsSynNode decl) {
		this.decls = new ArrayList<AbsSynNode>();
		this.decls.add(decl);
	}

	public SynDeclList append(AbsSynNode decl) {
		this.decls.add(decl);
		return this;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		for (int i = 0; i < this.decls.size(); ++i)
			this.decls.get(i).dumpNode(indent);
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		for (int i = 0; i < this.decls.size(); ++i)
			this.decls.get(i).clearAttr();

	}

}
