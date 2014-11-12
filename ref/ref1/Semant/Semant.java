package Semant;

import java.util.ArrayList;

import Translate.*;

public class Semant {
Env env;

public Semant(ErrorMsg.ErrorMsg err,String t){this(new Env(err,t));}
Semant(Env e){env=e;}
ExpTy transVar(Absyn.Var var)
{
	return null;
}
public ExpTy transExp(Absyn.Exp exp)
{
	
	if(exp instanceof Absyn.VarExp)
		return transExp((Absyn.VarExp)exp);
	if(exp instanceof Absyn.IntExp)
		return transExp((Absyn.IntExp)exp);
	if(exp instanceof Absyn.StringExp)
		return transExp((Absyn.StringExp)exp);
	if(exp instanceof Absyn.NilExp)
		return transExp((Absyn.NilExp)exp);
	if(exp instanceof Absyn.CallExp)
		return transExp((Absyn.CallExp)exp);
	if(exp instanceof Absyn.OpExp)
		return transExp((Absyn.OpExp)exp);
	if(exp instanceof Absyn.IfExp)
		return transExp((Absyn.IfExp)exp);
	if(exp instanceof Absyn.WhileExp)
		return transExp((Absyn.WhileExp)exp);
	if(exp instanceof Absyn.ForExp)
		return transExp((Absyn.ForExp)exp);
	if(exp instanceof Absyn.ArrayExp)
		return transExp((Absyn.ArrayExp)exp);
	if(exp instanceof Absyn.RecordExp)
			return transExp((Absyn.RecordExp)exp);
	if(exp instanceof Absyn.AssignExp)
		return transExp((Absyn.AssignExp)exp);
	if(exp instanceof Absyn.LetExp)
		return transExp((Absyn.LetExp)exp);
	if(exp instanceof Absyn.SeqExp)
		return transExp((Absyn.SeqExp)exp);
	env.error(exp.pos, "unknown exp");
	return new ExpTy(null,new Types.VOID());
}
ExpTy transExp(Absyn.CallExp exp)
{
	Entry e=(Entry)env.venv.get(exp.func);
	//System.out.println("-------------------call "+exp.func);
	if(e==null)
	{
		env.error(exp.pos, "function ["+exp.func+"] is not defined");
		return new ExpTy(null,new Types.VOID());
	}
	if(e instanceof FunEntry){
		FunEntry fe=(FunEntry)e;
	Types.RECORD tr=fe.formals;
	Types.RECORD i=tr;Absyn.ExpList el=exp.args;
	
	int index=0;
	for(;i!=null && el!=null;i=i.tail,el=el.tail)
	{
		index++;
		ExpTy ty=transExp(el.head);
		if(!ty.ty.coerceTo(i.fieldType))
		{
			env.error(exp.pos, "function parameter [index:"+index+"] is not matched");
		}	
	}
	if(!(i==null && el==null))
		env.error(exp.pos, "function parameter number is not matched");
	return new ExpTy(null,fe.result);
	}else 
		{
		env.error(exp.pos, "unknown function call");
		return new ExpTy(null,((VarEntry)e).ty);
		}
}
ExpTy transExp(Absyn.OpExp exp)
{
	ExpTy left,right;
	switch(exp.oper)
	{
	case Absyn.OpExp.UMINUS:
		right=transExp(exp.right);
		if(!(right.ty instanceof Types.INT ))
		    env.error(exp.pos, "oper["+getOperName(exp.oper)+"]integer required");
		break;
	case Absyn.OpExp.EQ:
	case Absyn.OpExp.NE:
		left=transExp(exp.left);
		right=transExp(exp.right);
		if(!left.ty.coerceTo(right.ty))
		{
			if(!(left.ty instanceof Types.NIL ||right.ty instanceof Types.NIL))
			{
				env.error(exp.pos, "oper["+getOperName(exp.oper)+"] variable type is not suitble!");
			}
		}
		break;
		default:
		left=transExp(exp.left);
		right=transExp(exp.right);
		if(!(left.ty instanceof Types.INT ) || !(right.ty instanceof Types.INT ))
		{
			env.error(exp.pos, "oper["+getOperName(exp.oper)+"]integer required");
		}
			break;
	}
	return new ExpTy(null,new Types.INT());
}
String getOperName(int t)
{
	switch(t)
	{
	case Absyn.OpExp.AND:
		return "&";
	case Absyn.OpExp.DIV:
		return "/";
	case Absyn.OpExp.EQ:
		return "=";
	case Absyn.OpExp.GE:
		return ">=";
	case Absyn.OpExp.GT:
		return ">";
	case Absyn.OpExp.LE:
		return "<=";
	case Absyn.OpExp.LT:
		return "<=";
	case Absyn.OpExp.MINUS:
		return "-";
	case Absyn.OpExp.MUL:
		return "*";
	case Absyn.OpExp.NE:
		return "<>";
	case Absyn.OpExp.OR:
		return "|";
	case Absyn.OpExp.PLUS:
		return "+";
	case Absyn.OpExp.UMINUS:
		return "u-";
		default:
			return "unknown oper";
	}
}
ExpTy transExp(Absyn.VarExp exp)
{
		return (transExp(exp.var));
}
ExpTy transExp(Absyn.Var exp)
{
	if(exp instanceof Absyn.SimpleVar)
		return (transExp((Absyn.SimpleVar)exp));
	if(exp instanceof Absyn.SubscriptVar)
		return (transExp((Absyn.SubscriptVar)exp));
	if(exp instanceof Absyn.FieldVar)
		return (transExp((Absyn.FieldVar)exp));
	return new ExpTy(null,new Types.INT());
}
ExpTy transExp(Absyn.SimpleVar exp)
{
	Entry x=(Entry)env.venv.get(exp.name);
	if(x instanceof VarEntry)
	{
		VarEntry ent = (VarEntry)x;
		return new ExpTy(null,ent.ty);
	}else{
		env.error(exp.pos, "undefined variable ["+exp.name+"]");
	return new ExpTy(null,new Types.INT());
	}
}
ExpTy transExp(Absyn.SubscriptVar exp)
{
	ExpTy r=transExp(exp.var);
	Types.Type n=r.ty;
	if(r.ty instanceof Types.NAME) n=r.ty.actual();
	if(!(n instanceof Types.ARRAY))
		env.error(exp.pos, "variable is not an array");
	ExpTy t=transExp(exp.index);
	if(!(t.ty instanceof Types.INT))
		env.error(exp.pos, "array index must be an integer");
	if(!(n instanceof Types.ARRAY))
		return  new ExpTy(null,n);
	else 
		return new ExpTy(null,((Types.ARRAY)n).element);
}
ExpTy transExp(Absyn.FieldVar exp)
{
	ExpTy t=transExp(exp.var);
	if(t.ty instanceof Types.NAME)
	{
		Types.RECORD r=(Types.RECORD)t.ty.actual();
		for(;r!=null;r=r.tail)
		{
			if(r.fieldName.equals(exp.field))
			{
				break;
			}
		}
		if(r==null)
			env.error(exp.pos, "["+exp.field+"] is not a member");
		else
			return new ExpTy(null,r.fieldType);
	}else
		env.error(exp.pos, "it is not a lvalue");

	return new ExpTy(null,new Types.VOID());
}
ExpTy transExp(Absyn.IntExp exp)
{
	return new ExpTy(null,new Types.INT());
}
ExpTy transExp(Absyn.NilExp exp)
{
	return new ExpTy(null,new Types.NIL());
}
ExpTy transExp(Absyn.StringExp exp)
{
	return new ExpTy(null,new Types.STRING());
}
ExpTy transExp(Absyn.IfExp exp)
{
	ExpTy et=transExp(exp.test);
	if(!(et.ty instanceof Types.INT))
		env.error(exp.pos, "if case must be an integer");
	ExpTy et1=transExp(exp.thenclause);
	if(exp.elseclause!=null)
	{
		  ExpTy et2=transExp(exp.elseclause);
		  if(!et1.ty.coerceTo(et2.ty))
		  {
			  if(!(et1.ty instanceof Types.NIL || et2.ty instanceof Types.NIL))
		     env.error(exp.pos, "if then exp must be same type");
		  }
	}
	return et1;
}
ExpTy transExp(Absyn.WhileExp exp)
{
	ExpTy et=transExp(exp.test);
	if(!(et.ty instanceof Types.INT))
		env.error(exp.pos, "while case must be an integer");
	transExp(exp.body);
	return new ExpTy(null,new Types.VOID());//et1;
}
ExpTy transExp(Absyn.ForExp exp)
{
	env.venv.beginScope();
	transDec(exp.var);
	
	Entry e=(Entry)env.venv.get(exp.var.name);
	if(!(e instanceof VarEntry))
	{
		env.error(exp.pos, "["+exp.var.name+"] must be a variable");
	}else
	{
		if(!(((VarEntry)e).ty instanceof Types.INT))
			env.error(exp.pos, "["+exp.var.name+"] must be an integer");
	}
	ExpTy et=transExp(exp.hi);
	if(!(et.ty instanceof Types.INT))
		env.error(exp.pos, "for to must be an integer");
	transExp(exp.body);
	env.venv.endScope();
	return new ExpTy(null,new Types.VOID());//et1;
}
ExpTy transExp(Absyn.BreakExp exp)
{
	return new ExpTy(null,new Types.VOID());
}
ExpTy transExp(Absyn.AssignExp exp)
{
	ExpTy et1=transExp(exp.exp);
	ExpTy et2=transExp(exp.var);
	if(!et1.ty.coerceTo(et2.ty))
		env.error(exp.pos, "assign different type");
	return et2;
}
ExpTy transExp(Absyn.ArrayExp exp)
{
	ExpTy et=transExp(exp.size);
	if(!(et.ty instanceof Types.INT))
		env.error(exp.pos, "array index must be an integer");
	Types.Type t=(Types.Type)env.tenv.get(exp.typ);
    if(!(t instanceof Types.NAME) || !(t.actual() instanceof Types.ARRAY))
	{
		env.error(exp.pos, "["+exp.typ+"]is not an array");
		return et;
	}
	Types.Type a=t;
	if(t instanceof Types.NAME)a=t.actual();
	if(a instanceof Types.ARRAY)
	if(!((Types.ARRAY)a).element.coerceTo(transExp(exp.init).ty))
	{
		env.error(exp.pos, "array inital value type and element type is not same");
	}
	return new ExpTy(null,a);
}

ExpTy transExp(Absyn.RecordExp exp)
{
	Types.Type e=(Types.Type)env.tenv.get(exp.typ);
	if(e instanceof Types.NAME)
	{
		Types.RECORD r=(Types.RECORD)e.actual();
		Absyn.FieldExpList f=exp.fields;
		//if(f==null)env.error(exp.pos, "["+exp.typ+"]is not an array");
		for(;r!=null&&f!=null;r=r.tail,f=f.tail)
		{
				if(!f.name.equals(r.fieldName))
					env.error(exp.pos, "undefined feild ["+f.name+"]");
				Types.Type t=r.fieldType;
				if(r.fieldType instanceof Types.NAME)
					t=((Types.NAME)r.fieldType).actual();
				if(!t.coerceTo(transExp(f.init).ty) &&
						!(transExp(f.init).ty instanceof Types.NIL))
					env.error(exp.pos, "unmatched type ["+r.fieldName+"]");
		}
		if(r!=null || f!=null)
			env.error(exp.pos, "unmatched type");
			
		return new ExpTy(null,e);
	}else
	{
		env.error(exp.pos, "undefined type ["+exp.typ+"]");
	}
	return new ExpTy(null,new Types.VOID());
}
ExpTy transExp(Absyn.SeqExp exp)
{
   if(exp==null || exp.list==null)
	   return new ExpTy(null,new Types.VOID());
        ExpTy t = null;
		for(Absyn.ExpList s=exp.list;s!=null;s=s.tail)
		{
			t=transExp(s.head);
		}
		
		return t;

	//return transExp(exp.list.head);
}

ExpTy transExp(Absyn.LetExp exp)
{
	env.venv.beginScope();
	env.tenv.beginScope();

	for(Absyn.DecList p=exp.decs;p!=null;p=p.tail)
	{
		transDec(p.head);
	}

	
	ExpTy t=transExp(exp.body);
	env.venv.endScope();
	env.tenv.endScope();
	return new ExpTy(null,t.ty);
}


Exp transDec(Absyn.VarDec dec)
{
	ExpTy t=transExp(dec.init);

	if(dec.typ !=null)
	{
		Types.Type t1=transTy(dec.typ);
		if(t1 instanceof Types.NAME)t1=t1.actual();
		
		if(!t.ty.coerceTo(t1))
		{
			if((!(t1 instanceof Types.NAME)||!(t1 instanceof Types.NAME))
					&& !(t.ty instanceof Types.NIL) )
			{
				env.error(dec.pos, "type is not matched");
			}
		}
	}
	else
		if(t.ty instanceof Types.NIL)
			env.error(dec.pos, "variable ["+dec.name+"] initialed as nil");
	env.venv.put(dec.name, new VarEntry(transExp(dec.init).ty));
	return null;
}

Exp transDec(Absyn.TypeDec dec)
{
	 ArrayList<String> list =new ArrayList<String>();
   for(Absyn.TypeDec d=dec;d!=null;d=d.next)
   {
	   
	   if(list.contains(d.name.toString()))
		    env.error(d.pos, "type ["+d.name+"] already defined");
	   list.add(d.name.toString());
		env.tenv.put(d.name,new Types.NAME(d.name));
   }
   list.clear();
   for(Absyn.TypeDec d=dec;d!=null;d=d.next)
   {
		if(d.ty==null)
			((Types.NAME)env.tenv.get(d.name)).bind(new Types.NAME(d.name));
		else
		((Types.NAME)env.tenv.get(d.name)).bind(transTy(d.ty));
   }
   for(Absyn.TypeDec d=dec;d!=null;d=d.next)
		if(((Types.NAME)env.tenv.get(d.name)).isLoop())
			 env.error(d.pos, "type ["+d.name+"] is undefined");
	
   for(Absyn.TypeDec d=dec;d!=null;d=d.next)
		if(((Types.NAME)env.tenv.get(d.name)).isLoop())
			((Types.NAME)env.tenv.get(d.name)).bind(new Types.VOID());

	return null;
}

Exp transDec(Absyn.FunctionDec dec)
{
	 ArrayList<String> list =new ArrayList<String>();
	for(Absyn.FunctionDec d=dec;d!=null;d=d.next)
	{
	Types.Type re=transTy(d.result);
	Types.RECORD rc=transTypeFeilds(d.params);
		
	if(list.contains(d.name.toString()))
		env.error(d.pos, "function ["+d.name+"] already defined");	
	list.add(d.name.toString());
	
	env.venv.put(d.name, new FunEntry(rc,re));
	}
	list.clear();
   
	for(Absyn.FunctionDec d=dec;d!=null;d=d.next)
	{
		//System.out.println("\n+++++++++++++++++++");
		 env.venv.beginScope();
		 
		Types.Type re=transTy(d.result);
		
		list.clear();
		for(Absyn.FieldList f=d.params;f!=null;f=f.tail)
		{
			if(list.contains(f.name.toString()))
				env.error(d.pos, "function parameter ["+f.name+"] already defined ");
	
			list.add(f.name.toString());
			Types.Type ty=(Types.Type)env.tenv.get(f.typ);
			if(ty==null)
				env.error(d.pos, "undefined type ["+f.typ+"]");
	
			env.venv.put(f.name, new VarEntry(ty));
			//System.out.println(f.name+" funciotn para+++++++++++++++++++");
		}
		//System.out.println("+++++++++++++++++++trans "+((Absyn.SeqExp)d.body).list.tail.head.getClass());
		if(!re.actual().coerceTo(transExp(d.body).ty.actual()))
			env.error(d.pos, "function return type is not matched");
		
		env.venv.endScope();//System.out.println("+++++++++++++++++++\n");
	}
	
	//for(Absyn.FunctionDec d=dec;d!=null;d=d.next)
	//{
		
	//}
	return null;
}
Types.RECORD transTypeFeilds(Absyn.FieldList dec)
{
	if(dec==null)return null;
	Types.RECORD rc=new Types.RECORD(dec.name,(Types.Type)env.tenv.get(dec.typ),null);
	Types.RECORD tmp=null;
	tmp=rc;
	for(Absyn.FieldList f=dec.tail;f!=null;f=f.tail)
	{
		rc.tail=new Types.RECORD(f.name,(Types.Type)env.tenv.get(f.typ),null);
		rc=rc.tail;
	}
	return tmp;
}
Exp transDec(Absyn.Dec dec)
{
	if(dec instanceof Absyn.FunctionDec)
		return transDec((Absyn.FunctionDec)dec);
	if(dec instanceof Absyn.TypeDec)
		return transDec((Absyn.TypeDec)dec);
	if(dec instanceof Absyn.VarDec)
		return transDec((Absyn.VarDec)dec);
	env.error(dec.pos, "unknow declare");
	return null;
}
Types.Type transTy(Absyn.Ty ty)
{
	if(ty instanceof Absyn.RecordTy)
		{
		return transTy((Absyn.RecordTy) ty);
		}
	if(ty instanceof Absyn.ArrayTy)
	{
	return transTy((Absyn.ArrayTy) ty);
	}
	if(ty instanceof Absyn.NameTy)
	{
	return transTy((Absyn.NameTy) ty);
	}
	env.error(ty.pos, "unknow type");
	return null;
}
Types.Type transTy(Absyn.RecordTy ty)
{
	Types.RECORD rc=null,tmp=null;
	if(env.tenv.get(ty.fields.typ)==null)
		env.error(ty.pos, "undefined type ["+ty.fields.typ+"]");
	rc=new Types.RECORD(ty.fields.name,(Types.Type)env.tenv.get(ty.fields.typ),null);
	ArrayList<String>list=new ArrayList<String>();
	list.add(ty.fields.name.toString());
	tmp=rc;
	env.venv.beginScope();

	for(Absyn.FieldList f=ty.fields.tail;f!=null;f=f.tail)
	{
		if(list.contains(f.name.toString()))
			env.error(ty.pos, "feild ["+f.name+"] already defined");
		else list.add(f.name.toString());

		Types.Type t=(Types.Type)env.tenv.get(f.typ);
		if(t==null)
		{
			env.error(ty.pos, "undefined type ["+f.typ+"]");
		}
		
		env.venv.put(f.name, new VarEntry(t));
		tmp.tail=new Types.RECORD(f.name,t,null);
		tmp=tmp.tail;
	}
	env.venv.endScope();
     
     return rc;
}
Types.Type transTy(Absyn.ArrayTy ty)
{
	Types.Type t=(Types.Type)env.tenv.get(ty.typ); 
	if(t==null)env.error(ty.pos, "undefined type ["+ty.typ+"]");
	return new Types.ARRAY(t);
}
Types.Type transTy(Absyn.NameTy ty)
{
	if(ty==null)
		return new Types.VOID();
	Types.Type t=(Types.Type)env.tenv.get(ty.name);

	return t;
}


}
