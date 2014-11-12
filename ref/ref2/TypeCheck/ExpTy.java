package TypeCheck;
import Absyn.*;
public class ExpTy {
	Exp exp;
	public Types.Type ty;
  public ExpTy(Exp e,Types.Type t)
  {
	ty=t;exp=e;
  }
}