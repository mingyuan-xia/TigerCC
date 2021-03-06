/* This file was generated by SableCC (http://www.sablecc.org/). */

package Syntax.analysis;

import Syntax.node.*;

public interface Analysis extends Switch
{
    Object getIn(Node node);
    void setIn(Node node, Object o);
    Object getOut(Node node);
    void setOut(Node node, Object o);

    void caseStart(Start node);
    void caseAStrExpr(AStrExpr node);
    void caseAIntExpr(AIntExpr node);
    void caseANilExpr(ANilExpr node);
    void caseALvalueExpr(ALvalueExpr node);
    void caseANegitiveExpr(ANegitiveExpr node);
    void caseAPlusExpr(APlusExpr node);
    void caseAMinusExpr(AMinusExpr node);
    void caseAMultiplyExpr(AMultiplyExpr node);
    void caseADivideExpr(ADivideExpr node);
    void caseAEqualsExpr(AEqualsExpr node);
    void caseANotEqualsExpr(ANotEqualsExpr node);
    void caseALessExpr(ALessExpr node);
    void caseALessOrEqualExpr(ALessOrEqualExpr node);
    void caseAGreaterExpr(AGreaterExpr node);
    void caseAGreaterOrEqualExpr(AGreaterOrEqualExpr node);
    void caseAAndExpr(AAndExpr node);
    void caseAOrExpr(AOrExpr node);
    void caseAAssignExpr(AAssignExpr node);
    void caseAMethodExpr(AMethodExpr node);
    void caseASeqExpr(ASeqExpr node);
    void caseARecordExpr(ARecordExpr node);
    void caseAArrayExpr(AArrayExpr node);
    void caseAIfExpr(AIfExpr node);
    void caseAWhileExpr(AWhileExpr node);
    void caseAForExpr(AForExpr node);
    void caseABreakExpr(ABreakExpr node);
    void caseALetExpr(ALetExpr node);
    void caseASingleLvalue(ASingleLvalue node);
    void caseAListLvalue(AListLvalue node);
    void caseAArrayLvalue(AArrayLvalue node);
    void caseAField(AField node);
    void caseATypeDeclaration(ATypeDeclaration node);
    void caseAVariableDeclaration(AVariableDeclaration node);
    void caseAMethodDeclaration(AMethodDeclaration node);
    void caseASingleTypeCombination(ASingleTypeCombination node);
    void caseAArrayTypeCombination(AArrayTypeCombination node);
    void caseARecordTypeCombination(ARecordTypeCombination node);
    void caseATypeField(ATypeField node);

    void caseTArray(TArray node);
    void caseTBreak(TBreak node);
    void caseTDo(TDo node);
    void caseTElse(TElse node);
    void caseTEnd(TEnd node);
    void caseTFor(TFor node);
    void caseTFunction(TFunction node);
    void caseTIf(TIf node);
    void caseTIn(TIn node);
    void caseTLet(TLet node);
    void caseTNil(TNil node);
    void caseTOf(TOf node);
    void caseTThen(TThen node);
    void caseTTo(TTo node);
    void caseTType(TType node);
    void caseTVar(TVar node);
    void caseTWhile(TWhile node);
    void caseTComma(TComma node);
    void caseTColon(TColon node);
    void caseTSemicolon(TSemicolon node);
    void caseTLParen(TLParen node);
    void caseTRParen(TRParen node);
    void caseTLBrack(TLBrack node);
    void caseTRBrack(TRBrack node);
    void caseTLCurly(TLCurly node);
    void caseTRCurly(TRCurly node);
    void caseTDot(TDot node);
    void caseTPlus(TPlus node);
    void caseTMinus(TMinus node);
    void caseTMultiply(TMultiply node);
    void caseTDivide(TDivide node);
    void caseTEquals(TEquals node);
    void caseTNotEquals(TNotEquals node);
    void caseTLess(TLess node);
    void caseTLessOrEqual(TLessOrEqual node);
    void caseTGreater(TGreater node);
    void caseTGreaterOrEqual(TGreaterOrEqual node);
    void caseTAnd(TAnd node);
    void caseTOr(TOr node);
    void caseTAssign(TAssign node);
    void caseTId(TId node);
    void caseTWhitespace(TWhitespace node);
    void caseTInt(TInt node);
    void caseTComment(TComment node);
    void caseTCommentBody(TCommentBody node);
    void caseTCommentEnd(TCommentEnd node);
    void caseTStr(TStr node);
    void caseTEscapeChar(TEscapeChar node);
    void caseTControlChar(TControlChar node);
    void caseTGraphChar(TGraphChar node);
    void caseTSimpleChar(TSimpleChar node);
    void caseTLineContinuer(TLineContinuer node);
    void caseTStrEnd(TStrEnd node);
    void caseEOF(EOF node);
}
