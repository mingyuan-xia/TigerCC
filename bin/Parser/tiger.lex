/* JFlex script for tiger by Kenmark
*  2009-12-9
*/

package Parser;

import java_cup.runtime.*;
import Parser.sym;
import util.ErrorMsg;
%%

%public
%class Lexer

%unicode

%line
%char

%cup

%{
  /* Custom code for lexer */
  StringBuffer str_literal = new StringBuffer();
  int comment_layer = 0;
  
  private void lexerPanic(String msg)
  {
    ErrorMsg.instance.err("Lexer panic @<" + yyline + ">:" + msg);
  }

  private Symbol symbol(int type)
  {
    //System.out.println("token:<"+type+">{"+yytext()+"}");
    return new Symbol(type, yyline, 0);
  }
  private Symbol symbol(int type, Object value) {
    //System.out.println("token:<"+type+">{"+yytext()+"}");
    return new Symbol(type, yyline, 0, value);
  }
%}

ALPHA = [a-zA-Z]
DIGIT = [0-9]
BLANK_CHARS = [ \t\f\r\n]

%state STRING_LITERAL
%state NESTED_COMMENT

%%


<YYINITIAL>"array"              { return symbol(sym.ARRAY);    }
<YYINITIAL>"break"              { return symbol(sym.BREAK);    }
<YYINITIAL>"do"                 { return symbol(sym.DO);       }
<YYINITIAL>"else"               { return symbol(sym.ELSE);     }
<YYINITIAL>"end"                { return symbol(sym.END);      }
<YYINITIAL>"for"                { return symbol(sym.FOR);      }
<YYINITIAL>"function"           { return symbol(sym.FUNCTION); }
<YYINITIAL>"if"                 { return symbol(sym.IF);       }
<YYINITIAL>"in"                 { return symbol(sym.IN);       }
<YYINITIAL>"let"                { return symbol(sym.LET);      }
<YYINITIAL>"nil"                { return symbol(sym.NIL);      }
<YYINITIAL>"of"                 { return symbol(sym.OF);       }
<YYINITIAL>"then"               { return symbol(sym.THEN);     }
<YYINITIAL>"to"                 { return symbol(sym.TO);       }
<YYINITIAL>"type"               { return symbol(sym.TYPE);     }
<YYINITIAL>"var"                { return symbol(sym.VAR);      }
<YYINITIAL>"while"              { return symbol(sym.WHILE);    }

<YYINITIAL>[a-zA-Z][a-zA-Z0-9_]* { return symbol(sym.IDENTIFIER, yytext()); }
 
<YYINITIAL>\"                   { str_literal.setLength(0); yybegin(STRING_LITERAL);    }
<YYINITIAL>{DIGIT}+			    { return symbol(sym.INTEGER, yytext()); }

<YYINITIAL>","                  { return symbol(sym.COMMA);       }
<YYINITIAL>":"                  { return symbol(sym.COLON);       }
<YYINITIAL>";"                  { return symbol(sym.SEMICOLON);   }
<YYINITIAL>"("                  { return symbol(sym.LPARENTHESE); }
<YYINITIAL>")"                  { return symbol(sym.RPARENTHESE); }
<YYINITIAL>"["                  { return symbol(sym.LBRACKET);    }
<YYINITIAL>"]"                  { return symbol(sym.RBRACKET);    }
<YYINITIAL>"{"                  { return symbol(sym.LBRACE);      }
<YYINITIAL>"}"                  { return symbol(sym.RBRACE);      }
<YYINITIAL>"."                  { return symbol(sym.DOT);         }
<YYINITIAL>"+"                  { return symbol(sym.ADD);         }
<YYINITIAL>"-"                  { return symbol(sym.SUB);         }
<YYINITIAL>"*"                  { return symbol(sym.MUL);         }
<YYINITIAL>"/"                  { return symbol(sym.DIV);         }
<YYINITIAL>"="                  { return symbol(sym.EQ);          }
<YYINITIAL>"<>"                 { return symbol(sym.NEQ);         }
<YYINITIAL>"<"                  { return symbol(sym.LESS);        }
<YYINITIAL>"<="                 { return symbol(sym.LESSEQ);      }
<YYINITIAL>">"                  { return symbol(sym.GREATER);     }
<YYINITIAL>">="                 { return symbol(sym.GREATEREQ);   }
<YYINITIAL>"&"                  { return symbol(sym.AND);         }
<YYINITIAL>"|"                  { return symbol(sym.OR);          }
<YYINITIAL>":="                 { return symbol(sym.ASSIGN);      }

<YYINITIAL>"/*"                 { comment_layer = 1; yybegin(NESTED_COMMENT); }
<YYINITIAL>"*/"			 	    { lexerPanic("Unmatched comment!"); }
 
<YYINITIAL>{BLANK_CHARS}*       { /* ignore */ }

<NESTED_COMMENT>"/*"            { ++comment_layer; }
<NESTED_COMMENT>"*/"            { if(--comment_layer == 0) yybegin(YYINITIAL); }
<NESTED_COMMENT>[ \t\f\r\n]|.   { /* ignore */ }

<STRING_LITERAL>\"              { yybegin(YYINITIAL); return symbol(sym.STRING, str_literal.toString()); }
<STRING_LITERAL>[^\n\r\"\\]+    { str_literal.append( yytext() ); }
<STRING_LITERAL>\\t             { str_literal.append('\t'); }
<STRING_LITERAL>\\n             { str_literal.append('\n'); }
<STRING_LITERAL>\\{BLANK_CHARS}+\\ { /* ignore */ }
<STRING_LITERAL>\\r             { str_literal.append('\r'); }
<STRING_LITERAL>\\\"            { str_literal.append('\"'); }
<STRING_LITERAL>\\\\			{ str_literal.append('\\'); }
<STRING_LITERAL>\\[0-9][0-9][0-9] { int tmp = Integer.parseInt(yytext().substring(1, 4));
                                    if(tmp>255) lexerPanic("exceed \\ddd"); else str_literal.append((char)tmp);}

<YYINITIAL>.|\n                 { throw new Error("Illegal character <"+ yytext()+">"); }