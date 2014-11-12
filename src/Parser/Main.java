package Parser;

import java.io.FileReader;

import x86.x86Target;
import x86.x86TargetScope;
import AbsSytree.*;
import IR.*;

public class Main {
	public static void main(String[] args) throws Exception {
		parser p = new parser(new Lexer(new FileReader(args[0])));
		AbsSynNode result = (AbsSynNode) p.parse().value;
		
		result.visit(new TypeChecking());
		// result.clearAttr();
		//System.out.println("Output the syntax tree:");
		//result.dumpNode(0);
		
		IRGen ir = new IRGen();
		Scope irglobal = ir.startGen(result);
		// result.clearAttr();
		//System.out.println("Output the intermediate represention:");
		//System.out.println(irglobal.outputAll());
		//System.out.println("total ir blocks generated:" + BasicBlock.labCount);
//		irglobal.doLivenessAnalysis();
		x86Target target = new x86Target();
		x86TargetScope targetScope = (x86TargetScope) target.generate(irglobal);
		//System.out.println("Output the target code:");
		System.out.print(".686\n" + 
		".model flat, stdcall\n" + 
		"option casemap :none   ; case sensitive\n" +
		"include windows.inc\n" +
		"include user32.inc\n" +
		"include kernel32.inc\n" +
		"include masm32.inc\n" +
		".data\n");
		String strliteral = null;
		char []strarray;
		for (int i = 0; i < target.strLiterals.size(); ++i)
		{
			strarray = target.strLiterals.get(i).toCharArray();
			strliteral = new String();
			for (int j = 0; j < strarray.length; ++j)
				strliteral = strliteral+"0"+Integer.toHexString(strarray[j])+"h,";
			System.out.println("stringConst"+i+" db "+strliteral+"0");
		}
		for (int i = 0; i < target.framePtrList.size(); ++i)
			System.out.println("__"+target.framePtrList.get(i).toString()+"_fp dd ?");
		System.out.print(".code\n"+
		"extrn tg_print :near\n"+
		"extrn tg_newrecord :near\n"+
		"extrn tg_getchar :near\n"+
		"extrn tg_newarray :near\n"+
		"extrn tg_finalize :near\n"+
		"extrn tg_ord :near\n"+
		"extrn tg_chr :near\n"+
		"extrn tg_printi :near\n"+
		"extrn tg_streq :near\n"+
		"extrn tg_strneq :near\n"+
		"main PROC C\n"+
		"	call scope0\n"+
		"	call tg_finalize\n"+
		"	call ExitProcess\n"+
		"	ret 0\n"+
		"main endp\n");
		System.out.println(targetScope.outputAll());
		System.out.println("end");
	}
}
