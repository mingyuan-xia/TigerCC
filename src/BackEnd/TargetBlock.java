package BackEnd;

import java.util.ArrayList;

import IR.BasicBlock;

public class TargetBlock {
	private BasicBlock myIR;
	public ArrayList<CrtInstr> instructions;
	
	public TargetBlock(BasicBlock myir)
	{
		this.myIR = myir;
		this.instructions = new ArrayList<CrtInstr>(); 
	}
	@Override
	public String toString()
	{
		return myIR.toString();
	}
	public String outputAll()
	{
		String s = this.toString()+":\n";
		for (int i = 0; i < this.instructions.size(); ++i)
			s += this.instructions.get(i).toString();
		return s;
	}

}
