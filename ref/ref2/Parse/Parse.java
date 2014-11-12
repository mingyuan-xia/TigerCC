package Parse;
import Absyn.*;
import TypeCheck.*;
public class Parse {

  public ErrorMsg.ErrorMsg errorMsg;

  public Parse(String filename) {
       errorMsg = new ErrorMsg.ErrorMsg(filename);
       java.io.InputStream inp;
       try {inp=new java.io.FileInputStream(filename);
       } catch (java.io.FileNotFoundException e) {
	 throw new Error("File not found: " + filename);
       }
      
        parser grm=new parser(new Yylex(inp,errorMsg),errorMsg);
       
      try {
    	Print printer =new Print(System.out);
    	Exp val=(Exp)grm.parse().value;
    	printer.prExp(val, 1);
    	System.out.println();
    	Check tmp=new Check(errorMsg);
    	tmp.check(val);
    
    	} catch (Throwable e) {
	e.printStackTrace();
	throw new Error(e.toString());
      } 
      finally {
         try {inp.close();} catch (java.io.IOException e) {}
      }
  }
}
   