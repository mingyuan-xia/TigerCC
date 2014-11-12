package IR;

import util.ErrorMsg;

public class AbsInstr {
	public static final int INS_MISC_FRAME_ENTER = 1;
	public static final int INS_MISC_FRAME_LEVAVE = 2;
	public static final int INS_MISC_NEWRECORD = 3;
	public static final int INS_MISC_NEWARRAY = 4;

	public static final int INS_JUMP_EQ = 10;
	public static final int INS_JUMP_NEQ = 11;
	public static final int INS_JUMP_LESS = 12;
	public static final int INS_JUMP_LESSEQ = 13;
	public static final int INS_JUMP_GREATER = 14;
	public static final int INS_JUMP_GREATER_EQ = 15;
	public static final int INS_JUMP = 16;

	public static final int INS_OP_NEG = 21;
	public static final int INS_OP_ADD = 22;
	public static final int INS_OP_SUB = 23;
	public static final int INS_OP_MUL = 24;
	public static final int INS_OP_DIV = 25;
	public static final int INS_OP_AND = 26;
	public static final int INS_OP_OR = 27;

	public static final int INS_OP_GREATER = 30;
	public static final int INS_OP_GREATEREQ = 31;
	public static final int INS_OP_NEQ = 32;
	public static final int INS_OP_LESS = 33;
	public static final int INS_OP_LESSEQ = 34;
	public static final int INS_OP_EQ = 35;

	public static final int INS_OP_SUBSCRIPT = 40;
	public static final int INS_OP_INDEX = 41;
	public static final int INS_OP_ASSIGN = 42;
	public static final int INS_OP_PUSH = 43;
	//public static final int INS_OP_POP = 44;
	public static final int INS_OP_CALL = 45;
	public static final int INS_OP_GETFIELD = 46;
	public static final int INS_OP_SETFIELD = 47;
	
	public int op;
	public ScopeItem opr0, opr1, opr2;

	public AbsInstr(int op, ScopeItem opr0, ScopeItem opr1, ScopeItem opr2) {
		this.op = op;
		this.opr0 = opr0;
		this.opr1 = opr1;
		this.opr2 = opr2;
	}

	@Override
	public String toString() {
		switch (this.op) {
		case INS_MISC_FRAME_ENTER:
			return "frame_enter\n";
		case INS_MISC_FRAME_LEVAVE:
			if (this.opr0 == null)
				return "frame_leave\n";
			else
				return "frame_leave"+this.opr0.toString()+"\n";
		case INS_MISC_NEWRECORD:
			return this.opr0.toString() + "=newrecord[" + this.opr1.toString()
					+ "]\n";
		case INS_MISC_NEWARRAY:
			return this.opr0.toString() + "=newarray[" + this.opr1.toString()
					+ "]of" + this.opr2.toString() + "\n";
			
		case INS_JUMP_EQ:
			return "if " + this.opr0.toString() + "==0 then "
					+ this.opr1.toString() + " else " + this.opr2.toString()
					+ "\n";
		case INS_JUMP_NEQ:
			return "if " + this.opr0.toString() + "!=0 then "
					+ this.opr1.toString() + " else " + this.opr2.toString()
					+ "\n";
		case INS_JUMP_LESS:
			return "if " + this.opr0.toString() + "<0 then "
					+ this.opr1.toString() + " else " + this.opr2.toString()
					+ "\n";
		case INS_JUMP_LESSEQ:
			return "if " + this.opr0.toString() + "<=0 then "
					+ this.opr1.toString() + " else " + this.opr2.toString()
					+ "\n";
		case INS_JUMP_GREATER:
			return "if " + this.opr0.toString() + ">0 then "
					+ this.opr1.toString() + " else " + this.opr2.toString()
					+ "\n";
		case INS_JUMP_GREATER_EQ:
			return "if " + this.opr0.toString() + ">=0 then "
					+ this.opr1.toString() + " else " + this.opr2.toString()
					+ "\n";
		case INS_JUMP:
			return "jump " + this.opr0.toString() + "\n";

		case INS_OP_NEG:
			return this.opr0.toString() + "=-" + this.opr1.toString() + "\n";
		case INS_OP_ADD:
			return this.opr0.toString() + "=" + this.opr1.toString() + "+"
					+ this.opr2.toString() + "\n";
		case INS_OP_SUB:
			return this.opr0.toString() + "=" + this.opr1.toString() + "-"
					+ this.opr2.toString() + "\n";
		case INS_OP_MUL:
			return this.opr0.toString() + "=" + this.opr1.toString() + "*"
					+ this.opr2.toString() + "\n";
		case INS_OP_DIV:
			return this.opr0.toString() + "=" + this.opr1.toString() + "/"
					+ this.opr2.toString() + "\n";
		case INS_OP_AND:
			return this.opr0.toString() + "=" + this.opr1.toString() + "&"
					+ this.opr2.toString() + "\n";
		case INS_OP_OR:
			return this.opr0.toString() + "=" + this.opr1.toString() + "|"
					+ this.opr2.toString() + "\n";

		case INS_OP_GREATER:
			return this.opr0.toString() + "=(" + this.opr1.toString() + ">"
					+ this.opr2.toString() + ")\n";
		case INS_OP_GREATEREQ:
			return this.opr0.toString() + "=(" + this.opr1.toString() + ">="
					+ this.opr2.toString() + ")\n";
		case INS_OP_NEQ:
			return this.opr0.toString() + "=(" + this.opr1.toString() + "!="
					+ this.opr2.toString() + ")\n";
		case INS_OP_LESS:
			return this.opr0.toString() + "=(" + this.opr1.toString() + "<"
					+ this.opr2.toString() + ")\n";
		case INS_OP_LESSEQ:
			return this.opr0.toString() + "=(" + this.opr1.toString() + "<="
					+ this.opr2.toString() + ")\n";
		case INS_OP_EQ:
			return this.opr0.toString() + "=(" + this.opr1.toString() + "=="
					+ this.opr2.toString() + ")\n";

		case INS_OP_SUBSCRIPT:
			return this.opr0.toString() + "[" + this.opr1.toString() + "]="
					+ this.opr2.toString() + "\n";
		case INS_OP_INDEX:
			return this.opr0.toString() + "=" + this.opr1.toString() + "["
					+ this.opr2.toString() + "]\n";
		case INS_OP_ASSIGN:
			return this.opr0.toString() + "=" + this.opr1.toString() + "\n";
		case INS_OP_PUSH:
			return "push " + this.opr0.toString() + "\n";
		case INS_OP_CALL:
			if (this.opr0 == null)
				return "call " + this.opr1.toString()
				+ "of " + this.opr2.toString() + " args\n";
			else
				return this.opr0.toString() + "=call " + this.opr1.toString()
				+ "of " + this.opr2.toString() + " args\n";
		case INS_OP_GETFIELD:
			return this.opr0.toString() + "= " + this.opr1.toString()
					+ ".field[" + this.opr2.toString() + "]\n";
		case INS_OP_SETFIELD:
			return this.opr0.toString() + ".field[" + this.opr1.toString()
					+ "]=" + this.opr2.toString() + "\n";
		default:
			ErrorMsg.instance.err("unknown abstract instruction!");
			return null;
		}
	}
}
