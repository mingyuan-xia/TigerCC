/* This file was generated by SableCC (http://www.sablecc.org/). */

package Syntax.node;

import Syntax.analysis.*;

@SuppressWarnings("nls")
public final class ALvalueExpr extends PExpr
{
    private PLvalue _lvalue_;

    public ALvalueExpr()
    {
        // Constructor
    }

    public ALvalueExpr(
        @SuppressWarnings("hiding") PLvalue _lvalue_)
    {
        // Constructor
        setLvalue(_lvalue_);

    }

    @Override
    public Object clone()
    {
        return new ALvalueExpr(
            cloneNode(this._lvalue_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALvalueExpr(this);
    }

    public PLvalue getLvalue()
    {
        return this._lvalue_;
    }

    public void setLvalue(PLvalue node)
    {
        if(this._lvalue_ != null)
        {
            this._lvalue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lvalue_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lvalue_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lvalue_ == child)
        {
            this._lvalue_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lvalue_ == oldChild)
        {
            setLvalue((PLvalue) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
