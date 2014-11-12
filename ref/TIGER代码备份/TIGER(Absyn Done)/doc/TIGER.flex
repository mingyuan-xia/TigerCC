/* JFlex example: part of Java language lexer specification */

package Parse;
import java_cup.runtime.*;
import ErrorMsg.ErrorMsg;

/**
 * This class is a simple example lexer.
 */
%%

%public
%class Lexer
%extends sym

%unicode

%debug
%line
%column
%char

%cup

%{
  StringBuffer string = new StringBuffer();
  sym sym;
  int level = 0;
  
  private void newline() {
  	errorMsg.newline(yychar);
  }
  
  private void err(int pos,String s) {
  	errorMsg.error(pos,s);
  }
  
  private void err(String s) {
  	err(yychar,s);
  }
  
  private ErrorMsg errorMsg;

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn,null);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
    
  public Lexer(java.io.InputStream s,ErrorMsg e) {
  	this(s);
  	errorMsg = e;
  }
%}

%eof{
  /* your code goes here */
%eof}

LineTerminator = \r|\n|\r\n|\n\r
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

Identifier = [a-zA-Z][a-zA-Z0-9_]*

%state STRING
%state NESTED_COMMENT

%%

/* keywords */
<YYINITIAL> "array"              { return symbol(sym.ARRAY); }
<YYINITIAL> "break"              { return symbol(sym.BREAK); }
<YYINITIAL> "do"                 { return symbol(sym.DO); }
<YYINITIAL> "else"               { return symbol(sym.ELSE); }
<YYINITIAL> "end"                { return symbol(sym.END); }
<YYINITIAL> "for"                { return symbol(sym.FOR); }
<YYINITIAL> "function"           { return symbol(sym.FUNCTION); }
<YYINITIAL> "if"                 { return symbol(sym.IF); }
<YYINITIAL> "in"                 { return symbol(sym.IN); }
<YYINITIAL> "let"                { return symbol(sym.LET); }
<YYINITIAL> "nil"                { return symbol(sym.NIL); }
<YYINITIAL> "of"                 { return symbol(sym.OF); }
<YYINITIAL> "then"               { return symbol(sym.THEN); }
<YYINITIAL> "to"                 { return symbol(sym.TO); }
<YYINITIAL> "type"               { return symbol(sym.TYPE); }
<YYINITIAL> "var"                { return symbol(sym.VAR); }
<YYINITIAL> "while"              { return symbol(sym.WHILE); }


<YYINITIAL> {
  /* identifiers */ 
  {Identifier}                   { return symbol(sym.IDENTIFIER,yytext()); }
 
  /* literals */
  \"                             { string.setLength(0); yybegin(STRING); }
  [0-9]+					     { return symbol(sym.NUM,new Integer(yytext())); }

  /* punctuation symbols */
  ","                            { return symbol(sym.COMMA); }
  ":"                            { return symbol(sym.COLON); }
  ";"                            { return symbol(sym.SEMICOLON); }
  "("                            { return symbol(sym.L1); }
  ")"                            { return symbol(sym.R1); }
  "["                            { return symbol(sym.L2); }
  "]"                            { return symbol(sym.R2); }
  "{"                            { return symbol(sym.L3); }
  "}"                            { return symbol(sym.R3); }
  "."                            { return symbol(sym.DOT); }
  "+"                            { return symbol(sym.ADD); }
  "-"                            { return symbol(sym.SUB); }
  "*"                            { return symbol(sym.MUL); }
  "/"                            { return symbol(sym.DIV); }
  "="                            { return symbol(sym.EQ); }
  "<>"                           { return symbol(sym.NEQ); }
  "<"                            { return symbol(sym.LESS); }
  "<="                           { return symbol(sym.NGTR); }
  ">"                            { return symbol(sym.GTR); }
  ">="                           { return symbol(sym.NLESS); }
  "&"                            { return symbol(sym.AND); }
  "|"                            { return symbol(sym.OR); }
  ":="                           { return symbol(sym.ASSIGN); }

  /* comments */
  "/*"                           { level = 1; yybegin(NESTED_COMMENT); }
  "*/"							 { err("Comment don't match!"); }
 
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<NESTED_COMMENT> {
  "/*"                           { ++level; }
  "*/"                           { --level; if(level == 0) yybegin(YYINITIAL); }
  [^]							 {/* ignore */}
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return symbol(sym.STRING, string.toString()); }
  [^\n\t\"\\\r]+                 { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }
  \\\"                           { string.append('\"'); }
  \\\\				             { string.append('\\'); }
  \\\^[@A-Z\[\\\]\^_]            { string.append((char)yycharat(yylength() - 1) - 64); }
  \\[0-9][0-9][0-9]              { int tmp = yycharat(yylength() - 3) * 100 + yycharat(yylength() - 2) * 10 + yycharat(yylength() - 1);
  								   if(tmp > 255) err("exceed ASCII code \\ddd!"); else string.append((char)tmp); }
  \r						     { err("Error:\\r in string!"); }
  \\                             { string.append('\\'); }
  \\[ \t\n\r\f]+\\               { /* ignore */ }
}

/* error fallback */
<YYINITIAL>.|\n                  { throw new Error("Illegal character <"+ yytext()+">"); }