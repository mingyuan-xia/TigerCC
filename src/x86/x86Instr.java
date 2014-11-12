package x86;

import util.ErrorMsg;
import BackEnd.CrtInstr;
import BackEnd.TargetBlock;

public class x86Instr extends CrtInstr {
	public static final int INS_TODO = 0;
	public static final int INS_PUSH = 1;
	public static final int INS_MOV = 2;
	public static final int INS_SUB = 3;
	public static final int INS_ADD = 4;
	public static final int INS_RET = 5;
	public static final int INS_POP = 6;
	public static final int INS_NEG = 7;
	public static final int INS_MUL = 8;
	public static final int INS_DIV = 9;
	public static final int INS_AND = 10;
	public static final int INS_OR = 11;
	public static final int INS_JMP = 12;
	public static final int INS_JE = 13;
	public static final int INS_JNE = 14;
	public static final int INS_JL = 15;
	public static final int INS_JG = 16;
	public static final int INS_JLE = 17;
	public static final int INS_JGE = 18;
	public static final int INS_CMP = 19;
	public static final int INS_CALL = 20;
	public static final int INS_CMOVE = 21;
	public static final int INS_CMOVNE = 22;
	public static final int INS_CMOVL = 23;
	public static final int INS_CMOVG = 24;
	public static final int INS_CMOVLE = 25;
	public static final int INS_CMOVGE = 26;
	public static final int INS_LEA = 27;
	public static final int INS_SHL = 28;

	public x86Instr(int op, x86Access opr0, x86Access opr1, x86Access opr2, x86Access opr3) {
		super(op, opr0, opr1, opr2, opr3);
	}
	public x86Instr(int op, x86Access opr0, x86Access opr1, x86Access opr2) {
		super(op, opr0, opr1, opr2);
	}
	public x86Instr(int op, x86Access opr0, x86Access opr1) {
		super(op, opr0, opr1);
	}
	public x86Instr(int op, x86Access opr0) {
		super(op, opr0);
	}
	public x86Instr(int op) {
		super(op);
	}
	@Override
	public String toString() {
		switch (this.op)
		{
		case INS_TODO:
			return ";"+this.opr0.toString()+"\n";
		case INS_PUSH:
			return "push "+this.opr0.toString()+"\n";
		case INS_MOV:
			return "mov "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_SHL:
			return "shl "+this.opr0.toString()+",1\n";
		case INS_LEA:
			return "lea "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_SUB:
			return "sub "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_ADD:
			return "add "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_MUL:
			return "imul "+this.opr0.toString()+"\n";
		case INS_DIV:
			return "idiv "+this.opr0.toString()+"\n";
		case INS_AND:
			return "and "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_OR:
			return "or "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_RET:
			if (this.opr0 == null)
				return "ret\n";
			else
				return "ret "+this.opr0.toString()+"\n"; 
		case INS_POP:
			return "pop "+this.opr0.toString()+"\n";
		case INS_NEG:
			return "neg "+this.opr0.toString()+"\n";
		case INS_CMP:
			return "cmp "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_JMP:
			return "jmp "+this.opr0.toString()+"\n";
		case INS_JE:
			return "je "+this.opr0.toString()+"\n";
		case INS_JNE:
			return "jne "+this.opr0.toString()+"\n";
		case INS_JL:
			return "jl "+this.opr0.toString()+"\n";
		case INS_JLE:
			return "jle "+this.opr0.toString()+"\n";
		case INS_JG:
			return "jg "+this.opr0.toString()+"\n";
		case INS_JGE:
			return "jge "+this.opr0.toString()+"\n";
		case INS_CMOVE:
			return "cmove "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_CMOVNE:
			return "cmovne "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_CMOVL:
			return "cmovl "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_CMOVLE:
			return "cmovle "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_CMOVG:
			return "cmovg "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_CMOVGE:
			return "cmovge "+this.opr0.toString()+","+this.opr1.toString()+"\n";
		case INS_CALL:
			return "call "+this.opr0.toString()+"\n";
		default:
			return null;
		}
	}

	public static x86Instr newInsPush(TargetBlock block, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_PUSH, a1);
		block.instructions.add(ins);
		return ins;
	}

	public static x86Instr newInsMov(TargetBlock block, x86Access a1,
			x86Access a2) {
		ErrorMsg.instance.errChk(!a1.isMemory() || !a2.isMemory(), "x86 does not suppot mem to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_MOV, a1, a2);
		block.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsLea(TargetBlock block, x86Access a1,
			x86Access a2) {
		ErrorMsg.instance.errChk(!a1.isMemory() || !a2.isMemory(), "x86 does not suppot mem to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_LEA, a1, a2);
		block.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsCmove(TargetBlock tb, x86Access a1, x86Access a2) {
		x86Instr ins = new x86Instr(x86Instr.INS_CMOVE, a1, a2);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsCmovne(TargetBlock tb, x86Access a1, x86Access a2) {
		x86Instr ins = new x86Instr(x86Instr.INS_CMOVNE, a1, a2);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsCmovl(TargetBlock tb, x86Access a1, x86Access a2) {
		x86Instr ins = new x86Instr(x86Instr.INS_CMOVL, a1, a2);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsCmovle(TargetBlock tb, x86Access a1, x86Access a2) {
		x86Instr ins = new x86Instr(x86Instr.INS_CMOVLE, a1, a2);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsCmovg(TargetBlock tb, x86Access a1, x86Access a2) {
		x86Instr ins = new x86Instr(x86Instr.INS_CMOVG, a1, a2);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsCmovge(TargetBlock tb, x86Access a1, x86Access a2) {
		x86Instr ins = new x86Instr(x86Instr.INS_CMOVGE, a1, a2);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsAnd(TargetBlock block, x86Access a1,
			x86Access a2) {
		ErrorMsg.instance.errChk(!a1.isMemory() || !a2.isMemory(), "x86 does not suppot mem to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_AND, a1, a2);
		block.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsOr(TargetBlock block, x86Access a1,
			x86Access a2) {
		ErrorMsg.instance.errChk(!a1.isMemory() || !a2.isMemory(), "x86 does not suppot mem to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_OR, a1, a2);
		block.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsMul(TargetBlock block, x86Access a1) {
		ErrorMsg.instance.errChk(!a1.isMemory(), "x86 does not suppot mem to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_MUL, a1);
		block.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsDiv(TargetBlock block, x86Access a1) {
		ErrorMsg.instance.errChk(!a1.isMemory(), "x86 does not suppot mem to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_DIV, a1);
		block.instructions.add(ins);
		return ins;
	}

	public static x86Instr newInsSub(TargetBlock block, x86Access a1,
			x86Access a2) {
		ErrorMsg.instance.errChk(!a1.isMemory() || !a2.isMemory(), "x86 does not suppot mem to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_SUB, a1, a2);
		block.instructions.add(ins);
		return ins;
	}

	public static x86Instr newInsAdd(TargetBlock block, x86Access a1,
			x86Access a2) {
		ErrorMsg.instance.errChk(!a1.isMemory() || !a2.isMemory(), "x86 does not suppot mem to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_ADD, a1, a2);
		block.instructions.add(ins);
		return ins;
	}

	public static x86Instr newInsRet(TargetBlock block, x86Access a1) {
		ErrorMsg.instance.errChk(a1.isAbsolute(), "x86 does not suppot ret without argument not absolute val!");
		x86Instr ins = new x86Instr(x86Instr.INS_RET, a1);
		block.instructions.add(ins);
		return ins;
	}

	public static x86Instr newInsRet(TargetBlock block) {
		x86Instr ins = new x86Instr(x86Instr.INS_RET);
		block.instructions.add(ins);
		return ins;
	}

	public static x86Instr newInsPop(TargetBlock block, x86Access a1) {
		ErrorMsg.instance.errChk(!a1.isMemory(), "x86 does not suppot pop to mem instruction!");
		x86Instr ins = new x86Instr(x86Instr.INS_POP, a1);
		block.instructions.add(ins);
		return ins;
	}
	
	public static x86Instr newInsNeg(TargetBlock block, x86Access a1) {
		ErrorMsg.instance.errChk(a1.isRegister(), "x86 does not suppot neg without register!");
		x86Instr ins = new x86Instr(x86Instr.INS_NEG, a1);
		block.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsShl(TargetBlock block, x86Access a1) {
		ErrorMsg.instance.errChk(a1.isRegister(), "x86 does not suppot neg without register!");
		x86Instr ins = new x86Instr(x86Instr.INS_SHL, a1);
		block.instructions.add(ins);
		return ins;
	}

	public static x86Instr newInsCmp(TargetBlock tb, x86Access a1,
			x86Access a2) {
		x86Instr ins = new x86Instr(x86Instr.INS_CMP, a1, a2);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsJmp(TargetBlock tb, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_JMP, a1);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsJe(TargetBlock tb, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_JE, a1);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsJne(TargetBlock tb, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_JNE, a1);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsJl(TargetBlock tb, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_JL, a1);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsJle(TargetBlock tb, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_JLE, a1);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsJg(TargetBlock tb, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_JG, a1);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsJge(TargetBlock tb, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_JGE, a1);
		tb.instructions.add(ins);
		return ins;
	}
	public static x86Instr newInsCall(TargetBlock tb, x86Access a1) {
		x86Instr ins = new x86Instr(x86Instr.INS_CALL, a1);
		tb.instructions.add(ins);
		return ins;
	}	
	public static x86Instr newInsTODO(TargetBlock block, String comment)
	{
		x86Instr ins = new x86Instr(x86Instr.INS_TODO, new x86Access(comment));
		block.instructions.add(ins);
		return ins;
	}
}
