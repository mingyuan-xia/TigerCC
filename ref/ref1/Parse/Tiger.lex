package Parse;
import ErrorMsg.ErrorMsg;

%% 

%implements Lexer
%function nextToken
%type Token
%char

%{
private void newline() {
  errorMsg.newline(yychar);
}

private String err(int pos, String s) {
  return errorMsg.error(pos,s);
}

private String err(String s) {
  return err(yychar,s);
}
private String transMeaning(String s)
{
    char tmp_c=0;
    String tmp_s="";
    
    if("\\t".equals(s)) tmp_c= 9;
    else
    if("\\n".equals(s)) tmp_c= 10;
    else
    if("\\r".equals(s)) tmp_c= 13;
    else
    if("\\".equals(s)) tmp_c= '\\';
    else
    if("\\\"".equals(s))tmp_c= '"';
    else
    if(s.length()>=4&&s.charAt(0)=='\\'&&Character.isDigit(s.charAt(1))&&
    Character.isDigit(s.charAt(2))&&Character.isDigit(s.charAt(3)))
    {

    int tmp=0;
    tmp=(s.charAt(1)-48)*100;
    tmp+=(s.charAt(2)-48)*10;
    tmp+=(s.charAt(3)-48);
    if(tmp<256&&tmp>=0)
    tmp_c= (char)tmp;
    else err("Wrong character [" + s + "]");
    }



    if(tmp_c!=0)tmp_s+=tmp_c;
    return tmp_s;
}
public Yylex(java.io.Reader s, ErrorMsg e) {
	  this(s);
	  errorMsg=e;
	}
private Token tok(int kind) {
    return new Token(yychar, yychar+yylength(),kind);
}
private StrToken errorTok(int kind,String value) {
    return new StrToken(yychar, yychar+yylength(),kind,value);
}
private IntToken intTok(int kind,int value) {
    return new IntToken(yychar, yychar+yylength(),kind,value);
}
private StrToken strTok(int left,int kind, String value) {
    return new StrToken(left, yychar+yylength(),kind,  value);
}
private Token commTok(int left,int kind) {
    return new Token(left, yychar+yylength(),kind);
}
private StrToken idTok(int kind,String value) {
    return new StrToken(yychar, yychar+yylength(),kind,value);
}

private ErrorMsg errorMsg;
private String con_str="";
private int comm_depth=0;
private int comm_left=0;
private int str_left=0;

public Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg=e;
}

%}

%eofval{
	{
	if(yy_lexical_state == STRING||yy_lexical_state == STRINGLINES)
	    {
	    err("Unclosed string constant.[" + yytext() + "]");
	    err("Wrong end of file.");
	    return errorTok(sym.EOF,str_left+"_STRING");
	    }
	   if(yy_lexical_state == COMMENT)
	    {
	    err("Unclosed comment.[" + yytext() + "]");
	    err("Wrong end of file." );
	    return errorTok(sym.EOF,comm_left+"_COMM");
	    }

	 return tok(sym.EOF);
  }
%eofval}       

%state STRING,STRINGLINES,COMMENT

id=[A-Za-z][A-Za-z_0-9]*
ws=("\012"|[\t\ ])*
num=[0-9]+
%%
<YYINITIAL,COMMENT> [\ \t\f]		{}
<YYINITIAL,COMMENT> (\r\n|\n|\r)	{newline();}

<YYINITIAL>{ws}	{}
<YYINITIAL>\r\n	{newline();}
<YYINITIAL>\n	{newline();}
<YYINITIAL>","	{return tok(sym.COMMA);}
<YYINITIAL>"{"	{return tok(sym.LBRACE);}
<YYINITIAL>"}"	{return tok(sym.RBRACE);}
<YYINITIAL>"["	{return tok(sym.LBRACK);}
<YYINITIAL>"]"	{return tok(sym.RBRACK);}
<YYINITIAL>":"	{return tok(sym.COLON);}
<YYINITIAL>";"	{return tok(sym.SEMICOLON);}
<YYINITIAL>"("	{return tok(sym.LPAREN);}
<YYINITIAL>")"	{return tok(sym.RPAREN);}
<YYINITIAL>"."	{return tok(sym.DOT);}
<YYINITIAL>"+"	{return tok(sym.PLUS);}
<YYINITIAL>"-"	{return tok(sym.MINUS);}
<YYINITIAL>"*"	{return tok(sym.TIMES);}
<YYINITIAL>"/"	{return tok(sym.DIVIDE);}
<YYINITIAL>"&"	{return tok(sym.AND);}
<YYINITIAL>"|"	{return tok(sym.OR);}
<YYINITIAL>":="	{return tok(sym.ASSIGN);}
<YYINITIAL>"="	{return tok(sym.EQ);}
<YYINITIAL>"<>"	{return tok(sym.NEQ);}
<YYINITIAL>">"	{return tok(sym.GT);}
<YYINITIAL>"<"	{return tok(sym.LT);}
<YYINITIAL>">="	{return tok(sym.GE);}
<YYINITIAL>"<="	{return tok(sym.LE);}
<YYINITIAL>for	{return tok(sym.FOR);}
<YYINITIAL>while {return tok(sym.WHILE);}
<YYINITIAL>break {return tok(sym.BREAK);}
<YYINITIAL>let 	{return tok(sym.LET);}
<YYINITIAL>in  	{return tok(sym.IN);}
<YYINITIAL>nil 	{return tok(sym.NIL);}
<YYINITIAL>to  	{return tok(sym.TO);}
<YYINITIAL>end 	{return tok(sym.END);}
<YYINITIAL>function  {return tok(sym.FUNCTION);}
<YYINITIAL>var 	{return tok(sym.VAR);}
<YYINITIAL>type	{return tok(sym.TYPE);}
<YYINITIAL>array  {return tok(sym.ARRAY);}
<YYINITIAL>if 	{return tok(sym.IF);}
<YYINITIAL>then {return tok(sym.THEN);}
<YYINITIAL>else {return tok(sym.ELSE);}
<YYINITIAL>do  	{return tok(sym.DO);}
<YYINITIAL>of  	{return tok(sym.OF);}

<YYINITIAL>{id} 	{return idTok(sym.ID, yytext());}
<YYINITIAL> {num}	{return intTok(sym.INT, new Integer(Integer.parseInt(yytext())));}

<YYINITIAL> \"		
{
    con_str="";
    str_left=yychar;
	yybegin(STRING);
}
<STRING>\\[\ \t]*(\n|\r\n|\r)	
{

	newline();
	yybegin(STRINGLINES);
}
<STRINGLINES> [\ \t]*(\n|\r\n|\r|\f)
{

	newline();
}
<STRINGLINES> \\	{yybegin(STRING);}
<STRING> \\(r|n|t|"^"[@A-Z_]|[0-9][0-9][0-9]|\"|\\)  
{
    con_str+=transMeaning(yytext());
}
<STRING> [\n|\r\n|\r|\f]	
{
    newline();
	err("Unclosed string constant [" + con_str + "]");
}
<STRING> \\.		
{
	err("illegal escape sequence [" + yytext() + "]");
}

<STRING> \"		
{
	yybegin(YYINITIAL);
	return strTok(str_left,sym.STRING, con_str);
}
<STRING> .		
{	
		con_str+=yytext();
}

<YYINITIAL> /\*		
{
    comm_depth=1;
    comm_left=yychar;
	yybegin(COMMENT);
}
<YYINITIAL> \*/		
{	
    comm_depth=0;
	err("Unclosed comment.");
}
<COMMENT> /\*		
{
	comm_depth++;
}
<COMMENT> \*/		
{
	if ((--comm_depth)==0)
		{
			yybegin(YYINITIAL);
			return commTok(comm_left,sym.COMM);
		}
}
<COMMENT> .		{}
<YYINITIAL> .		
{
    err("error ["+yytext()+"]");
	return errorTok(sym.error, yytext());
}