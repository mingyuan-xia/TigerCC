package TypeCheck;

import Types.*;
import ErrorMsg.*;
import Absyn.*;
public class Check {
Env env;
ErrorMsg errmsg;
int loop_count=0;
public Check(ErrorMsg e){
	env=new Env();
	errmsg=e;
}
public ExpTy check(Exp exp)
{
	if(exp instanceof VarExp)
		 return check((VarExp)exp);
	if(exp instanceof IntExp)
		 return check((IntExp)exp);
	if(exp instanceof StringExp)
		 return check((StringExp)exp);
	if(exp instanceof NilExp)
		 return check((NilExp)exp);
	if(exp instanceof CallExp)
		 return check((CallExp)exp);
	if(exp instanceof OpExp)
		 return check((OpExp)exp);
	if(exp instanceof IfExp)
		 return check((IfExp)exp);
	if(exp instanceof WhileExp)
		 return check((WhileExp)exp);
	if(exp instanceof ForExp)
		 return check((ForExp)exp);
	if(exp instanceof ArrayExp)
		 return check((ArrayExp)exp);
	if(exp instanceof RecordExp)
		 return check((RecordExp)exp);
	if(exp instanceof AssignExp)
		 return check((AssignExp)exp);
	if(exp instanceof LetExp)
		 return check((LetExp)exp);
	if(exp instanceof SeqExp)
		 return check((SeqExp)exp);
	errmsg.error(exp.pos, "unknown exp");
	return null;
}
public ExpTy check(VarExp exp){//变量表达式,不需检查，直接返回
	return check(exp.var);
}
ExpTy check(Var exp)//变量
{
	if(exp instanceof SimpleVar)
		return (check((SimpleVar)exp));
	if(exp instanceof SubscriptVar)
		return (check((SubscriptVar)exp));
	if(exp instanceof FieldVar)
		return (check((FieldVar)exp));
	return new ExpTy(null,new INT());
}
ExpTy check(SimpleVar exp)//简单变量
{
	Entry en=(Entry)env.varenv.get(exp.name);
	if(en instanceof VarEntry)  //类型已经定义
	{
		VarEntry ent = (VarEntry)en;
		return new ExpTy(null,ent.ty);
	}else{
		errmsg.error(exp.pos, "undefined variable ");
	    return new ExpTy(null,new INT());
	}
}


ExpTy check(SubscriptVar exp)//数组变量
{
	ExpTy r=check(exp.var);
	Type n=r.ty.actual();
	if(!(n instanceof ARRAY))//类型为非数组
		{ 
			errmsg.error(exp.pos, "variable is not an array");
	     return  new ExpTy(null,n);}
	ExpTy t=check(exp.index);
	Type  in=t.ty.actual();
	if(!(in instanceof INT))//下标为非整数
		errmsg.error(exp.pos, "index is not INT");
	return new ExpTy(null,((ARRAY)n).element);
}

ExpTy check(FieldVar exp)//域变量
{
	ExpTy r=check(exp.var);
	Type n=r.ty.actual();
	if(!(n instanceof RECORD))//类型为非RECORD
		{errmsg.error(exp.pos, "variable is not lvalue");
	     return  new ExpTy(null,n);}
	RECORD in=(RECORD)n.actual();
	while(in!=null){
		if(in.fieldName==exp.field) break;
		in=in.tail;
	}
	if(in==null) {errmsg.error(exp.pos, "variable is not a member");//不在RECORD中
    				return  new ExpTy(null,n);}
	else return new ExpTy(null,in.fieldType);
}

ExpTy check(IntExp exp)  //int直接返回，不需检查
{
	return new ExpTy(null,new INT());
}
ExpTy check(NilExp exp) //nil直接返回，不需检查
{
	return new ExpTy(null,new NIL());
}
ExpTy check(StringExp exp)//string直接返回，不需检查
{
	return new ExpTy(null,new STRING());
}
ExpTy check(OpExp exp){
	Type left,right;
	left=check(exp.left).ty.actual();
	right=check(exp.right).ty.actual();
	
	if(exp.oper==OpExp.UMINUS) //负号,right必须为int
		if(!(right instanceof INT)) 
			errmsg.error(exp.pos,"UMINUS right must be int" );
	if(exp.oper==OpExp.PLUS || exp.oper==OpExp.DIV || exp.oper==OpExp.MINUS || exp.oper==OpExp.MUL)//加减乘除，right，left都必须为int
	{
		if(!(right instanceof INT)) 
			errmsg.error(exp.pos,"right must be int" );
	    if(!(left instanceof INT)) 
		errmsg.error(exp.pos,"left must be int" );}
	if(exp.oper==OpExp.EQ || exp.oper==OpExp.NE)//相等，不等，right，left类型相同，或者一个是NIL，一个是RECORD
	{
		if(right.coerceTo(left) || (right instanceof NIL) && (left instanceof RECORD) || (left instanceof NIL) && (right instanceof RECORD))
			;
		else errmsg.error(exp.pos, "Wrong Type");
	}
	if(exp.oper>=OpExp.LT && exp.oper<=OpExp.OR){//关系运算符，right，left必须为INT 或者 STRING
		if((right instanceof INT) && (left instanceof INT) || (right instanceof STRING) && (left instanceof STRING)) 
			;
		else errmsg.error(exp.pos, "Wrong Type");
	}
	
	return new ExpTy(null,new Types.INT());
	}
ExpTy check(CallExp exp)//函数调用表达式
{  Entry en=(Entry)env.varenv.get(exp.func);
	if(en==null)
	{
		errmsg.error(exp.pos, "Undefined function");
		return new ExpTy(null,new VOID());
	}
	if(en instanceof FunEntry){
		FunEntry fe=(FunEntry)en;
		RECORD tr=fe.formals;//形参
		ExpList el=exp.args;//实参
		while(tr!=null && el!=null){
			ExpTy ty=check(el.head);
			//System.out.println(tr.fieldName);
			//System.out.println(ty.ty.getClass());
			if(!(ty.ty.coerceTo(tr.fieldType)))//类型是否匹配
				errmsg.error(exp.pos, "function parameter  is not matched ");
			tr=tr.tail;
			el=el.tail;
		}
		//if(tr!=null) System.out.println("XINGCAN ");
		//if(el!=null) {System.out.println("shican "+el.head);}
		if(tr!=null || el!=null) //数目是否匹配
			errmsg.error(exp.pos, "function parameter number is not matched");
		return new ExpTy(null,fe.result);
	}
	else {
	      errmsg.error(exp.pos, "Unkown Call");
	      return new ExpTy(null,new VOID());
	}
}
ExpTy check(IfExp exp){//if表达式
	  ExpTy test=check(exp.test);
	  if(!(test.ty.actual() instanceof INT)) //test应返回整型
		  errmsg.error(exp.pos, "Wrong test");
	  ExpTy th=check(exp.thenclause);
	  if(exp.elseclause!=null && exp.thenclause==null)//缺少else语句，then有返回值 
		  errmsg.error(exp.pos, "Lack of thenclause");
	  if(exp.elseclause!=null){
		  ExpTy el=check(exp.elseclause);
		  if(!th.ty.coerceTo(el.ty)) 
			  if(!((th.ty.actual() instanceof NIL) || (el.ty.actual() instanceof NIL)))//then 与else 返回值不同，并且不是NIL
			  {errmsg.error(exp.pos, "Return type of elseclause and thenclause is not the same");
			  // System.out.println(th.ty.actual().getClass()+"asdf"+el.ty.getClass());
			  }
	  	}
	  
	 return th;
	}
ExpTy check(WhileExp exp){//while表达式
	
	ExpTy test=check(exp.test);
	  if(!(test.ty.actual() instanceof INT)) //test应返回整型
		  errmsg.error(exp.pos, "Wrong test");
	  loop_count=0;
	  newloop();
	  ExpTy e=check(exp.body);
	  if(e.ty!=null && !(e.ty.actual() instanceof Types.VOID)) errmsg.error(exp.pos, "While  body can not return  value");
	  endloop();                          
	  return new ExpTy(null,new Types.VOID());
}

ExpTy check(BreakExp exp)//break表达式
{
	if(loop_count<=0) errmsg.error(exp.pos, "Break must in loop");//loop_count<=0说明不再循环中
	endloop();
	return new ExpTy(null,new Types.VOID());
}
void newloop(){                  //开始一个循环       
	env.varenv.beginScope();
	loop_count++;
	
}
void endloop(){                    //结束一个循环
	env.varenv.endScope();
	loop_count--;
}

ExpTy check(ForExp exp){           //for表达式
	loop_count=0;
	newloop();
	check(exp.var);
	Entry v=(Entry)env.varenv.get(exp.var.name);
	ExpTy hi=check(exp.hi);
	if(!(((VarEntry)v).ty instanceof INT) || !(hi.ty.actual() instanceof INT))//循环变量应为INT
		errmsg.error(exp.pos, "Must be int");
	else
		check(exp.body);
	endloop();
	return new ExpTy(null,new Types.VOID());
}

ExpTy check(ArrayExp exp){ //数组表达式
	ExpTy s=check(exp.size);
	if(!(s.ty.actual() instanceof INT)) //下标应为INT
		errmsg.error(exp.pos, "Array index must be int");
	Type t=(Type)env.tyenv.get(exp.typ);
	if(t==null)//类型表中无记录
		errmsg.error(exp.pos, "Undefined Type here");
	if(!(t instanceof Types.NAME) || !(t.actual() instanceof Types.ARRAY))  //非ARRAY
		    	errmsg.error(exp.pos, "NOT array");
	t=t.actual();
	if(!(((ARRAY)t).element.coerceTo(check(exp.init).ty))) //初始类型与声明类型不一致
		errmsg.error(exp.pos, "Wrong type; not the same");
	return new ExpTy(null,t);
}
ExpTy check(RecordExp exp){//record表达式	
	Type e=(Types.Type)env.tyenv.get(exp.typ);
	if(e instanceof NAME){ //已经定义
		RECORD r=(RECORD)e.actual();
		FieldExpList f=exp.fields;
		while(r!=null && f!=null){//检查每个类型是否存在
			if(!f.name.equals(r.fieldName))
				errmsg.error(exp.pos, "Undefined feild");
			Type t=r.fieldType;
			if(r.fieldType instanceof NAME)
				t=((NAME)r.fieldType).actual();
			if(!t.coerceTo(check(f.init).ty) && !(check(f.init).ty instanceof NIL))
				errmsg.error(exp.pos, "unmatched type");
			r=r.tail;
			f=f.tail;
		}
		if(f!=null || r!=null)
			errmsg.error(exp.pos, "Unmatched");
		return new ExpTy(null,e);
	}
	else
		errmsg.error(exp.pos, "undefined type");
	return new ExpTy(null,new VOID());
}
ExpTy check(AssignExp exp){//赋值表达式
	ExpTy e=check(exp.exp);
	
	if(e.ty.actual() instanceof VOID)//右边为VOID，出错
		errmsg.error(exp.pos, "It is wrong Type change");
	ExpTy e2=check(exp.var);
	if(!(e.ty.coerceTo(e2.ty))) //两边类型不能转换，出错
		errmsg.error(exp.pos, "It is wrong Type change ");
	return new ExpTy(null,null);
}
ExpTy check(LetExp exp){//let表达式
	env.varenv.beginScope();
	env.tyenv.beginScope();
	for(DecList p=exp.decs;p!=null;p=p.tail)
		check(p.head);
	ExpTy t=check(exp.body);
	env.varenv.endScope();
	env.tyenv.endScope();
	return new ExpTy(null,t.ty);
}


ExpTy check(SeqExp exp){//SeqExp 检查
	if(exp==null || exp.list==null)//为空
		   return new ExpTy(null,new Types.VOID());
	ExpTy t = null;
	for(ExpList s=exp.list;s!=null;s=s.tail)//一个一个检查序列，返回最后一个的值
		 t=check(s.head);
	return t;
}
ExpTy check(Dec exp){//声明检查
	if(exp instanceof VarDec)
		return (check((VarDec)exp));
	if(exp instanceof TypeDec)
		return (check((TypeDec)exp));
	if(exp instanceof FunctionDec)
		return (check((FunctionDec)exp));
	errmsg.error(exp.pos, "Wrong Dec");
	return null;
}
ExpTy check(VarDec exp){ //变量声明检查
	ExpTy e=check(exp.init);
	
	if(exp.typ==null){   //无显示声明类型
		//System.out.println("as");
		if(e.ty.actual() instanceof NIL)//右边为NIL，出错
			errmsg.error(exp.pos, "It is wrong Dec ,inintial");
	 }
	else{
		Type t=check(exp.typ);
		//System.out.println(t.actual().getClass()+"  "+e.ty.actual());
		t=t.actual();
		if(!(t.coerceTo(e.ty.actual()))) //类型不相同
			errmsg.error(exp.pos, "Dec error,unmatched Type");
	}
	
	env.varenv.put(exp.name, new VarEntry(check(exp.init).ty));
	return null;
	}
ExpTy check(TypeDec exp){//类型声明
	 for(TypeDec d=exp;d!=null;d=d.next)
	   { Type a=(Type)env.tyenv.get(d.name); 
		 if(a!=null && a.actual().coerceTo(check(d.ty))) //在同一块中是否已经声明,如果重复声明，但类型不一样依然合法
		   		errmsg.error(d.pos, "type already defined");
	   		env.tyenv.put(d.name,new NAME(d.name));
	   }
	
	 for(TypeDec d=exp;d!=null;d=d.next){
		    if(d.ty==null) //类型绑定
				((NAME)env.tyenv.get(d.name)).bind(new NAME(d.name));
			else
				((NAME)env.tyenv.get(d.name)).bind(check(d.ty));
			if(((NAME)env.tyenv.get(d.name)).isLoop())//循环声明
			{   errmsg.error(d.pos, "Loop declaration");
	 			((NAME)env.tyenv.get(d.name)).bind(new Types.VOID());}
		
	
	 }
		return null;
		
}
ExpTy check(FunctionDec exp){//函数声明
	for(FunctionDec d=exp;d!=null;d=d.next)
	{  	Type re=check(d.result);
		RECORD rc=(RECORD)check(d.params);
		Entry en=(Entry)env.varenv.get(d.name);
		if(en!=null) errmsg.error(exp.pos, "Defined more than once");
		else 
			env.varenv.put(d.name, new FunEntry(rc,re));//添加函数入口
	}
	for(FunctionDec d=exp;d!=null;d=d.next)
	{	
		env.varenv.beginScope();
		Type re=check(d.result);
		for(FieldList f=d.params;f!=null;f=f.tail)  //参数处理
		{
			Types.Type ty=(Types.Type)env.tyenv.get(f.typ);
			if(ty==null)
				errmsg.error(d.pos, "undefined type of para");
			env.varenv.put(f.name, new VarEntry(ty));
		}
	
		if(!re.actual().coerceTo(check(d.body).ty.actual()))//检查返回类型是否相符
			errmsg.error(d.pos, "Wrong return Type");
		env.varenv.endScope();
	}
	return null;
}
Type check(Ty ty)
{   
	if(ty instanceof RecordTy)
		return check((RecordTy) ty);
	if(ty instanceof ArrayTy)
		return check((ArrayTy) ty);
	if(ty instanceof NameTy)
		return check((NameTy) ty);
	errmsg.error(ty.pos, "unknow type");
	return null;
}
Type check(NameTy ty){
	if(ty==null) 
		return new VOID();
	else return (Type)env.tyenv.get(ty.name);
}
Types.Type check(ArrayTy ty)
{
	Type t=(Types.Type)env.tyenv.get(ty.typ); 
	if(t==null) errmsg.error(ty.pos, "undefined type");
	return new Types.ARRAY(t);
}
Type check(RecordTy ty){
	env.tyenv.beginScope();
	Type tmp=check(ty.fields);
	env.tyenv.endScope();
	return tmp;
}
Type check(FieldList fl){
	if(fl==null)return null;
	RECORD rc=new RECORD(fl.name,(Types.Type)env.tyenv.get(fl.typ),null);
	RECORD tmp=rc;
	for(FieldList f=fl.tail;f!=null;f=f.tail)
	{
		rc.tail=new Types.RECORD(f.name,(Types.Type)env.tyenv.get(f.typ),null);
		rc=rc.tail;
	}
	return tmp;
}
}
