package syntax_analyzer.compiler.model;
import syntax_analyzer.compiler.MiniJavaParser;
import syntax_analyzer.compiler.visitor.MiniJavaParserVisitor;

/* Generated By:JJTree: Do not edit this line. ASTMethodDeclaration.java */

public class ASTMethodDeclaration extends SimpleNode {
  public ASTMethodDeclaration(int id) {
    super(id);
  }

  public ASTMethodDeclaration(MiniJavaParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MiniJavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
