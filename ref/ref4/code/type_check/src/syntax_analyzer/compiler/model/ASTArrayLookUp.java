package syntax_analyzer.compiler.model;
import syntax_analyzer.compiler.MiniJavaParser;
import syntax_analyzer.compiler.visitor.MiniJavaParserVisitor;

/* Generated By:JJTree: Do not edit this line. ASTArrayLookUp.java */

public class ASTArrayLookUp extends SimpleNode {
  public ASTArrayLookUp(int id) {
    super(id);
  }

  public ASTArrayLookUp(MiniJavaParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
