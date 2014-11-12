package GUI;

import Parse.sym;
import java_cup.runtime.Symbol;


public class Result {
	String result;
	Symbol tok;
	public Result()
	{
		result="";
		tok=null;
	}
	public Result(String r)
	{
		result=r;
		tok=null;
	}
	public Result(Symbol t)
	{
		result="";
		tok=t;
	}
	public void setResult(String r)
	{
		result=r;
	}
	public String getResult()
	{
		return result;
	}
	public void setTok(Symbol  t)
	{
		tok=t;
	}
	public Symbol getTok()
	{
		return tok;
	}
	public String toString()
	{
		if(tok==null)return result;
		else 
		{
			if(tok.value==null)
				return "name["+GUI.Config.symnames2.get(tok.sym) + "]";
			if(tok.value instanceof String && tok.sym==sym.STRING)
				return "name[STRING] value["+tok.value+"]";
			if(tok.value instanceof String && tok.sym==sym.ID)
				return "["+tok.value+"]";
			if(tok.value instanceof Integer && tok.sym==sym.INT)
				return "name[INT] value["+tok.value+"]";
			return tok.getValue().getClass().toString().substring(6);
		}
	}
}
