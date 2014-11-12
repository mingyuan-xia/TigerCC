package Semant;

import Symbol.Table;
import Symbol.Symbol;

public class Env {
Table venv =new Table();
Table tenv = new Table();
ErrorMsg.ErrorMsg errorMsg;
String text="";
 public	Env(ErrorMsg.ErrorMsg err,String t)
	{
	 errorMsg = err;
	 text=t;
	 
	 tenv.put(Symbol.symbol("int"), new Types.INT());
	 tenv.put(Symbol.symbol("string"), new Types.STRING());
	 
	 
	 
	 Types.Type re=new Types.VOID();
	 Types.RECORD rc=new Types.RECORD(Symbol.symbol("s"),(Types.Type)tenv.get(Symbol.symbol("string")),null);
	 venv.put(Symbol.symbol("print"), new FunEntry(rc,re));
	 venv.beginScope();
       venv.put(Symbol.symbol("s"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("string"))));
     venv.endScope();
     
	 re=new Types.VOID();
	 rc=new Types.RECORD(Symbol.symbol("i"),(Types.Type)tenv.get(Symbol.symbol("int")),null);
	 venv.put(Symbol.symbol("printi"), new FunEntry(rc,re));
	 venv.beginScope();
       venv.put(Symbol.symbol("i"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("int"))));
     venv.endScope();
     
	 re=new Types.VOID();
	 venv.put(Symbol.symbol("flush"), new FunEntry(null,re));
	
	 re=new Types.STRING();
	 venv.put(Symbol.symbol("getchar"), new FunEntry(null,re));
	
	 re=new Types.INT();
	 rc=new Types.RECORD(Symbol.symbol("s"),(Types.Type)tenv.get(Symbol.symbol("string")),null);
	 venv.put(Symbol.symbol("ord"), new FunEntry(rc,re));
	 venv.beginScope();
     venv.put(Symbol.symbol("s"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("string"))));
     venv.endScope();
     
	 re=new Types.STRING();
	 rc=new Types.RECORD(Symbol.symbol("i"),(Types.Type)tenv.get(Symbol.symbol("int")),null);
	 venv.put(Symbol.symbol("chr"), new FunEntry(rc,re));
	 venv.beginScope();
       venv.put(Symbol.symbol("i"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("int"))));
     venv.endScope();
     
	 re=new Types.INT();
	 rc=new Types.RECORD(Symbol.symbol("s"),(Types.Type)tenv.get(Symbol.symbol("string")),null);
	 venv.put(Symbol.symbol("size"), new FunEntry(rc,re));
	 venv.beginScope();
     venv.put(Symbol.symbol("s"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("string"))));
     venv.endScope();
     
	 re=new Types.STRING();
	 rc=new Types.RECORD(Symbol.symbol("n"),(Types.Type)tenv.get(Symbol.symbol("int")),null);
	 rc.tail=new Types.RECORD(Symbol.symbol("f"),(Types.Type)tenv.get(Symbol.symbol("int")),null);
	 rc.tail.tail=new Types.RECORD(Symbol.symbol("s"),(Types.Type)tenv.get(Symbol.symbol("string")),null);
	 venv.put(Symbol.symbol("substring"), new FunEntry(rc,re));
	 venv.beginScope();
     venv.put(Symbol.symbol("n"), 
  		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("int"))));
     venv.put(Symbol.symbol("f"), 
  		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("int"))));
     venv.put(Symbol.symbol("s"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("string"))));
     venv.endScope();
     
	 re=new Types.STRING();
	 rc=new Types.RECORD(Symbol.symbol("s2"),(Types.Type)tenv.get(Symbol.symbol("string")),null);
	 rc.tail=new Types.RECORD(Symbol.symbol("s1"),(Types.Type)tenv.get(Symbol.symbol("string")),null);
	 venv.put(Symbol.symbol("concat"), new FunEntry(rc,re));
	 venv.beginScope();
     venv.put(Symbol.symbol("s2"), 
  		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("string"))));
     venv.put(Symbol.symbol("s1"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("string"))));
     venv.endScope();
     
	 re=new Types.INT();
	 rc=new Types.RECORD(Symbol.symbol("i"),(Types.Type)tenv.get(Symbol.symbol("int")),null);
	 venv.put(Symbol.symbol("not"), new FunEntry(rc,re));
	 venv.beginScope();
     venv.put(Symbol.symbol("i"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("int"))));
     venv.endScope();
     
	 re=new Types.VOID();
	 rc=new Types.RECORD(Symbol.symbol("i"),(Types.Type)tenv.get(Symbol.symbol("int")),null);
	 venv.put(Symbol.symbol("exit"), new FunEntry(rc,re));
	 venv.beginScope();
       venv.put(Symbol.symbol("i"), 
    		   new VarEntry((Types.Type)tenv.get(Symbol.symbol("int"))));
     venv.endScope();
	// venv.put(key, value)
	}
 public String error(int pos,String err)
 {
	 return errorMsg.errorLine(pos,Env.getLine(pos,text),err);
 }
 public static  int getLine(int pos,String t)
 {
    if(pos<=0)return 1;
    int line=1;
    for(int i=0;i<t.length();i++)
    {
 	   if(i>=pos)break;
 	   if(t.charAt(i)=='\n')
 		   line++;
    }
    return line;
 }
}
