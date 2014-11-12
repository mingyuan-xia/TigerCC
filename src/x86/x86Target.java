package x86;

import java.util.ArrayList;

import util.ErrorMsg;
import util.TypeDesc;

import BackEnd.*;
import IR.*;

public class x86Target extends Target {
	public ArrayList<String> strLiterals;
	public ArrayList<TargetFrame> framePtrList;
	private Register[] allocState;
	private int nextReg = 0;
	public Register regEAX, regEBX, regECX, regEDX, regESI, regEDI;
	public Register regEBP, regESP;

	public x86Target() {
		initRegs();
		this.strLiterals = new ArrayList<String>();
		framePtrList = new ArrayList<TargetFrame>();
	}

	private void initRegs() {
		this.regEAX = new Register("eax");
		this.regEBX = new Register("ebx");
		this.regECX = new Register("ecx");
		this.regEDX = new Register("edx");
		this.regESI = new Register("esi");
		this.regEDI = new Register("edi");

		this.regEBP = new Register("ebp");
		this.regESP = new Register("esp");
		this.regSet = new Register[] { regEAX, regEBX, regECX, regEDX, regESI,
				regEDI, regEBP, regESP };
		this.allocState = new Register[] { regEAX, regEBX, regECX, regEDX,
				regESI, regEDI };
	}

	private Register findItem(ScopeItem u) {
		for (int i = 0; i < this.allocState.length; ++i)
			if (this.allocState[i].data == u)
				return this.allocState[i];
		return null;
	}

	private void loadItem(TargetBlock tb, Register r1, ScopeItem opr1) {
		ErrorMsg.instance.errChk(r1.isAvailable(),
				"load item with register not available!");
		x86TargetScope s = (x86TargetScope) this.curFrame;
		x86Access myAccess = new x86Access(r1);
		x86Access ref = (x86Access) s.refItem(opr1);
		if (opr1.ctx == this.curFrame.absScope) {
			if (ref == null) // not persistent yet
				ref = (x86Access) this.curFrame.addLocal(opr1);
			x86Instr.newInsMov(tb, myAccess, ref);
		} else {
			while (ref == null) {
				s = (x86TargetScope) s.parent;
				ErrorMsg.instance.errChk(s != null, "where is the item?");
				ref = (x86Access) s.refItem(opr1);
			}
			x86Instr.newInsPush(tb, new x86Access(regEBP));
			x86Instr.newInsMov(tb, new x86Access(regEBP), new x86Access("__"+s.toString()+"_fp"));
			x86Instr.newInsMov(tb, myAccess, ref);
			x86Instr.newInsPop(tb, new x86Access(regEBP));
		}
		r1.bind(opr1);
		r1.synced = true;
	}

	@Override
	protected Register allocReg(TargetBlock tb) {
		for (int i = 0; i < this.allocState.length; ++i)
			if (this.allocState[i].isAvailable())
				return this.allocState[i];
		do {
		nextReg = (nextReg + 1) % 6;
		} while (this.allocState[nextReg].locked);
		return this.spillReg(tb, this.allocState[nextReg]);
	}

	@Override
	protected TargetFrame makeFrame(Scope scope, TargetFrame parent) {
		return new x86TargetScope(scope, parent, this);
	}

	private Register fetchItem(TargetBlock tb, ScopeItem u) {
		Register retVal;
		if (u instanceof Constant) {
			Constant c = (Constant) u;
			retVal = this.allocReg(tb);
			if (c.data instanceof Integer)
				x86Instr.newInsMov(tb, new x86Access(retVal), new x86Access(
						((Integer) c.data).intValue()));
			else
				x86Instr.newInsLea(tb, new x86Access(retVal), new x86Access(
						regStrLiteral((String)c.data)));
			retVal.bind(u);
		} else {
			retVal = this.findItem(u);
			if (retVal == null) {
				retVal = this.allocReg(tb);
				this.loadItem(tb, retVal, u);
			}
		}
		return retVal;
	}

	private String regStrLiteral(String string) {
		this.strLiterals.add(string);
		return "stringConst"+(this.strLiterals.size()-1);
	}
	@Override
	protected void genHook(TargetFrame s)
	{
		this.framePtrList.add(s);
	}
	private void btran(TargetBlock tb, AbsInstr ains) {
		// static binary translation
		Register r1, r2, r3, r4;
		switch (ains.op) {
		case AbsInstr.INS_MISC_FRAME_ENTER:
			this.curFrame.generateEntry(tb);
			break;
		case AbsInstr.INS_MISC_FRAME_LEVAVE:
			r1 = this.fetchItem(tb, ains.opr0);
			this.spillAll(tb);
			x86Instr.newInsMov(tb, new x86Access(regEAX), new x86Access(r1));
			this.curFrame.generateExit(tb);
			break;
		case AbsInstr.INS_MISC_NEWRECORD:
			this.spillAll(tb);
			regEAX.locked = true;
			r1 = this.fetchItem(tb, ains.opr1);
			x86Instr.newInsPush(tb, new x86Access(r1));
			x86Instr.newInsCall(tb, new x86Access("tg_newrecord"));
			x86Instr.newInsAdd(tb, new x86Access(regESP), new x86Access(4));
			regEAX.locked = false;
			regEAX.bind(ains.opr0);
			regEAX.synced = false;
			r1.free();
			break;
		case AbsInstr.INS_MISC_NEWARRAY:
			this.spillAll(tb);
			regEAX.locked = true;
			r2 = this.fetchItem(tb, ains.opr1);
			r2.locked = true;
			r3 = this.fetchItem(tb, ains.opr2);
			x86Instr.newInsPush(tb, new x86Access(r3));
			x86Instr.newInsPush(tb, new x86Access(r2));
			x86Instr.newInsCall(tb, new x86Access("tg_newarray"));
			x86Instr.newInsAdd(tb, new x86Access(regESP), new x86Access(8));
			r2.locked = false;
			regEAX.locked = false;
			regEAX.bind(ains.opr0);
			regEAX.synced = false;
			r2.free();
			r3.free();
			break;

		case AbsInstr.INS_JUMP_EQ:
			r1 = this.findItem(ains.opr0);
			this.spillAll(tb);
			if (r1 == null)
				r1 = this.fetchItem(tb, ains.opr0);
			x86Instr.newInsCmp(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsJe(tb, new x86Access(((BasicBlock) ains.opr1)
					.toString()));
			x86Instr.newInsJmp(tb, new x86Access(((BasicBlock) ains.opr2)
					.toString()));
			r1.free();
			break;
		case AbsInstr.INS_JUMP_NEQ:
			r1 = this.findItem(ains.opr0);
			this.spillAll(tb);
			if (r1 == null)
				r1 = this.fetchItem(tb, ains.opr0);
			x86Instr.newInsCmp(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsJne(tb, new x86Access(((BasicBlock) ains.opr1)
					.toString()));
			x86Instr.newInsJmp(tb, new x86Access(((BasicBlock) ains.opr2)
					.toString()));
			r1.free();
			break;
		case AbsInstr.INS_JUMP_LESS:
			r1 = this.findItem(ains.opr0);
			this.spillAll(tb);
			if (r1 == null)
				r1 = this.fetchItem(tb, ains.opr0);
			x86Instr.newInsCmp(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsJl(tb, new x86Access(((BasicBlock) ains.opr1)
					.toString()));
			x86Instr.newInsJmp(tb, new x86Access(((BasicBlock) ains.opr2)
					.toString()));
			r1.free();
			break;
		case AbsInstr.INS_JUMP_LESSEQ:
			r1 = this.findItem(ains.opr0);
			this.spillAll(tb);
			if (r1 == null)
				r1 = this.fetchItem(tb, ains.opr0);
			x86Instr.newInsCmp(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsJle(tb, new x86Access(((BasicBlock) ains.opr1)
					.toString()));
			x86Instr.newInsJmp(tb, new x86Access(((BasicBlock) ains.opr2)
					.toString()));
			r1.free();
			break;
		case AbsInstr.INS_JUMP_GREATER:
			r1 = this.findItem(ains.opr0);
			this.spillAll(tb);
			if (r1 == null)
				r1 = this.fetchItem(tb, ains.opr0);
			x86Instr.newInsCmp(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsJg(tb, new x86Access(((BasicBlock) ains.opr1)
					.toString()));
			x86Instr.newInsJmp(tb, new x86Access(((BasicBlock) ains.opr2)
					.toString()));
			r1.free();
			break;
		case AbsInstr.INS_JUMP_GREATER_EQ:
			r1 = this.findItem(ains.opr0);
			this.spillAll(tb);
			if (r1 == null)
				r1 = this.fetchItem(tb, ains.opr0);
			x86Instr.newInsCmp(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsJge(tb, new x86Access(((BasicBlock) ains.opr1)
					.toString()));
			x86Instr.newInsJmp(tb, new x86Access(((BasicBlock) ains.opr2)
					.toString()));
			r1.free();
			break;
		case AbsInstr.INS_JUMP:
			this.spillAll(tb);
			x86Instr.newInsJmp(tb, new x86Access(((BasicBlock) ains.opr0)
					.toString()));
			break;

		case AbsInstr.INS_OP_NEG:
			r1 = this.fetchItem(tb, ains.opr1);
			this.spillReg(tb, r1);
			x86Instr.newInsNeg(tb, new x86Access(r1));
			r1.bind(ains.opr0);
			r1.synced = false;
			break;

		case AbsInstr.INS_OP_ADD:
			r1 = this.fetchItem(tb, ains.opr1);
			this.spillReg(tb, r1);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr2);
			x86Instr.newInsAdd(tb, new x86Access(r1), new x86Access(r2));
			r1.locked = false;
			r1.synced = false;
			r1.bind(ains.opr0);
			break;
		case AbsInstr.INS_OP_SUB:
			r1 = this.fetchItem(tb, ains.opr1);
			this.spillReg(tb, r1);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr2);
			x86Instr.newInsSub(tb, new x86Access(r1), new x86Access(r2));
			r1.locked = false;
			r1.synced = false;
			r1.bind(ains.opr0);
			break;
		case AbsInstr.INS_OP_MUL:
			this.spillReg(tb, regEAX);
			regEAX.locked = true;
			this.spillReg(tb, regEDX);
			regEDX.locked = true;
			r1 = this.fetchItem(tb, ains.opr1);
			x86Instr.newInsMov(tb, new x86Access(regEAX), new x86Access(r1));
			x86Instr.newInsMov(tb, new x86Access(regEDX), new x86Access(0));
			this.spillReg(tb, r1);
			r2 = this.fetchItem(tb, ains.opr2);
			x86Instr.newInsMul(tb, new x86Access(r2));
			regEAX.locked = false;
			regEAX.bind(ains.opr0);
			regEAX.synced = false;
			regEDX.locked = false;
			break;
		case AbsInstr.INS_OP_DIV:
			this.spillReg(tb, regEAX);
			regEAX.locked = true;
			this.spillReg(tb, regEDX);
			regEDX.locked = true;
			r1 = this.fetchItem(tb, ains.opr1);
			x86Instr.newInsMov(tb, new x86Access(regEAX), new x86Access(r1));
			x86Instr.newInsMov(tb, new x86Access(regEDX), new x86Access(0));
			this.spillReg(tb, r1);
			r2 = this.fetchItem(tb, ains.opr2);
			x86Instr.newInsDiv(tb, new x86Access(r2));
			regEAX.locked = false;
			regEAX.bind(ains.opr0);
			regEAX.synced = false;
			regEDX.locked = false;
			break;
		case AbsInstr.INS_OP_AND:
			r1 = this.fetchItem(tb, ains.opr1);
			this.spillReg(tb, r1);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr2);
			x86Instr.newInsAnd(tb, new x86Access(r1), new x86Access(r2));
			r1.locked = false;
			r1.synced = false;
			r1.bind(ains.opr0);
			break;
		case AbsInstr.INS_OP_OR:
			r1 = this.fetchItem(tb, ains.opr1);
			this.spillReg(tb, r1);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr2);
			x86Instr.newInsOr(tb, new x86Access(r1), new x86Access(r2));
			r1.locked = false;
			r1.synced = false;
			r1.bind(ains.opr0);
			break;

		case AbsInstr.INS_OP_GREATER:
			r1 = this.fetchItem(tb, ains.opr0);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr1);
			r2.locked = true;
			r3 = this.fetchItem(tb, ains.opr2);
			r3.locked = true;
			r4 = this.allocReg(tb);
			r4.locked = true;
			x86Instr.newInsMov(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsMov(tb, new x86Access(r4), new x86Access(1));
			x86Instr.newInsCmp(tb, new x86Access(r2), new x86Access(r3));
			x86Instr.newInsCmovg(tb, new x86Access(r1), new x86Access(r4));
			r1.locked = false;
			r2.locked = false;
			r3.locked = false;
			r4.locked = false;
			r1.synced = false;
			break;
		case AbsInstr.INS_OP_GREATEREQ:
			r1 = this.fetchItem(tb, ains.opr0);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr1);
			r2.locked = true;
			r3 = this.fetchItem(tb, ains.opr2);
			r3.locked = true;
			r4 = this.allocReg(tb);
			r4.locked = true;
			x86Instr.newInsMov(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsMov(tb, new x86Access(r4), new x86Access(1));
			x86Instr.newInsCmp(tb, new x86Access(r2), new x86Access(r3));
			x86Instr.newInsCmovge(tb, new x86Access(r1), new x86Access(r4));
			r1.locked = false;
			r2.locked = false;
			r3.locked = false;
			r4.locked = false;
			r1.synced = false;
			break;
		case AbsInstr.INS_OP_NEQ:
			if (getItemType(ains.opr1)== TypeDesc.PRIMITIVE_STRING)
			{
				this.spillAll(tb);
				regEAX.locked = true;
				r2 = this.fetchItem(tb, ains.opr1);
				r2.locked = true;
				r3 = this.fetchItem(tb, ains.opr2);
				x86Instr.newInsPush(tb, new x86Access(r3));
				x86Instr.newInsPush(tb, new x86Access(r2));
				x86Instr.newInsCall(tb, new x86Access("tg_strneq"));
				x86Instr.newInsAdd(tb, new x86Access(regESP), new x86Access(8));
				r2.locked = false;
				regEAX.locked = false;
				regEAX.bind(ains.opr0);
				regEAX.synced = false;
				r2.free();
				r3.free();
			}
			else
			{
				r1 = this.fetchItem(tb, ains.opr0);
				r1.locked = true;
				r2 = this.fetchItem(tb, ains.opr1);
				r2.locked = true;
				r3 = this.fetchItem(tb, ains.opr2);
				r3.locked = true;
				r4 = this.allocReg(tb);
				r4.locked = true;
				x86Instr.newInsMov(tb, new x86Access(r1), new x86Access(0));
				x86Instr.newInsMov(tb, new x86Access(r4), new x86Access(1));
				x86Instr.newInsCmp(tb, new x86Access(r2), new x86Access(r3));
				x86Instr.newInsCmovne(tb, new x86Access(r1), new x86Access(r4));
				r1.locked = false;
				r2.locked = false;
				r3.locked = false;
				r4.locked = false;
				r1.synced = false;
			}
			break;
		case AbsInstr.INS_OP_LESS:
			r1 = this.fetchItem(tb, ains.opr0);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr1);
			r2.locked = true;
			r3 = this.fetchItem(tb, ains.opr2);
			r3.locked = true;
			r4 = this.allocReg(tb);
			r4.locked = true;
			x86Instr.newInsMov(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsMov(tb, new x86Access(r4), new x86Access(1));
			x86Instr.newInsCmp(tb, new x86Access(r2), new x86Access(r3));
			x86Instr.newInsCmovl(tb, new x86Access(r1), new x86Access(r4));
			r1.locked = false;
			r2.locked = false;
			r3.locked = false;
			r4.locked = false;
			r1.synced = false;
			break;
		case AbsInstr.INS_OP_LESSEQ:
			r1 = this.fetchItem(tb, ains.opr0);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr1);
			r2.locked = true;
			r3 = this.fetchItem(tb, ains.opr2);
			r3.locked = true;
			r4 = this.allocReg(tb);
			r4.locked = true;
			x86Instr.newInsMov(tb, new x86Access(r1), new x86Access(0));
			x86Instr.newInsMov(tb, new x86Access(r4), new x86Access(1));
			x86Instr.newInsCmp(tb, new x86Access(r2), new x86Access(r3));
			x86Instr.newInsCmovle(tb, new x86Access(r1), new x86Access(r4));
			r1.locked = false;
			r2.locked = false;
			r3.locked = false;
			r4.locked = false;
			r1.synced = false;
			break;
		case AbsInstr.INS_OP_EQ:
			if (getItemType(ains.opr1)== TypeDesc.PRIMITIVE_STRING)
			{
				this.spillAll(tb);
				regEAX.locked = true;
				r2 = this.fetchItem(tb, ains.opr1);
				r2.locked = true;
				r3 = this.fetchItem(tb, ains.opr2);
				x86Instr.newInsPush(tb, new x86Access(r3));
				x86Instr.newInsPush(tb, new x86Access(r2));
				x86Instr.newInsCall(tb, new x86Access("tg_streq"));
				x86Instr.newInsAdd(tb, new x86Access(regESP), new x86Access(8));
				r2.locked = false;
				regEAX.locked = false;
				regEAX.bind(ains.opr0);
				regEAX.synced = false;
				r2.free();
				r3.free();
			}
			else
			{
				r1 = this.fetchItem(tb, ains.opr0);
				r1.locked = true;
				r2 = this.fetchItem(tb, ains.opr1);
				r2.locked = true;
				r3 = this.fetchItem(tb, ains.opr2);
				r3.locked = true;
				r4 = this.allocReg(tb);
				r4.locked = true;
				x86Instr.newInsMov(tb, new x86Access(r1), new x86Access(0));
				x86Instr.newInsMov(tb, new x86Access(r4), new x86Access(1));
				x86Instr.newInsCmp(tb, new x86Access(r2), new x86Access(r3));
				x86Instr.newInsCmove(tb, new x86Access(r1), new x86Access(r4));				
				r1.locked = false;
				r2.locked = false;
				r3.locked = false;
				r4.locked = false;
				r1.synced = false;
			}
			break;

		case AbsInstr.INS_OP_SETFIELD:
		case AbsInstr.INS_OP_SUBSCRIPT:
			this.spillReg(tb, regEBX);
			regEBX.locked = true;
			r1 = this.fetchItem(tb, ains.opr0);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr1);
			r2.locked = true;
			r3 = this.fetchItem(tb, ains.opr2);
			r3.locked = true;
			x86Instr.newInsMov(tb, new x86Access(regEBX), new x86Access(r2));
			x86Instr.newInsShl(tb, new x86Access(regEBX));
			x86Instr.newInsShl(tb, new x86Access(regEBX));
			x86Instr.newInsAdd(tb, new x86Access(regEBX), new x86Access(r1));
			x86Instr.newInsMov(tb, new x86Access("[]", regEBX), new x86Access(
					r3));
			regEBX.locked = false;
			r1.locked = false;
			r2.locked = false;
			r3.locked = false;
			regEBX.free();
			break;
		case AbsInstr.INS_OP_INDEX:
		case AbsInstr.INS_OP_GETFIELD:
			this.spillReg(tb, regEBX);
			regEBX.locked = true;
			r1 = this.fetchItem(tb, ains.opr0);
			r1.locked = true;
			r2 = this.fetchItem(tb, ains.opr1);
			r2.locked = true;
			r3 = this.fetchItem(tb, ains.opr2);
			r3.locked = true;
			x86Instr.newInsMov(tb, new x86Access(regEBX), new x86Access(r3));
			x86Instr.newInsShl(tb, new x86Access(regEBX));
			x86Instr.newInsShl(tb, new x86Access(regEBX));
			x86Instr.newInsAdd(tb, new x86Access(regEBX), new x86Access(r2));
			x86Instr.newInsMov(tb, new x86Access(r1), new x86Access("[]",
					regEBX));
			regEBX.locked = false;
			r1.locked = false;
			r2.locked = false;
			r3.locked = false;
			r1.synced = false;
			regEBX.free();
			break;
		case AbsInstr.INS_OP_ASSIGN:
			r1 = this.fetchItem(tb, ains.opr1);
			this.spillReg(tb, r1);
			r1.bind(ains.opr0);
			r1.synced = false;
			break;
		case AbsInstr.INS_OP_PUSH:
			r1 = this.fetchItem(tb, ains.opr0);
			x86Instr.newInsPush(tb, new x86Access(r1));
			break;
		case AbsInstr.INS_OP_CALL:
			String label = ains.opr1.toString();
			int paraSize = ((Integer) ((Constant) ains.opr2).data).intValue() * 4;
			if (label.equals("print"))
				label = "tg_print";
			else if (label.equals("ord"))
				label = "tg_ord";
			else if (label.equals("getchar"))
				label = "tg_getchar";
			else if (label.equals("chr"))
				label = "tg_chr";
			else if (label.equals("printi"))
				label = "tg_printi";
			this.spillAll(tb);
			x86Instr.newInsCall(tb, new x86Access(label));
			if (paraSize != 0)
				x86Instr.newInsAdd(tb, new x86Access(regESP), new x86Access(
					paraSize));
			if (ains.opr0 != null)
			{
				regEAX.bind(ains.opr0);
				regEAX.synced = false;
			}
			break;
		}
	}

	private TypeDesc getItemType(ScopeItem u) {
		if (u instanceof Constant)
			return (((Constant) u).data instanceof Integer ? TypeDesc.PRIMITIVE_INTEGER : TypeDesc.PRIMITIVE_STRING);
		else if (u instanceof Variable)
			return ((Variable) u).ty;
		else if (u instanceof Temporary)
			return ((Temporary) u).ty;
		else
			return null;
	}

	@Override
	protected TargetBlock parseBlock(BasicBlock b) {
		TargetBlock tb = this.curFrame.newBlock(b);
		AbsInstr ains;
		for (int i = 0; i < b.irIns.size(); ++i)
		{
			ains = b.irIns.get(i);
			x86Instr.newInsTODO(tb, ains.toString());
			this.btran(tb, ains);
		}
		this.clearAllocState();
		return tb;
	}

	private void clearAllocState() {
		for (int i = 0 ; i < this.allocState.length; ++i)
			this.allocState[i].free();
	}

	@Override
	protected TargetFrame parseFunction(FuncScope u) {
		TargetFrame saved = this.curFrame;
		TargetFrame retVal = this.generate(this.curFrame.newFunction(u));
		this.curFrame = saved;
		return retVal;
	}

	@Override
	protected void parsePlacement() {
		// place all local variables on the stack frame
		// simplify the escape design
		for (int i = 0; i < this.curFrame.absScope.locals.size(); ++i)
			this.curFrame.addLocal(this.curFrame.absScope.locals.get(i));
	}

	@Override
	protected TargetFrame parseSubscope(Scope scope) {
		TargetFrame saved = this.curFrame;
		TargetFrame retVal = this.generate(this.curFrame.newSubScope(scope));
		this.curFrame = saved;
		return retVal;
	}

	private void spillAll(TargetBlock tb) {
		for (int i = 0; i < this.allocState.length; ++i)
			this.spillReg(tb, this.allocState[i]);
	}

	@Override
	protected Register spillReg(TargetBlock tb, Register reg) {
		if (reg.isAvailable())
			return reg;
		ErrorMsg.instance.errChk(!reg.locked, "preserving locked register!");
		if (reg.data instanceof Constant || reg.synced) {
			reg.free();
			return reg;
		}
		x86TargetScope s = (x86TargetScope) this.curFrame;
		x86Access myAccess = new x86Access(reg);
		x86Access ref = (x86Access) s.refItem(reg.data);
		if (reg.data.ctx == this.curFrame.absScope) {
			if (ref == null) // not persistent yet
				ref = (x86Access) this.curFrame.addLocal(reg.data);
			x86Instr.newInsMov(tb, ref, myAccess);
		} else {
			while (ref == null) {
				s = (x86TargetScope) s.parent;
				ErrorMsg.instance.errChk(s != null, "where is the item?");
				ref = (x86Access) s.refItem(reg.data);
			}
			x86Instr.newInsPush(tb, new x86Access(regEBP));
			x86Instr.newInsMov(tb, new x86Access(regEBP), new x86Access("__"+s.toString()+"_fp"));
			x86Instr.newInsMov(tb, ref, myAccess);
			x86Instr.newInsPop(tb, new x86Access(regEBP));
		}
		reg.free();
		return reg;
	}

}
