package IR;

import java.util.ArrayList;

import util.*;
import AbsSytree.*;
import Parser.sym;

public class TypeChecking extends SynNodeVisitor {
	private SymbolTable<TypeDesc> varList = new SymbolTable<TypeDesc>();
	private SymbolTable<TypeDesc> typeList = new SymbolTable<TypeDesc>();
	private SymbolTable<FuncDesc> funcList = new SymbolTable<FuncDesc>();

	public TypeChecking() {
		addType("string", TypeDesc.PRIMITIVE_STRING);
		addType("int", TypeDesc.PRIMITIVE_INTEGER);
		addType("nil", TypeDesc.PRIMITIVE_NIL);
		addType("void", TypeDesc.PRIMITIVE_VOID);

		addFunc("getchar", FuncDesc.std_getchar);
		addFunc("print", FuncDesc.std_print);
		addFunc("ord", FuncDesc.std_ord);
		addFunc("chr", FuncDesc.std_chr);
		addFunc("printi", FuncDesc.std_printi);
	}

	private TypeDesc findVar(String name) {
		return this.varList.find(name);
	}

	private TypeDesc findType(String name) {
		return this.typeList.find(name);
	}

	private FuncDesc findFunc(String name) {
		return this.funcList.find(name);
	}

	private void exitScope() {
		this.varList.exitScope();
		this.funcList.exitScope();
		this.typeList.exitScope();
	}

	private void enterScope() {
		this.varList.enterScope();
		this.funcList.enterScope();
		this.typeList.enterScope();
	}

	private FuncDesc addFunc(String name, FuncDesc desc) {
		return funcList.add(name, desc);
	}

	private TypeDesc addVar(String name, TypeDesc desc) {
		return varList.add(name, desc);
	}

	private TypeDesc addType(String name, TypeDesc desc) {
		return typeList.add(name, desc);
	}

	private TypeDesc[] parseParaType(SynTypeFieldList para) {
		if (para == null)
			return new TypeDesc[] {};
		TypeDesc[] retVal = new TypeDesc[para.tyfields.size()];
		for (int i = 0; i < para.tyfields.size(); ++i) {
			retVal[i] = typeList.find(para.tyfields.get(i).tyid);
			ErrorMsg.instance
					.errChk(retVal[i] != null,
							"encounter unknown ty-id when parsing function parameters!");
		}
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

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Object visit(AbsSynNode node) {
		if (node.exprType != null)
			return null;
		return node.visit(this);
	}

	@Override
	public Object visit(SynString node) {
		if (node.exprType != null)
			return null;
		node.exprType = TypeDesc.PRIMITIVE_STRING;
		return null;
	}

	@Override
	public Object visit(SynInteger node) {
		if (node.exprType != null)
			return null;
		node.exprType = TypeDesc.PRIMITIVE_INTEGER;
		return null;
	}

	@Override
	public Object visit(SynNil node) {
		if (node.exprType != null)
			return null;
		node.exprType = TypeDesc.PRIMITIVE_NIL;
		return null;
	}

	@Override
	public Object visit(SynVarRef node) {
		if (node.exprType != null)
			return null;
		TypeDesc ty = this.findVar(node.varName);
		ErrorMsg.instance.errChk(ty != null, "var reference not found");
		node.exprType = ty;
		return null;
	}

	@Override
	public Object visit(SynBreak node) {
		if (node.exprType != null)
			return null;
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynSubExpr node) {
		if (node.exprType != null)
			return null;
		node.expr.visit(this);
		ErrorMsg.instance.errChk(node.expr.exprType
				.equals(TypeDesc.PRIMITIVE_INTEGER),
				"negative expr must derived from integer");
		node.exprType = node.expr.exprType;
		return null;
	}

	@Override
	public Object visit(SynBinaryOp node) {
		if (node.exprType != null)
			return null;
		node.el.visit(this);
		node.er.visit(this);
		ErrorMsg.instance.errChk(node.el.exprType.equals(node.er.exprType),
				"binary operation type mismatch!");
		switch (node.op) {
		case sym.ADD:
		case sym.SUB:
		case sym.MUL:
		case sym.DIV:
		case sym.OR:
		case sym.AND:
			node.exprType = node.el.exprType;
			break;
		default:
			node.exprType = TypeDesc.PRIMITIVE_INTEGER;
			break;
		}
		return null;
	}

	@Override
	public Object visit(SynAssign node) {
		if (node.exprType != null)
			return null;
		node.lv.visit(this);
		node.expr.visit(this);
		ErrorMsg.instance.errChk(node.lv.exprType.equals(node.expr.exprType),
				"assign type mismatch!");
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynAgg node) {
		if (node.exprType != null)
			return null;
		this.enterScope();
		node.dlist.visit(this);
		if (node.eseq != null) {
			node.eseq.visit(this);
			node.exprType = node.eseq.exprType;
		} else {
			node.exprType = TypeDesc.PRIMITIVE_VOID;
		}
		this.exitScope();
		return null;
	}

	@Override
	public Object visit(SynArray node) {
		if (node.exprType != null)
			return null;
		TypeDesc ty = this.findType(node.aname);
		ErrorMsg.instance.errChk(ty != null, "array type not found");
		node.exprType = ty;
		ErrorMsg.instance.errChk(node.exprType.isArray(),
				"array cast mismatch!");
		node.indexExpr.visit(this);
		node.valExpr.visit(this);
		ErrorMsg.instance.errChk(node.indexExpr.exprType
				.equals(TypeDesc.PRIMITIVE_INTEGER),
				"index of array should be AbsSynNodeeger!");
		ErrorMsg.instance.errChk(node.valExpr.exprType.equals(node.exprType
				.getArrayeeType()), "array type mismatch with arrayee type!");
		return null;
	}

	@Override
	public Object visit(SynIf node) {
		if (node.exprType != null)
			return null;
		node.cond.visit(this);
		ErrorMsg.instance.errChk(node.cond.exprType
				.equals(TypeDesc.PRIMITIVE_INTEGER),
				"if conodeition should be AbsSynNode type!");
		node.ife.visit(this);
		if (node.elsee == null) {
			ErrorMsg.instance.errChk(node.ife.exprType
					.equals(TypeDesc.PRIMITIVE_VOID),
					"if-then should takes void body!");
			node.exprType = TypeDesc.PRIMITIVE_VOID;
		} else {
			node.elsee.visit(this);
			ErrorMsg.instance.errChk(node.ife.exprType
					.equals(node.elsee.exprType),
					"if two bracnch type mismatch!");
			if (node.ife.exprType.isNil())
				node.exprType = node.elsee.exprType;
			else
				node.exprType = node.ife.exprType;
		}
		return null;
	}

	@Override
	public Object visit(SynWhile node) {
		if (node.exprType != null)
			return null;
		node.cond.visit(this);
		ErrorMsg.instance.errChk(node.cond.exprType
				.equals(TypeDesc.PRIMITIVE_INTEGER),
				"while conodeition should be AbsSynNode type!");
		node.e.visit(this);
		ErrorMsg.instance.errChk(node.e.exprType
				.equals(TypeDesc.PRIMITIVE_VOID),
				"while should takes void body!");
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynFor node) {
		if (node.exprType != null)
			return null;
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		this.enterScope();
		this.addVar(node.iname, TypeDesc.PRIMITIVE_INTEGER);
		node.efrom.visit(this);
		ErrorMsg.instance.errChk(node.efrom.exprType
				.equals(TypeDesc.PRIMITIVE_INTEGER),
				"for from expr should be AbsSynNode!");
		node.eto.visit(this);
		ErrorMsg.instance.errChk(node.eto.exprType
				.equals(TypeDesc.PRIMITIVE_INTEGER),
				"for to expr should be AbsSynNode!");
		node.e.visit(this);
		this.exitScope();
		return null;
	}

	@Override
	public Object visit(SynDeclList node) {
		if (node.exprType != null)
			return null;
		for (int i = 0; i < node.decls.size(); ++i)
			node.decls.get(i).visit(this);
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynExprList node) {
		if (node.exprType != null)
			return null;
		for (int i = 0; i < node.exprs.size(); ++i)
			node.exprs.get(i).visit(this);
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynExprSeq node) {
		if (node.exprType != null)
			return null;
		for (int i = 0; i < node.exprs.size(); ++i)
			node.exprs.get(i).visit(this);
		node.exprType = node.exprs.get(node.exprs.size() - 1).exprType;
		return null;
	}

	@Override
	public Object visit(SynTypeFieldList node) {
		if (node.exprType != null)
			return null;
		for (int i = node.tyfields.size() - 1; i >= 0; --i)
			node.tyfields.get(i).visit(this);
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynTypeField node) {
		if (node.exprType != null)
			return null;
		TypeDesc ty = this.findType(node.tyid);
		ErrorMsg.instance.errChk(ty != null, "tyid not found");
		node.exprType = ty;
		return null;
	}

	@Override
	public Object visit(SynVarDecl node) {
		if (node.exprType != null)
			return null;
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		TypeDesc ty = null;
		if (node.tyid == null) {
			ErrorMsg.instance.errChk(node.e != null,
					"var decl without expression and tyid");
			node.e.visit(this);
			ty = node.e.exprType;
		} else {
			ty = this.findType(node.tyid);
			ErrorMsg.instance.errChk(ty != null, "var typeid not found");
			if (node.e != null)
				node.e.visit(this);
			ErrorMsg.instance.errChk(node.e.exprType.equals(this
					.findType(node.tyid)),
					"var definitation type mismatch with expr type!");
		}
		ErrorMsg.instance.errChk(!ty.equals(TypeDesc.PRIMITIVE_VOID),
		"variable cannot be declared void");			
		ErrorMsg.instance.errChk(!ty.isNil(),
				"variable cannot be declared nil");
		this.addVar(node.vname, ty);
		return null;
	}

	@Override
	public Object visit(SynRecord node) {
		if (node.exprType != null)
			return null;
		if (node.flist != null)
			node.flist.visit(this);
		TypeDesc ty = this.findType(node.rname);
		ErrorMsg.instance.errChk(ty != null, "record type name not found!");
		node.exprType = ty;
		return null;
	}

	@Override
	public Object visit(SynFieldList node) {
		if (node.exprType != null)
			return null;
		for (int i = 0; i < node.exprs.size(); ++i)
			node.exprs.get(i).visit(this);
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynCall node) {
		if (node.exprType != null)
			return null;
		FuncDesc fd = this.funcList.find(node.procName);
		ErrorMsg.instance.errChk(fd != null, "proc name not found!");
		node.exprType = fd.retType;
		if (node.elst != null) {
			node.elst.visit(this);
			ErrorMsg.instance.errChk(fd.paraTypes.length == node.elst.exprs
					.size(), "function call arguments length mismatch!");
			for (int i = 0; i < fd.paraTypes.length; ++i)
				ErrorMsg.instance.errChk(fd.paraTypes[i].equals(node.elst.exprs
						.get(i).exprType),
						"function call arguments type mismatch!");
		} else {
			ErrorMsg.instance.errChk(fd.paraTypes.length == 0,
					"function call arguments length mismatch!");
		}
		return null;
	}

	@Override
	public Object visit(SynFuncDecl node) {
		if (node.exprType != null)
			return null;
		FuncDesc fdesc = this.findFunc(node.name);
		ErrorMsg.instance.errChk(fdesc != null, "func decl not function well!");
		this.enterScope();
		for (int i = 0; i < fdesc.paraNames.length; ++i)
			this.addVar(fdesc.paraNames[i], fdesc.paraTypes[i]);
		node.e.visit(this);
		this.exitScope();
		ErrorMsg.instance.errChk(node.e.exprType.equals(fdesc.retType),
				"function return type mismatch!");
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynTypeID node) {
		if (node.exprType != null)
			return null;
		TypeDesc ty = this.findType(node.typeName);
		ErrorMsg.instance.errChk(ty != null, "tyid not found");
		node.exprType = ty;
		return null;
	}

	@Override
	public Object visit(SynArrayType node) {
		if (node.exprType != null)
			return null;
		TypeDesc arrayeeType = this.findType(node.arrayeeType.typeName);
		ErrorMsg.instance.errChk(arrayeeType != null, "arrayeeType not found");
		node.exprType = new TypeDesc(arrayeeType, 0);
		return null;
	}

	@Override
	public Object visit(SynRecordType node) {
		if (node.exprType != null)
			return null;
		ErrorMsg.instance.errChk(node.tyfl != null, "null record type!");
		node.tyfl.visit(this);
		node.exprType = new TypeDesc(node.tyfl); // TODO : more strict check!
		return null;
	}

	@Override
	public Object visit(SynTypeDecl node) {
		if (node.exprType != null)
			return null;
		TypeDesc newTy = this.findType(node.name);
		ErrorMsg.instance.errChk(newTy != null, "TypeDecl not function right!");
		node.ty.visit(this);
		if (!newTy.setAsAlias(node.ty.exprType))
			ErrorMsg.instance.err("detect loop recursive type alias!");
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynArrayRef node) {
		if (node.exprType != null)
			return null;
		node.lv.visit(this);
		ErrorMsg.instance.errChk(node.lv.exprType.isArray(),
				"array cast mismatch!");
		node.exprType = node.lv.exprType.getArrayeeType();
		node.e.visit(this);
		ErrorMsg.instance.errChk(node.e.exprType
				.equals(TypeDesc.PRIMITIVE_INTEGER),
				"array ref expr should be AbsSynNode!");
		return null;
	}

	@Override
	public Object visit(SynRecordRef node) {
		if (node.exprType != null)
			return null;
		node.lv.visit(this);
		node.exprType = (node.lv.exprType.getRecordField(node.id));
		ErrorMsg.instance.errChk(node.exprType != null, "no fields in record!");
		return null;
	}

	@Override
	public Object visit(SynVoid node) {
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynTypeDeclList node) {
		if (node.exprType != null)
			return null;
		// support recursive declaration declaration
		ArrayList<String> batchNames = new ArrayList<String>();
		for (int i = 0; i < node.decls.size(); ++i)
		{
			SynTypeDecl tdecl = node.decls.get(i);
			ErrorMsg.instance.errChk(!batchNames.contains(tdecl.name), "mutli declaration in batch type decl list!");
			batchNames.add(tdecl.name);
			this.addType(tdecl.name, new TypeDesc());
		}
		for (int i = 0; i < node.decls.size(); ++i)
			node.decls.get(i).visit(this);
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

	@Override
	public Object visit(SynFuncDeclList node) {
		if (node.exprType != null)
			return null;
		// support recursive declaration declaration
		SynFuncDecl fdecl = null;
		TypeDesc retTy = null;
		ArrayList<String> batchNames = new ArrayList<String>();
		for (int i = 0; i < node.decls.size(); ++i) {
			fdecl = node.decls.get(i);
			if (fdecl.para != null)
				fdecl.para.visit(this);
			retTy = (fdecl.tyid == null ? TypeDesc.PRIMITIVE_VOID : this
					.findType(fdecl.tyid));
			ErrorMsg.instance.errChk(retTy != null,
					"function return type-id not found");
			ErrorMsg.instance.errChk(!batchNames.contains(fdecl.name), "mutli declaration in batch func decl list!");
			batchNames.add(fdecl.name);
			this
					.addFunc(fdecl.name, new FuncDesc(retTy, this
							.parseParaName(fdecl.para), this
							.parseParaType(fdecl.para)));
		}
		for (int i = 0; i < node.decls.size(); ++i)
			node.decls.get(i).visit(this);
		node.exprType = TypeDesc.PRIMITIVE_VOID;
		return null;
	}

}
