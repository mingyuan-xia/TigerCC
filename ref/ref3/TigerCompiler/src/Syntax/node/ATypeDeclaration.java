/* This file was generated by SableCC (http://www.sablecc.org/). */

package Syntax.node;

import Syntax.analysis.*;

@SuppressWarnings("nls")
public final class ATypeDeclaration extends PDeclaration
{
    private TId _id_;
    private PTypeCombination _typeCombination_;

    public ATypeDeclaration()
    {
        // Constructor
    }

    public ATypeDeclaration(
        @SuppressWarnings("hiding") TId _id_,
        @SuppressWarnings("hiding") PTypeCombination _typeCombination_)
    {
        // Constructor
        setId(_id_);

        setTypeCombination(_typeCombination_);

    }

    @Override
    public Object clone()
    {
        return new ATypeDeclaration(
            cloneNode(this._id_),
            cloneNode(this._typeCombination_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATypeDeclaration(this);
    }

    public TId getId()
    {
        return this._id_;
    }

    public void setId(TId node)
    {
        if(this._id_ != null)
        {
            this._id_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._id_ = node;
    }

    public PTypeCombination getTypeCombination()
    {
        return this._typeCombination_;
    }

    public void setTypeCombination(PTypeCombination node)
    {
        if(this._typeCombination_ != null)
        {
            this._typeCombination_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._typeCombination_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._id_)
            + toString(this._typeCombination_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._id_ == child)
        {
            this._id_ = null;
            return;
        }

        if(this._typeCombination_ == child)
        {
            this._typeCombination_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._id_ == oldChild)
        {
            setId((TId) newChild);
            return;
        }

        if(this._typeCombination_ == oldChild)
        {
            setTypeCombination((PTypeCombination) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
