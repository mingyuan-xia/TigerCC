package AbsSytree;

import java.util.ArrayList;

public class SynFieldList extends AbsSynNode {
	public ArrayList<String> names;
	public ArrayList<AbsSynNode> exprs;

	public SynFieldList(String name, AbsSynNode e) {
		this.names = new ArrayList<String>();
		this.exprs = new ArrayList<AbsSynNode>();
		this.names.add(name);
		this.exprs.add(e);
	}

	public SynFieldList append(String name, AbsSynNode e) {
		this.names.add(name);
		this.exprs.add(e);
		return this;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		for (int i = 0; i < this.names.size(); ++i) {
			// TODO
		}
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		for (int i = 0; i < this.exprs.size(); ++i)
			this.exprs.get(i).clearAttr();

	}

}
