package Parse;

public class Token extends java_cup.runtime.Symbol {
 // int left,right;
  Token(int l, int r, int kind) {
   super(kind,l,r);
  // left=l; right=r;
  }
  Token(int l, int r, int kind,String value) {
	   super(kind,l,r,value);
	  // left=l; right=r;
	  }
}

class StrToken extends Token {
  String val;
  StrToken(int l, int r, int kind, String v) { 
    super(l,r,kind);
    val=v;
    this.value=v;
  }
  public Object getValue()
  {
	  return val;
  }
}

class IntToken extends Token {
  int val;
  IntToken(int l, int r, int kind, int v) { 
    super(l,r,kind);
    val=v;
this.value=v;
  }
  public Object getValue()
  {
	  return val;
  }
}
