package AbsSytree;

import java.util.ArrayList;

public class SynExprList extends AbsSynNode {
	public ArrayList<AbsSynNode> exprs;

	public SynExprList(AbsSynNode e) {
		this.exprs = new ArrayList<AbsSynNode>();
		this.exprs.add(e);
	}

	public SynExprList append(AbsSynNode e) {
		this.exprs.add(e);
		return this;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		for (int i = 0; i < this.exprs.size(); ++i) {
			this.exprs.get(i).dumpNode(indent);
			this.dumpFormat(indent, ",");
		}
	}

	@Override
	public void clearAttr() {
		this.attr = null;
		for (int i = 0; i < this.exprs.size(); ++i)
			this.exprs.get(i).clearAttr();
	}

}
