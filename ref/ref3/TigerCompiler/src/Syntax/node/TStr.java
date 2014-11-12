/* This file was generated by SableCC (http://www.sablecc.org/). */

package Syntax.node;

import Syntax.analysis.*;

@SuppressWarnings("nls")
public final class TStr extends Token
{
    public TStr(String text)
    {
        setText(text);
    }

    public TStr(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TStr(getText(), getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTStr(this);
    }
}
