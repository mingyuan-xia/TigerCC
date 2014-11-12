package Parse;



public interface Lexer {
    public java_cup.runtime.Symbol nextToken() throws java.io.IOException;
  //  public void errorMsg(String msg, java_cup.runtime.Symbol info);
  //  public Token nextToken() throws java.io.IOException;
    
}
 