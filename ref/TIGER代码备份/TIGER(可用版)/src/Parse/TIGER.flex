/* JFlex example: part of Java language lexer specification */

package Parse;

import java_cup.runtime.*;
import ErrorMsg.ErrorMsg;

/**
 * This class is a simple example lexer.
 */
%%

%debug

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

  /*private java_cup.runtime.Symbol symbol(int type) {
  	System.out.println("hi");
    return new java_cup.runtime.Symbol(type, yyline, yycolumn,null);
  }
  private java_cup.runtime.Symbol symbol(int type, Object value) {
    return new java_cup.runtime.Symbol(type, yyline, yycolumn, value);*/
    
    private java_cup.runtime.Symbol symbol(int type, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar + yylength(), value);
  }
    
  Yylex(java.io.InputStream s,ErrorMsg e) {
  	this(s);
  	errorMsg = e;
  }
%}

%eof{
  /* your code goes here */
%eof}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

Identifier = [a-zA-Z][a-zA-Z0-9_]*

%state STRING
%state NESTED_COMMENT

%%

/* keywords */
<YYINITIAL> "array"              { return symbol(sym.ARRAY,null); }
<YYINITIAL> "break"              { return symbol(sym.BREAK,null); }
<YYINITIAL> "do"                 { return symbol(sym.DO,null); }
<YYINITIAL> "else"               { return symbol(sym.ELSE,null); }
<YYINITIAL> "end"                { return symbol(sym.END,null); }
<YYINITIAL> "for"                { return symbol(sym.FOR,null); }
<YYINITIAL> "function"           { return symbol(sym.FUNCTION,null); }
<YYINITIAL> "if"                 { return symbol(sym.IF,null); }
<YYINITIAL> "in"                 { return symbol(sym.IN,null); }
<YYINITIAL> "let"                { System.out.println("hihi");return symbol(sym.LET,null); }
<YYINITIAL> "nil"                { return symbol(sym.NIL,null); }
<YYINITIAL> "of"                 { return symbol(sym.OF,null); }
<YYINITIAL> "then"               { return symbol(sym.THEN,null); }
<YYINITIAL> "to"                 { return symbol(sym.TO,null); }
<YYINITIAL> "type"               { return symbol(sym.TYPE,null); }
<YYINITIAL> "var"                { return symbol(sym.VAR,null); }
<YYINITIAL> "while"              { return symbol(sym.WHILE,null); }


<YYINITIAL> {
  /* identifiers */ 
  {Identifier}                   { return symbol(sym.IDENTIFIER,yytext()); }
 
  /* literals */
  \"                             { string.setLength(0); yybegin(STRING); }
  [0-9]+					     { return symbol(sym.NUM,new Integer(yytext())); }

  /* punctuation symbols */
  ","                            { return symbol(sym.COMMA,null); }
  ":"                            { return symbol(sym.COLON,null); }
  ";"                            { return symbol(sym.SEMICOLON,null); }
  "("                            { return symbol(sym.L1,null); }
  ")"                            { return symbol(sym.R1,null); }
  "["                            { return symbol(sym.L2,null); }
  "]"                            { return symbol(sym.R2,null); }
  "{"                            { return symbol(sym.L3,null); }
  "}"                            { return symbol(sym.R3,null); }
  "."                            { return symbol(sym.DOT,null); }
  "+"                            { return symbol(sym.ADD,null); }
  "-"                            { return symbol(sym.SUB,null); }
  "*"                            { return symbol(sym.MUL,null); }
  "/"                            { return symbol(sym.DIV,null); }
  "="                            { return symbol(sym.EQ,null); }
  "<>"                           { return symbol(sym.NEQ,null); }
  "<"                            { return symbol(sym.LESS,null); }
  "<="                           { return symbol(sym.NGTR,null); }
  ">"                            { return symbol(sym.GTR,null); }
  ">="                           { return symbol(sym.NLESS,null); }
  "&"                            { return symbol(sym.AND,null); }
  "|"                            { return symbol(sym.OR,null); }
  ":="                           { return symbol(sym.ASSIGN,null); }

  /* comments */
  "/*"                           { level = 1; yybegin(NESTED_COMMENT); }
  "*/"							 { err("Comment don't match!"); }
 
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<NESTED_COMMENT> {
  "/*"                           { ++level; }
  "*/"                           { --level; if(level == 0) yybegin(YYINITIAL); }
  .                             { /* ignore */ }
}

<STRING> {
  \"                             { yybegin(YYINITIAL); 
                                   return symbol(sym.STRING, 
                                   string.toString()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\[ \t\n\r\f]+\\               { /* ignore */ }
  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\\\				             { string.append("\\\\"); }
  \\\^[@A-Z\[\\\]\^_]            { string.append((char)yycharat(yylength() - 1) - 64); }
  \\[0-9][0-9][0-9]              { int tmp = yycharat(yylength() - 3) * 100 + yycharat(yylength() - 2) * 10 + yycharat(yylength() - 1);
  								   if(tmp > 255) err("exceed ASCII code \\ddd!"); else string.append((char)tmp); }
  \\                             { string.append('\\'); }
}

/* error fallback */
<YYINITIAL>.|\n                  { throw new Error("Illegal character <"+ yytext()+">"); }