package AbsSytree;

import java.util.ArrayList;

public class SynTypeFieldList extends AbsSynNode {
	public ArrayList<SynTypeField> tyfields;

	public SynTypeFieldList(SynTypeField tf) {
		this.tyfields = new ArrayList<SynTypeField>();
		this.tyfields.add(tf);
	}

	public SynTypeFieldList append(SynTypeField tf) {
		this.tyfields.add(tf);
		return this;
	}

	@Override
	public Object visit(SynNodeVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public void dumpNode(int indent) {
		for (int i = 0; i < this.tyfields.size(); ++i) {
			this.tyfields.get(i).dumpNode(indent);
			this.dumpFormat(indent, ",");
		}

	}

	@Override
	public void clearAttr() {
		this.attr = null;
		for (int i = 0; i < this.tyfields.size(); ++i)
			this.tyfields.get(i).clearAttr();
	}

}
