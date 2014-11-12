/* This file was generated by SableCC (http://www.sablecc.org/). */

package Syntax.node;

import java.util.*;
import Syntax.analysis.*;

@SuppressWarnings("nls")
public final class ARecordExpr extends PExpr
{
    private TId _id_;
    private final LinkedList<PField> _field_ = new LinkedList<PField>();

    public ARecordExpr()
    {
        // Constructor
    }

    public ARecordExpr(
        @SuppressWarnings("hiding") TId _id_,
        @SuppressWarnings("hiding") List<PField> _field_)
    {
        // Constructor
        setId(_id_);

        setField(_field_);

    }

    @Override
    public Object clone()
    {
        return new ARecordExpr(
            cloneNode(this._id_),
            cloneList(this._field_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseARecordExpr(this);
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

    public LinkedList<PField> getField()
    {
        return this._field_;
    }

    public void setField(List<PField> list)
    {
        this._field_.clear();
        this._field_.addAll(list);
        for(PField e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._id_)
            + toString(this._field_);
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

        if(this._field_.remove(child))
        {
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

        for(ListIterator<PField> i = this._field_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PField) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}
