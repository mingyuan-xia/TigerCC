package TypeCheck;

import Symbol.*;
import Types.*;
public class Env {
	Table varenv =new Table();
	Table tyenv = new Table();
	Type re;
	RECORD rc; 
public	Env(){
		inittyenv();
		 initvarenv();
}
public void inittyenv(){
		 tyenv.put(Symbol.symbol("int"), new INT());
		 tyenv.put(Symbol.symbol("string"), new STRING());}
public void initvarenv(){
		 re=new VOID();//print（s:string）
		 rc=new RECORD(Symbol.symbol("s"),(Type)tyenv.get(Symbol.symbol("string")),null);
		 varenv.put(Symbol.symbol("print"), new FunEntry(rc,re));
		
	     re=new VOID();//printi(s:int)
	     rc=new RECORD(Symbol.symbol("s"),(Type)tyenv.get(Symbol.symbol("int")),null);
	     varenv.put(Symbol.symbol("printi"), new FunEntry(rc,re));
	     
	     re=new VOID();//flush()
	     varenv.put(Symbol.symbol("flush"),new FunEntry(null,re));
	   	
	     re=new STRING();//getchar() string
	     varenv.put(Symbol.symbol("getchar"),new FunEntry(null,re));
	     
	     re=new INT();//ord(s:string) int
	     rc=new RECORD(Symbol.symbol("s"),(Type)tyenv.get(Symbol.symbol("string")),null);
	     varenv.put(Symbol.symbol("ord"),new FunEntry(rc,re));
	     
	     re=new STRING();//chr(i:int) string
	     rc=new RECORD(Symbol.symbol("i"),(Type)tyenv.get(Symbol.symbol("int")),null);
	     varenv.put(Symbol.symbol("chr"),new FunEntry(rc,re));
	     
	     re=new INT();//size(s:string) int
	     rc=new RECORD(Symbol.symbol("s"),(Type)tyenv.get(Symbol.symbol("string")),null);
	     varenv.put(Symbol.symbol("size"),new FunEntry(rc,re));
	     
	     re=new STRING();//substring(s:string f:int n:int) string
	     RECORD rc2=new RECORD(Symbol.symbol("n"),(Type)tyenv.get(Symbol.symbol("int")),null);
	     RECORD rc1=new RECORD(Symbol.symbol("f"),(Type)tyenv.get(Symbol.symbol("int")),rc2);
	     rc=new RECORD(Symbol.symbol("s"),(Type)tyenv.get(Symbol.symbol("string")),rc1);
	     varenv.put(Symbol.symbol("substring"),new FunEntry(rc,re));
	     
	     re=new STRING();//concat(s1:string s2:string) string
	     rc1=new RECORD(Symbol.symbol("s1"),(Type)tyenv.get(Symbol.symbol("string")),null);
	     rc=new RECORD(Symbol.symbol("s2"),(Type)tyenv.get(Symbol.symbol("string")),rc1);
	     varenv.put(Symbol.symbol("concat"),new FunEntry(rc,re));
	     
	     re=new INT();//not(i:int) int
	     rc=new RECORD(Symbol.symbol("i"),(Type)tyenv.get(Symbol.symbol("int")),null);
	     varenv.put(Symbol.symbol("not"),new FunEntry(rc,re));
	     
	     re=new VOID();//exit(i:int)
	     rc=new RECORD(Symbol.symbol("i"),(Type)tyenv.get(Symbol.symbol("int")),null);
	     varenv.put(Symbol.symbol("exit"),new FunEntry(rc,re));
	     
		}
}
