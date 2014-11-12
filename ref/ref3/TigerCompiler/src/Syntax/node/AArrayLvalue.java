/* This file was generated by SableCC (http://www.sablecc.org/). */

package Syntax.node;

import Syntax.analysis.*;

@SuppressWarnings("nls")
public final class AArrayLvalue extends PLvalue
{
    private PLvalue _lvalue_;
    private PExpr _expr_;

    public AArrayLvalue()
    {
        // Constructor
    }

    public AArrayLvalue(
        @SuppressWarnings("hiding") PLvalue _lvalue_,
        @SuppressWarnings("hiding") PExpr _expr_)
    {
        // Constructor
        setLvalue(_lvalue_);

        setExpr(_expr_);

    }

    @Override
    public Object clone()
    {
        return new AArrayLvalue(
            cloneNode(this._lvalue_),
            cloneNode(this._expr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAArrayLvalue(this);
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

    public PExpr getExpr()
    {
        return this._expr_;
    }

    public void setExpr(PExpr node)
    {
        if(this._expr_ != null)
        {
            this._expr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expr_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lvalue_)
            + toString(this._expr_);
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

        if(this._expr_ == child)
        {
            this._expr_ = null;
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

        if(this._expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
