package ErrorMsg;

import GUI.*;
import GUI.Error;

public class ErrorMsg {
  private LineList linePos = new LineList(-1,null);
  private int lineNum=1;
  private String filename;
  private messPrinter printer;
  public boolean anyErrors;

  public ErrorMsg(String f) {
      filename=f;
      printer = (messPrinter)new SysPrinter();
  }

  public ErrorMsg(String f,tigerFrame ots) {
      filename=f;
      printer = (messPrinter)ots;
  }
  
  public void newline(int pos) {
     lineNum++;
     linePos = new LineList(pos,linePos);
  }
  public String error(int pos,String msg) {
	int n = lineNum;
        LineList p = linePos;
	String sayPos="0.0";

	anyErrors=true;

        while (p!=null) {
          if (p.head<pos) {
	     sayPos = ":" + String.valueOf(n) + "." + String.valueOf(pos-p.head);
	     break;
          }
	  p=p.tail; n--;
        }
    String error=filename + ":" + sayPos + ": " + msg;
    Error err=new Error(n,error);
    if(printer!=null)
	printer.printErr(err);
	return error;
  }
  public String errorLine(int pos,int line,  String msg) {

		String sayPos="0.0";
        sayPos = ":" + String.valueOf(line) + "." + String.valueOf(pos);

	    String error=filename + ":" + sayPos + ": " + msg;
	    Error err=new Error(line,error);
	    if(printer!=null)
		printer.printErr(err);
		return error;
	  }
}

class LineList {
  int head;
  LineList tail;
  LineList(int h, LineList t) {head=h; tail=t;}
}
