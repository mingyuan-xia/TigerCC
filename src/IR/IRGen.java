package IR;

import java.util.Stack;

import AbsSytree.*;
import Parser.sym;
import util.*;

public class IRGen extends SynNodeVisitor {
	private class PseudoScope
	{
		public String loopVar, alias;
		BasicBlock exit;
		PseudoScope(BasicBlock exit)
		{
			this.loopVar = null;
			this.alias = null;
			this.exit = exit;
		}
		PseudoScope(String loopVar, String alias, BasicBlock exit)
		{
			this.loopVar = loopVar;
			this.alias = alias;
			this.exit = exit;
		}
	}
	private Stack<PseudoScope> loopScope;
	private BasicBlock curBlock;
	private Scope curScope;

	public IRGen() {
	}

	public Scope startGen(AbsSynNode root) {
		// init for special global scope
		this.loopScope = new Stack<PseudoScope>();
		Scope global = new Scope(null);
		this.curScope = global;
		this.curBlock = this.curScope.setEntry(this.curScope.addBlock());
		this.pushIns(AbsInstr.INS_MISC_FRAME_ENTER, null, null, null);

		this.addType("string", TypeDesc.PRIMITIVE_STRING);
		this.addType("int", TypeDesc.PRIMITIVE_INTEGER);
		this.addType("nil", TypeDesc.PRIMITIVE_NIL);
		this.addType("void", TypeDesc.PRIMITIVE_VOID);

		// set basic block and Scope for std functions
		global.addFunc("getchar", FuncDesc.std_getchar);
		global.addFunc("print", FuncDesc.std_print);
		global.addFunc("ord", FuncDesc.std_ord);
		global.addFunc("chr", FuncDesc.std_chr);
		global.addFunc("printi", FuncDesc.std_printi);
		
		this.visit(root);
		global.setExit(this.curBlock);
		this.pushIns(global.exit, AbsInstr.INS_MISC_FRAME_LEVAVE, null,
				null, null);
		return global;
	}

	private TypeDesc findType(String name) {
		return this.curScope.findType(name);
	}

	private TypeDesc addType(String name, TypeDesc desc) {
		return this.curScope.addType(name, desc);
	}

	private AbsInstr pushIns(int op, ScopeItem dst, ScopeItem opr1,
			ScopeItem opr2) {
		return pushIns(this.curBlock, op, dst, opr1, opr2);
	}

	private AbsInstr pushIns(BasicBlock b, int op, ScopeItem dst,
			ScopeItem opr1, ScopeItem opr2) {
		AbsInstr retVal = new AbsInstr(op, dst, opr1, opr2);
		b.irIns.add(retVal);
		return retVal;
	}

	private TypeDesc[] parseParaType(SynTypeFieldList para) {
		if (para == null)
			return new TypeDesc[] {};
		TypeDesc[] retVal = new TypeDesc[para.tyfields.size()];
		for (int i = 0; i < para.tyfields.size(); ++i)
			retVal[i] = findType(para.tyfields.get(i).tyid);
		return retVal;
	}

	private String[] parseParaName(SynTypeFieldList para) {
		if (para == null)
			return new String[] {};
		String[] retVal = new String[para.tyfields.size()];
		for (int i = 0; i < para.tyfields.size(); ++i)
			retVal[i] = para.tyfields.get(i).name;
		return retVal;
	}
	
	/**************************** Visitor *********************************************/

	@Override
	public Object visit(AbsSynNode node) {
		return node.visit(this);
	}

	@Override
	public Object visit(SynAgg node) {
		Scope parentf = this.curScope;
		Scope subf = this.curScope.addSubScope();
		Temporary retVal = null;
		subf.setEntry(subf.addBlock());
		// init new Scope
		pushIns(subf.entry, AbsInstr.INS_MISC_FRAME_ENTER, null, null, null);
		// still in the parent Scope, end parent block
		if (node.exprType.equals(TypeDesc.PRIMITIVE_VOID))
			pushIns(AbsInstr.INS_OP_CALL, retVal = null, subf, ScopeItem.makeConst(0));
		else
			pushIns(AbsInstr.INS_OP_CALL, retVal = this.curScope.addTemp(node.exprType), subf, ScopeItem.makeConst(0));
		BasicBlock parentb = this.curBlock;
		// enter new Scope and block
		this.curScope = subf;
		this.curBlock = subf.entry;
		
		node.dlist.visit(this);
		ScopeItem r = null;
		if (node.eseq != null)
			r = (ScopeItem) node.eseq.visit(this);
		
		this.curScope.setExit(this.curBlock);
		// init exit of old Scope
		pushIns(this.curScope.exit, AbsInstr.INS_MISC_FRAME_LEVAVE, r, null,
				null);
		// back to parent Scope
		this.curScope = parentf;
		this.curBlock = parentb;
		return retVal;
	}

	@Override
	public Object visit(SynArray node) {
		Temporary retVal = this.curScope.addTemp(node.exprType);
		this.pushIns(AbsInstr.INS_MISC_NEWARRAY, retVal,
				(ScopeItem) node.indexExpr.visit(this),
				(ScopeItem) node.valExpr.visit(this));
		return retVal;
	}

	@Override
	public Object visit(SynArrayRef node) {
		Temporary retVal = this.curScope.addTemp(node.exprType);
		this.pushIns(AbsInstr.INS_OP_INDEX, retVal, (ScopeItem) node.lv
				.visit(this), (ScopeItem) node.e.visit(this));
		return retVal;
	}

	@Override
	public Object visit(SynAssign node) {
		if (node.lv instanceof SynArrayRef) {
			SynArrayRef nd = (SynArrayRef) node.lv;
			ScopeItem base = (ScopeItem) nd.lv.visit(this);
			ScopeItem idx = (ScopeItem) nd.e.visit(this);
			ScopeItem val = (ScopeItem) node.expr.visit(this);
			this.pushIns(AbsInstr.INS_OP_SUBSCRIPT, base, idx, val);

		} else if (node.lv instanceof SynRecordRef) {
			SynRecordRef nd = (SynRecordRef) node.lv;
			ScopeItem base = (ScopeItem) nd.lv.visit(this);
			ScopeItem idx = ScopeItem.makeConst(nd.lv.exprType
					.getRecordFieldLoc(nd.id));
			ScopeItem val = (ScopeItem) node.expr.visit(this);
			this.pushIns(AbsInstr.INS_OP_SETFIELD, base, idx, val);
		} else if (node.lv instanceof SynVarRef) {
			SynVarRef nd = (SynVarRef) node.lv;
			ScopeItem var = (ScopeItem) nd.visit(this);
			ScopeItem val = (ScopeItem) node.expr.visit(this);
			this.pushIns(AbsInstr.INS_OP_ASSIGN, var, val, null);
		}
		return null;
	}

	@Override
	public Object visit(SynBinaryOp node) {
		ScopeItem rl = (ScopeItem) node.el.visit(this);
		ScopeItem rr = (ScopeItem) node.er.visit(this);
		int op = 0;
		switch (node.op) {
		case sym.ADD:
			op = AbsInstr.INS_OP_ADD;
			break;
		case sym.SUB:
			op = AbsInstr.INS_OP_SUB;
			break;
		case sym.MUL:
			op = AbsInstr.INS_OP_MUL;
			break;
		case sym.DIV:
			op = AbsInstr.INS_OP_DIV;
			break;
		case sym.OR:
			op = AbsInstr.INS_OP_OR;
			break;
		case sym.AND:
			op = AbsInstr.INS_OP_AND;
			break;
		case sym.GREATER:
			op = AbsInstr.INS_OP_GREATER;
			break;
		case sym.GREATEREQ:
			op = AbsInstr.INS_OP_GREATEREQ;
			break;
		case sym.EQ:
			op = AbsInstr.INS_OP_EQ;
			break;
		case sym.NEQ:
			op = AbsInstr.INS_OP_NEQ;
			break;
		case sym.LESS:
			op = AbsInstr.INS_OP_LESS;
			break;
		case sym.LESSEQ:
			op = AbsInstr.INS_OP_LESSEQ;
			break;
		default:
			ErrorMsg.instance.err("unknown binary operation!");
			break;
		}
		Temporary retVal = this.curScope.addTemp(node.exprType);
		pushIns(op, retVal, rl, rr);
		return retVal;
	}

	@Override
	public Object visit(SynBreak node) {
		ErrorMsg.instance.errChk(!this.loopScope.isEmpty(), "break not allowed here!");
		this.pushIns(AbsInstr.INS_JUMP, this.loopScope.peek().exit, null, null);
		return null;
	}

	@Override
	public Object visit(SynCall node) {
		int i;
		int paraNum = node.elst == null ? 0 : node.elst.exprs.size();
		ScopeItem[] paras = new ScopeItem[paraNum];
		for (i = 0; i < paras.length; ++i)
			paras[i] = (ScopeItem) node.elst.exprs.get(i).visit(this);
		for (i = 0; i < paras.length; ++i)
			this.pushIns(AbsInstr.INS_OP_PUSH, paras[i], null, null);
		FuncScope func = this.curScope.findFunc(node.procName);
		Temporary retVal = this.curScope
				.addTemp(func.funcRetType);
		this.pushIns(AbsInstr.INS_OP_CALL, retVal, func, ScopeItem.makeConst(i));
		return retVal;
	}

	@Override
	public Object visit(SynDeclList node) {
		for (int i = 0; i < node.decls.size(); ++i)
			node.decls.get(i).visit(this);
		return null;
	}

	@Override
	public Object visit(SynExprList node) {
		// syncall will iterate the list and won't call visit
		ErrorMsg.instance.err("should not be here");
		return null;
	}

	@Override
	public Object visit(SynVoid node) {
		return null;
	}

	@Override
	public Object visit(SynExprSeq node) {
		for (int i = 0; i < node.exprs.size() - 1; ++i)
			node.exprs.get(i).visit(this);
		if (node.exprs.isEmpty())
			return null;
		else
			return node.exprs.get(node.exprs.size() - 1).visit(this);
	}

	@Override
	public Object visit(SynFor node) {
		Variable iter = this.curScope.addLocal("__loop_"+node.iname, TypeDesc.PRIMITIVE_INTEGER);
		ScopeItem r, t;
		r = (ScopeItem) node.efrom.visit(this);
		this.pushIns(AbsInstr.INS_OP_ASSIGN, iter, r, null);
		BasicBlock loopBody = this.curScope.addBlock(), loopExit = this.curScope
				.addBlock();
		this.pushIns(AbsInstr.INS_JUMP, loopBody, null, null);
		this.curBlock = loopBody;

		this.loopScope.push(new PseudoScope(node.iname, "__loop_"+node.iname, loopExit));
		node.e.visit(this);
		this.pushIns(AbsInstr.INS_OP_ADD, iter, iter, ScopeItem.makeConst(1));
		this.loopScope.pop();

		r = (ScopeItem) node.eto.visit(this);
		this.pushIns(AbsInstr.INS_OP_GREATER, t = this.curScope
				.addTemp(TypeDesc.PRIMITIVE_INTEGER), iter, r);
		this.pushIns(AbsInstr.INS_JUMP_EQ, t, loopBody, loopExit);
		this.curBlock = loopExit;
		return null;
	}

	@Override
	public Object visit(SynFuncDecl node) {
		BasicBlock parentb = this.curBlock;
		FuncScope func = this.curScope.findFunc(node.name);
		func.setEntry(func.addBlock());
		this.curScope = func;
		this.curBlock = func.entry;
		pushIns(AbsInstr.INS_MISC_FRAME_ENTER, null, null, null);
		pushIns(AbsInstr.INS_MISC_FRAME_LEVAVE, (ScopeItem) node.e.visit(this), null, null);
		this.curScope.setExit(this.curBlock);
		this.curScope = this.curScope.ctx;
		this.curBlock = parentb;
		return null;
	}

	@Override
	public Object visit(SynIf node) {
		// TODO : messy and possible bugs awaiting rewriting!
		ScopeItem rcond = (ScopeItem) node.cond.visit(this);
		BasicBlock trueBranch = this.curScope.addBlock(), falseBranch = this.curScope
				.addBlock(), exitPoint = null;
		ScopeItem exitRet = null;
		this.pushIns(AbsInstr.INS_JUMP_NEQ, rcond, trueBranch,
				falseBranch);
		// parse true branch
		this.curBlock = trueBranch;
		if (node.exprType.equals(TypeDesc.PRIMITIVE_VOID))
			node.ife.visit(this);
		else {
			exitRet = this.curScope.addTemp(node.exprType);
			pushIns(trueBranch, AbsInstr.INS_OP_ASSIGN, exitRet,
					(ScopeItem) node.ife.visit(this), null);
		}
		// determine exit point
		if (node.elsee == null) {
			exitPoint = falseBranch;
		} else {
			exitPoint = this.curScope.addBlock();
			// parse false branch
			this.curBlock = falseBranch;
			if (node.exprType.equals(TypeDesc.PRIMITIVE_VOID))
				node.elsee.visit(this);
			else
				pushIns(AbsInstr.INS_OP_ASSIGN, exitRet, (ScopeItem) node.elsee
						.visit(this), null);
			pushIns(AbsInstr.INS_JUMP, exitPoint, null, null);
		}
		pushIns(trueBranch, AbsInstr.INS_JUMP, exitPoint, null, null);
		this.curBlock = exitPoint;
		return exitRet;
	}

	@Override
	public Object visit(SynInteger node) {
		return ScopeItem.makeConst(node.val);
	}

	@Override
	public Object visit(SynNil node) {
		return ScopeItem.makeConst(0);
	}

	@Override
	public Object visit(SynRecord node) {
		ScopeItem retVal = this.curScope.addTemp(node.exprType);
		ScopeItem[] fields = new ScopeItem[node.flist.names.size()];
		for (int i = 0; i < fields.length; ++i)
			fields[i] = (ScopeItem) node.flist.exprs.get(i).visit(this);
		this.pushIns(AbsInstr.INS_MISC_NEWRECORD, retVal, ScopeItem.makeConst(
				node.exprType.getRecordFieldCount()), null);
		for (int i = 0; i < fields.length; ++i)
			this.pushIns(AbsInstr.INS_OP_SETFIELD, retVal, ScopeItem.makeConst(i),
					fields[i]);
		return retVal;
	}

	@Override
	public Object visit(SynRecordRef node) {
		ScopeItem r = (ScopeItem) node.lv.visit(this);
		int idx = node.lv.exprType.getRecordFieldLoc(node.id);
		Temporary retVal = this.curScope.addTemp(node.exprType);
		this.pushIns(AbsInstr.INS_OP_GETFIELD, retVal, r, ScopeItem.makeConst(idx));
		return retVal;
	}

	@Override
	public Object visit(SynRecordType node) {
		ErrorMsg.instance.err("should not be here");
		return null;
	}

	@Override
	public Object visit(SynString node) {
		return ScopeItem.makeConst(node.val);
	}

	@Override
	public Object visit(SynSubExpr node) {
		Temporary retVal = this.curScope.addTemp(node.exprType);
		pushIns(AbsInstr.INS_OP_NEG, retVal, (ScopeItem) node.expr.visit(this),
				null);
		return retVal;
	}

	@Override
	public Object visit(SynVarDecl node) {
		if (node.e == null)
		{
			this.curScope.addLocal(node.vname, findType(node.tyid));
		}
		else
		{
			ScopeItem newVar = this.curScope.addLocal(node.vname, node.e.exprType);
			ScopeItem val = (ScopeItem) node.e.visit(this);
			this.pushIns(AbsInstr.INS_OP_ASSIGN, newVar, val, null);
		}
		return null;
	}

	@Override
	public Object visit(SynVarRef node) {
		String loopVar;
		for (int i = this.loopScope.size() - 1; i >= 0; --i)
		{
			loopVar = this.loopScope.get(i).loopVar;
			if (loopVar != null && loopVar.equals(node.varName))
				return this.curScope.findVar(this.loopScope.get(i).alias);
		}
		return this.curScope.findVar(node.varName);
	}

	@Override
	public Object visit(SynWhile node) {
		BasicBlock loopCond = this.curScope.addBlock();
		this.pushIns(AbsInstr.INS_JUMP, loopCond, null, null);
		this.curBlock = loopCond;
		ScopeItem cond = (ScopeItem) node.cond.visit(this);
		BasicBlock loopBody = this.curScope.addBlock(), loopExit = this.curScope
				.addBlock();
		this.pushIns(AbsInstr.INS_JUMP_NEQ, cond, loopBody,
				loopExit);
		this.curBlock = loopBody;
		this.loopScope.push(new PseudoScope(loopExit));
		node.e.visit(this);
		this.pushIns(AbsInstr.INS_JUMP, loopCond, null, null);
		this.loopScope.pop();
		this.curBlock = loopExit;
		return null;
	}

	@Override
	public Object visit(SynTypeDeclList node) {
		SynTypeDecl tdecl = null;
		for (int i = 0; i < node.decls.size(); ++i)
		{
			tdecl = node.decls.get(i);
			this.addType(tdecl.name, tdecl.ty.exprType);
		}
		return null;
	}

	@Override
	public Object visit(SynFuncDeclList node) {
		SynFuncDecl fdecl = null;
		TypeDesc retTy = null;
		for (int i = 0; i < node.decls.size(); ++i)
		{
			fdecl = node.decls.get(i);
			retTy = (fdecl.tyid == null ? TypeDesc.PRIMITIVE_VOID : this
					.findType(fdecl.tyid));
			this.curScope.addFunc(fdecl.name, new FuncDesc(retTy, this
					.parseParaName(fdecl.para), this.parseParaType(fdecl.para)));
		}
		for (int i = 0; i < node.decls.size(); ++i)
			node.decls.get(i).visit(this);
		return null;
	}

	@Override
	public Object visit(SynArrayType node) {
		ErrorMsg.instance.err("should not be here");
		return null;
	}

	@Override
	public Object visit(SynTypeDecl node) {
		ErrorMsg.instance.err("should not be here");
		return null;
	}

	@Override
	public Object visit(SynTypeField node) {
		ErrorMsg.instance.err("should not be here");
		return null;
	}

	@Override
	public Object visit(SynTypeFieldList node) {
		ErrorMsg.instance.err("should not be here");
		return null;
	}

	@Override
	public Object visit(SynTypeID node) {
		ErrorMsg.instance.err("should not be here");
		return null;
	}

	@Override
	public Object visit(SynFieldList node) {
		ErrorMsg.instance.err("should not be here");
		return null;
	}

}
