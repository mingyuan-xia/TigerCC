package IR;

import java.util.ArrayList;
import java.util.BitSet;

public class BasicBlock extends ScopeItem{
	public static int labCount = 0;

	public int labID;
	public ArrayList<AbsInstr> irIns;
	public BitSet inLiveness, outLiveness;
	public BitSet inBlocks, outBlocks;
	public BitSet defs, uses;
	
	public BasicBlock(Scope scope) {
		super(scope);
		this.labID = BasicBlock.labCount++;
		this.irIns = new ArrayList<AbsInstr>();
	}

	@Override
	public String toString() {
		return "label" + this.labID;
	}

	public String outputAll() {
		String s = this.toString() + ":\n";
		for (int i = 0; i < this.irIns.size(); ++i)
			s += this.irIns.get(i).toString();
		return s;
	}

}
