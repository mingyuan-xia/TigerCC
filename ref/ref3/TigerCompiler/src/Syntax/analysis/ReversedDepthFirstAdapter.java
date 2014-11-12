/* This file was generated by SableCC (http://www.sablecc.org/). */

package Syntax.analysis;

import java.util.*;
import Syntax.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter
{
    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getEOF().apply(this);
        node.getPExpr().apply(this);
        outStart(node);
    }

    public void inAStrExpr(AStrExpr node)
    {
        defaultIn(node);
    }

    public void outAStrExpr(AStrExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAStrExpr(AStrExpr node)
    {
        inAStrExpr(node);
        if(node.getStr() != null)
        {
            node.getStr().apply(this);
        }
        outAStrExpr(node);
    }

    public void inAIntExpr(AIntExpr node)
    {
        defaultIn(node);
    }

    public void outAIntExpr(AIntExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntExpr(AIntExpr node)
    {
        inAIntExpr(node);
        if(node.getInt() != null)
        {
            node.getInt().apply(this);
        }
        outAIntExpr(node);
    }

    public void inANilExpr(ANilExpr node)
    {
        defaultIn(node);
    }

    public void outANilExpr(ANilExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANilExpr(ANilExpr node)
    {
        inANilExpr(node);
        outANilExpr(node);
    }

    public void inALvalueExpr(ALvalueExpr node)
    {
        defaultIn(node);
    }

    public void outALvalueExpr(ALvalueExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALvalueExpr(ALvalueExpr node)
    {
        inALvalueExpr(node);
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }
        outALvalueExpr(node);
    }

    public void inANegitiveExpr(ANegitiveExpr node)
    {
        defaultIn(node);
    }

    public void outANegitiveExpr(ANegitiveExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANegitiveExpr(ANegitiveExpr node)
    {
        inANegitiveExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outANegitiveExpr(node);
    }

    public void inAPlusExpr(APlusExpr node)
    {
        defaultIn(node);
    }

    public void outAPlusExpr(APlusExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPlusExpr(APlusExpr node)
    {
        inAPlusExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAPlusExpr(node);
    }

    public void inAMinusExpr(AMinusExpr node)
    {
        defaultIn(node);
    }

    public void outAMinusExpr(AMinusExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMinusExpr(AMinusExpr node)
    {
        inAMinusExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAMinusExpr(node);
    }

    public void inAMultiplyExpr(AMultiplyExpr node)
    {
        defaultIn(node);
    }

    public void outAMultiplyExpr(AMultiplyExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMultiplyExpr(AMultiplyExpr node)
    {
        inAMultiplyExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAMultiplyExpr(node);
    }

    public void inADivideExpr(ADivideExpr node)
    {
        defaultIn(node);
    }

    public void outADivideExpr(ADivideExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADivideExpr(ADivideExpr node)
    {
        inADivideExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outADivideExpr(node);
    }

    public void inAEqualsExpr(AEqualsExpr node)
    {
        defaultIn(node);
    }

    public void outAEqualsExpr(AEqualsExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAEqualsExpr(AEqualsExpr node)
    {
        inAEqualsExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAEqualsExpr(node);
    }

    public void inANotEqualsExpr(ANotEqualsExpr node)
    {
        defaultIn(node);
    }

    public void outANotEqualsExpr(ANotEqualsExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANotEqualsExpr(ANotEqualsExpr node)
    {
        inANotEqualsExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outANotEqualsExpr(node);
    }

    public void inALessExpr(ALessExpr node)
    {
        defaultIn(node);
    }

    public void outALessExpr(ALessExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALessExpr(ALessExpr node)
    {
        inALessExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outALessExpr(node);
    }

    public void inALessOrEqualExpr(ALessOrEqualExpr node)
    {
        defaultIn(node);
    }

    public void outALessOrEqualExpr(ALessOrEqualExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALessOrEqualExpr(ALessOrEqualExpr node)
    {
        inALessOrEqualExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outALessOrEqualExpr(node);
    }

    public void inAGreaterExpr(AGreaterExpr node)
    {
        defaultIn(node);
    }

    public void outAGreaterExpr(AGreaterExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAGreaterExpr(AGreaterExpr node)
    {
        inAGreaterExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAGreaterExpr(node);
    }

    public void inAGreaterOrEqualExpr(AGreaterOrEqualExpr node)
    {
        defaultIn(node);
    }

    public void outAGreaterOrEqualExpr(AGreaterOrEqualExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAGreaterOrEqualExpr(AGreaterOrEqualExpr node)
    {
        inAGreaterOrEqualExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAGreaterOrEqualExpr(node);
    }

    public void inAAndExpr(AAndExpr node)
    {
        defaultIn(node);
    }

    public void outAAndExpr(AAndExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAndExpr(AAndExpr node)
    {
        inAAndExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAAndExpr(node);
    }

    public void inAOrExpr(AOrExpr node)
    {
        defaultIn(node);
    }

    public void outAOrExpr(AOrExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAOrExpr(AOrExpr node)
    {
        inAOrExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAOrExpr(node);
    }

    public void inAAssignExpr(AAssignExpr node)
    {
        defaultIn(node);
    }

    public void outAAssignExpr(AAssignExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAssignExpr(AAssignExpr node)
    {
        inAAssignExpr(node);
        if(node.getR() != null)
        {
            node.getR().apply(this);
        }
        if(node.getL() != null)
        {
            node.getL().apply(this);
        }
        outAAssignExpr(node);
    }

    public void inAMethodExpr(AMethodExpr node)
    {
        defaultIn(node);
    }

    public void outAMethodExpr(AMethodExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMethodExpr(AMethodExpr node)
    {
        inAMethodExpr(node);
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
            Collections.reverse(copy);
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAMethodExpr(node);
    }

    public void inASeqExpr(ASeqExpr node)
    {
        defaultIn(node);
    }

    public void outASeqExpr(ASeqExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASeqExpr(ASeqExpr node)
    {
        inASeqExpr(node);
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
            Collections.reverse(copy);
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
        outASeqExpr(node);
    }

    public void inARecordExpr(ARecordExpr node)
    {
        defaultIn(node);
    }

    public void outARecordExpr(ARecordExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseARecordExpr(ARecordExpr node)
    {
        inARecordExpr(node);
        {
            List<PField> copy = new ArrayList<PField>(node.getField());
            Collections.reverse(copy);
            for(PField e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outARecordExpr(node);
    }

    public void inAArrayExpr(AArrayExpr node)
    {
        defaultIn(node);
    }

    public void outAArrayExpr(AArrayExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAArrayExpr(AArrayExpr node)
    {
        inAArrayExpr(node);
        if(node.getInit() != null)
        {
            node.getInit().apply(this);
        }
        if(node.getI() != null)
        {
            node.getI().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAArrayExpr(node);
    }

    public void inAIfExpr(AIfExpr node)
    {
        defaultIn(node);
    }

    public void outAIfExpr(AIfExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIfExpr(AIfExpr node)
    {
        inAIfExpr(node);
        if(node.getF() != null)
        {
            node.getF().apply(this);
        }
        if(node.getT() != null)
        {
            node.getT().apply(this);
        }
        if(node.getC() != null)
        {
            node.getC().apply(this);
        }
        outAIfExpr(node);
    }

    public void inAWhileExpr(AWhileExpr node)
    {
        defaultIn(node);
    }

    public void outAWhileExpr(AWhileExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAWhileExpr(AWhileExpr node)
    {
        inAWhileExpr(node);
        if(node.getT() != null)
        {
            node.getT().apply(this);
        }
        if(node.getC() != null)
        {
            node.getC().apply(this);
        }
        outAWhileExpr(node);
    }

    public void inAForExpr(AForExpr node)
    {
        defaultIn(node);
    }

    public void outAForExpr(AForExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAForExpr(AForExpr node)
    {
        inAForExpr(node);
        if(node.getT() != null)
        {
            node.getT().apply(this);
        }
        if(node.getUpper() != null)
        {
            node.getUpper().apply(this);
        }
        if(node.getLow() != null)
        {
            node.getLow().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAForExpr(node);
    }

    public void inABreakExpr(ABreakExpr node)
    {
        defaultIn(node);
    }

    public void outABreakExpr(ABreakExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABreakExpr(ABreakExpr node)
    {
        inABreakExpr(node);
        outABreakExpr(node);
    }

    public void inALetExpr(ALetExpr node)
    {
        defaultIn(node);
    }

    public void outALetExpr(ALetExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALetExpr(ALetExpr node)
    {
        inALetExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        {
            List<PDeclaration> copy = new ArrayList<PDeclaration>(node.getDeclaration());
            Collections.reverse(copy);
            for(PDeclaration e : copy)
            {
                e.apply(this);
            }
        }
        outALetExpr(node);
    }

    public void inASingleLvalue(ASingleLvalue node)
    {
        defaultIn(node);
    }

    public void outASingleLvalue(ASingleLvalue node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASingleLvalue(ASingleLvalue node)
    {
        inASingleLvalue(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outASingleLvalue(node);
    }

    public void inAListLvalue(AListLvalue node)
    {
        defaultIn(node);
    }

    public void outAListLvalue(AListLvalue node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAListLvalue(AListLvalue node)
    {
        inAListLvalue(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }
        outAListLvalue(node);
    }

    public void inAArrayLvalue(AArrayLvalue node)
    {
        defaultIn(node);
    }

    public void outAArrayLvalue(AArrayLvalue node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAArrayLvalue(AArrayLvalue node)
    {
        inAArrayLvalue(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }
        outAArrayLvalue(node);
    }

    public void inAField(AField node)
    {
        defaultIn(node);
    }

    public void outAField(AField node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAField(AField node)
    {
        inAField(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAField(node);
    }

    public void inATypeDeclaration(ATypeDeclaration node)
    {
        defaultIn(node);
    }

    public void outATypeDeclaration(ATypeDeclaration node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATypeDeclaration(ATypeDeclaration node)
    {
        inATypeDeclaration(node);
        if(node.getTypeCombination() != null)
        {
            node.getTypeCombination().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outATypeDeclaration(node);
    }

    public void inAVariableDeclaration(AVariableDeclaration node)
    {
        defaultIn(node);
    }

    public void outAVariableDeclaration(AVariableDeclaration node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVariableDeclaration(AVariableDeclaration node)
    {
        inAVariableDeclaration(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getTypeCombination() != null)
        {
            node.getTypeCombination().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAVariableDeclaration(node);
    }

    public void inAMethodDeclaration(AMethodDeclaration node)
    {
        defaultIn(node);
    }

    public void outAMethodDeclaration(AMethodDeclaration node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMethodDeclaration(AMethodDeclaration node)
    {
        inAMethodDeclaration(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getTypeCombination() != null)
        {
            node.getTypeCombination().apply(this);
        }
        {
            List<PTypeField> copy = new ArrayList<PTypeField>(node.getTypeField());
            Collections.reverse(copy);
            for(PTypeField e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAMethodDeclaration(node);
    }

    public void inASingleTypeCombination(ASingleTypeCombination node)
    {
        defaultIn(node);
    }

    public void outASingleTypeCombination(ASingleTypeCombination node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASingleTypeCombination(ASingleTypeCombination node)
    {
        inASingleTypeCombination(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outASingleTypeCombination(node);
    }

    public void inAArrayTypeCombination(AArrayTypeCombination node)
    {
        defaultIn(node);
    }

    public void outAArrayTypeCombination(AArrayTypeCombination node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAArrayTypeCombination(AArrayTypeCombination node)
    {
        inAArrayTypeCombination(node);
        if(node.getTypeCombination() != null)
        {
            node.getTypeCombination().apply(this);
        }
        outAArrayTypeCombination(node);
    }

    public void inARecordTypeCombination(ARecordTypeCombination node)
    {
        defaultIn(node);
    }

    public void outARecordTypeCombination(ARecordTypeCombination node)
    {
        defaultOut(node);
    }

    @Override
    public void caseARecordTypeCombination(ARecordTypeCombination node)
    {
        inARecordTypeCombination(node);
        {
            List<PTypeField> copy = new ArrayList<PTypeField>(node.getTypeField());
            Collections.reverse(copy);
            for(PTypeField e : copy)
            {
                e.apply(this);
            }
        }
        outARecordTypeCombination(node);
    }

    public void inATypeField(ATypeField node)
    {
        defaultIn(node);
    }

    public void outATypeField(ATypeField node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATypeField(ATypeField node)
    {
        inATypeField(node);
        if(node.getT() != null)
        {
            node.getT().apply(this);
        }
        if(node.getV() != null)
        {
            node.getV().apply(this);
        }
        outATypeField(node);
    }
}