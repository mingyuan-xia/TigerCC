package AbsSytree;

public abstract class SynNodeVisitor {
	public abstract Object visit(AbsSynNode node);

	public abstract Object visit(SynAgg synAgg);

	public abstract Object visit(SynArray synArray);

	public abstract Object visit(SynArrayRef synArrayRef);

	public abstract Object visit(SynArrayType synArrayType);

	public abstract Object visit(SynAssign synAssign);

	public abstract Object visit(SynBinaryOp synBinaryOp);

	public abstract Object visit(SynBreak synBreak);

	public abstract Object visit(SynCall synCall);

	public abstract Object visit(SynDeclList synDeclList);

	public abstract Object visit(SynExprList synExprList);

	public abstract Object visit(SynVoid synVoid);

	public abstract Object visit(SynExprSeq synExprSeq);

	public abstract Object visit(SynFieldList synFieldList);

	public abstract Object visit(SynFor synFor);

	public abstract Object visit(SynFuncDecl synFunc);

	public abstract Object visit(SynIf synIf);

	public abstract Object visit(SynInteger synInteger);

	public abstract Object visit(SynNil synNil);

	public abstract Object visit(SynRecord synRecord);

	public abstract Object visit(SynRecordRef synRecordRef);

	public abstract Object visit(SynRecordType synRecordType);

	public abstract Object visit(SynString synString);

	public abstract Object visit(SynSubExpr synSubExpr);

	public abstract Object visit(SynTypeDecl synTypeDecl);

	public abstract Object visit(SynTypeField synTypeField);

	public abstract Object visit(SynTypeFieldList synTypeFieldList);

	public abstract Object visit(SynTypeID synTypeID);

	public abstract Object visit(SynVarDecl synVar);

	public abstract Object visit(SynVarRef synVarRef);

	public abstract Object visit(SynWhile synWhile);

	public abstract Object visit(SynTypeDeclList node);

	public abstract Object visit(SynFuncDeclList node);
}
