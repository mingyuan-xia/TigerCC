/* This file was generated by SableCC (http://www.sablecc.org/). */

package Syntax.node;

import Syntax.analysis.*;

@SuppressWarnings("nls")
public final class TControlChar extends Token
{
    public TControlChar(String text)
    {
        setText(text);
    }

    public TControlChar(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TControlChar(getText(), getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTControlChar(this);
    }
}
