package Semant;

public class FunEntry extends Entry{
Types.RECORD formals;
Types.Type result;
public FunEntry(Types.RECORD f,Types.Type r)
{formals=f;result=r;}
}
