package Parse;
import ErrorMsg.ErrorMsg;


public class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 3;
	private final int STRINGLINES = 2;
	private final int yy_state_dtrans[] = {
		0,
		79,
		86,
		90
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NO_ANCHOR,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NOT_ACCEPT,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NOT_ACCEPT,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"55:9,8,3,55,1,2,55:18,8,55,51,55:3,23,55,16,17,21,19,9,20,18,22,5,6,7,50:7," +
"14,15,26,25,27,55,53,54:26,12,4,13,52,49,55,37,36,43,41,35,28,48,32,33,48,3" +
"8,34,48,40,29,46,48,30,47,39,42,44,31,48,45,48,10,24,11,55:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,121,
"0,1:2,2,3,4,1:5,5,1:6,6,7,1:3,8,9,10,1,11,1:6,12:17,1,13,1:2,14,1:6,15:2,1:" +
"2,16,3,1,17,1,18,19,2,20,21,22,23,24,25,26,27,28,29,22,30,31,32,33,34,35,36" +
",37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,12,56,57,58,59,60" +
",61,62,63,64")[0];

	private int yy_nxt[][] = unpackFromString(65,56,
"1,2,3,64,4,5:3,63,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,6" +
"6,111,114,111,74,115,116,117,118,111,78,119,81,111:2,120,111:4,65,5,26,65:2" +
",111,65,-1:59,64,-1:57,27,-1:55,5:3,-1:42,5,-1:30,28,-1:52,29,-1:54,30,-1:5" +
"9,31,-1,32,-1:53,33,-1:35,111:3,-1:20,111,83,111:12,85,111:8,-1:3,111,-1:7," +
"75,-1:54,111:3,-1:20,111:23,-1:3,111,-1:2,54,55,70,56,69:3,76,54:21,56,54:8" +
",56:2,54:9,69,56,80,54:3,-1:3,70,-1:56,67,-1:3,62,-1:52,111:3,-1:20,34,111:" +
"22,-1:3,111,-1:6,82:3,-1:42,82,-1:8,57,-1:74,60,-1:38,111:3,-1:20,35,111:11" +
",36,111:10,-1:3,111,-1:8,62,-1:50,55,70,-1:4,84,-1:68,61,-1:39,111:3,-1:20," +
"111,37,111:2,112,111:12,95,111:5,-1:3,111,-1,1,51:3,52,68:19,51,68:26,53,68" +
":4,-1:49,56,-1:3,56:2,-1:6,111:3,-1:20,111,38,111:21,-1:3,111,-1:6,56:3,-1:" +
"42,56,-1:10,111:3,-1:20,111:2,39,111:20,-1:3,111,-1:6,111:3,-1:20,111:12,98" +
",111:10,-1:3,111,-1,1,57,71,57,58,-1:3,88,-1:52,111:3,-1:20,111:5,99,111:17" +
",-1:3,111,-1:2,57,71,57,-1:4,88,-1:52,111:3,-1:20,111:11,40,111:11,-1:3,111" +
",-1,1,2,73,64,59:4,2,59:12,72,77,59:33,-1:5,111:3,-1:20,111:19,100,111:3,-1" +
":3,111,-1:6,111:3,-1:20,111:13,41,111:9,-1:3,111,-1:6,111:3,-1:20,111:7,101" +
",111:15,-1:3,111,-1:6,111:3,-1:20,111:2,113,111:20,-1:3,111,-1:6,111:3,-1:2" +
"0,111:18,103,111:4,-1:3,111,-1:6,111:3,-1:20,111:6,42,111:16,-1:3,111,-1:6," +
"111:3,-1:20,111:2,43,111:20,-1:3,111,-1:6,111:3,-1:20,111:15,104,111:7,-1:3" +
",111,-1:6,111:3,-1:20,111:6,105,111:16,-1:3,111,-1:6,111:3,-1:20,111:7,44,1" +
"11:15,-1:3,111,-1:6,111:3,-1:20,111:9,106,111:13,-1:3,111,-1:6,111:3,-1:20," +
"111:12,45,111:10,-1:3,111,-1:6,111:3,-1:20,111:7,46,111:15,-1:3,111,-1:6,11" +
"1:3,-1:20,111:11,108,111:11,-1:3,111,-1:6,111:3,-1:20,111:7,47,111:15,-1:3," +
"111,-1:6,111:3,-1:20,111:10,48,111:12,-1:3,111,-1:6,111:3,-1:20,111:17,49,1" +
"11:5,-1:3,111,-1:6,111:3,-1:20,111:5,109,111:17,-1:3,111,-1:6,111:3,-1:20,1" +
"11,110,111:21,-1:3,111,-1:6,111:3,-1:20,111:12,50,111:10,-1:3,111,-1:6,111:" +
"3,-1:20,111:7,102,111:15,-1:3,111,-1:6,111:3,-1:20,111:9,107,111:13,-1:3,11" +
"1,-1:6,111:3,-1:20,111:4,87,111:18,-1:3,111,-1:6,111:3,-1:20,111:7,89,111:1" +
"5,-1:3,111,-1:6,111:3,-1:20,111:6,91,111:5,92,111:10,-1:3,111,-1:6,111:3,-1" +
":20,111:2,93,111:20,-1:3,111,-1:6,111:3,-1:20,111:2,94,111:20,-1:3,111,-1:6" +
",111:3,-1:20,111:5,96,111:17,-1:3,111,-1:6,111:3,-1:20,111:9,97,111:13,-1:3" +
",111,-1");

	public Token nextToken ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 0:
						{}
					case -2:
						break;
					case 1:
						
					case -3:
						break;
					case 2:
						{}
					case -4:
						break;
					case 3:
						{newline();}
					case -5:
						break;
					case 4:
						{
    err("error ["+yytext()+"]");
	return errorTok(sym.error, yytext());
}
					case -6:
						break;
					case 5:
						{return intTok(sym.INT, new Integer(Integer.parseInt(yytext())));}
					case -7:
						break;
					case 6:
						{return tok(sym.COMMA);}
					case -8:
						break;
					case 7:
						{return tok(sym.LBRACE);}
					case -9:
						break;
					case 8:
						{return tok(sym.RBRACE);}
					case -10:
						break;
					case 9:
						{return tok(sym.LBRACK);}
					case -11:
						break;
					case 10:
						{return tok(sym.RBRACK);}
					case -12:
						break;
					case 11:
						{return tok(sym.COLON);}
					case -13:
						break;
					case 12:
						{return tok(sym.SEMICOLON);}
					case -14:
						break;
					case 13:
						{return tok(sym.LPAREN);}
					case -15:
						break;
					case 14:
						{return tok(sym.RPAREN);}
					case -16:
						break;
					case 15:
						{return tok(sym.DOT);}
					case -17:
						break;
					case 16:
						{return tok(sym.PLUS);}
					case -18:
						break;
					case 17:
						{return tok(sym.MINUS);}
					case -19:
						break;
					case 18:
						{return tok(sym.TIMES);}
					case -20:
						break;
					case 19:
						{return tok(sym.DIVIDE);}
					case -21:
						break;
					case 20:
						{return tok(sym.AND);}
					case -22:
						break;
					case 21:
						{return tok(sym.OR);}
					case -23:
						break;
					case 22:
						{return tok(sym.EQ);}
					case -24:
						break;
					case 23:
						{return tok(sym.LT);}
					case -25:
						break;
					case 24:
						{return tok(sym.GT);}
					case -26:
						break;
					case 25:
						{return idTok(sym.ID, yytext());}
					case -27:
						break;
					case 26:
						{
    con_str="";
    str_left=yychar;
	yybegin(STRING);
}
					case -28:
						break;
					case 28:
						{return tok(sym.ASSIGN);}
					case -29:
						break;
					case 29:
						{	
    comm_depth=0;
	err("Unclosed comment.");
}
					case -30:
						break;
					case 30:
						{
    comm_depth=1;
    comm_left=yychar;
	yybegin(COMMENT);
}
					case -31:
						break;
					case 31:
						{return tok(sym.LE);}
					case -32:
						break;
					case 32:
						{return tok(sym.NEQ);}
					case -33:
						break;
					case 33:
						{return tok(sym.GE);}
					case -34:
						break;
					case 34:
						{return tok(sym.OF);}
					case -35:
						break;
					case 35:
						{return tok(sym.IF);}
					case -36:
						break;
					case 36:
						{return tok(sym.IN);}
					case -37:
						break;
					case 37:
						{return tok(sym.TO);}
					case -38:
						break;
					case 38:
						{return tok(sym.DO);}
					case -39:
						break;
					case 39:
						{return tok(sym.FOR);}
					case -40:
						break;
					case 40:
						{return tok(sym.LET);}
					case -41:
						break;
					case 41:
						{return tok(sym.END);}
					case -42:
						break;
					case 42:
						{return tok(sym.NIL);}
					case -43:
						break;
					case 43:
						{return tok(sym.VAR);}
					case -44:
						break;
					case 44:
						{return tok(sym.ELSE);}
					case -45:
						break;
					case 45:
						{return tok(sym.THEN);}
					case -46:
						break;
					case 46:
						{return tok(sym.TYPE);}
					case -47:
						break;
					case 47:
						{return tok(sym.WHILE);}
					case -48:
						break;
					case 48:
						{return tok(sym.BREAK);}
					case -49:
						break;
					case 49:
						{return tok(sym.ARRAY);}
					case -50:
						break;
					case 50:
						{return tok(sym.FUNCTION);}
					case -51:
						break;
					case 51:
						{
    newline();
	err("Unclosed string constant [" + con_str + "]");
}
					case -52:
						break;
					case 52:
						{	
		con_str+=yytext();
}
					case -53:
						break;
					case 53:
						{
	yybegin(YYINITIAL);
	return strTok(str_left,sym.STRING, con_str);
}
					case -54:
						break;
					case 54:
						{
	err("illegal escape sequence [" + yytext() + "]");
}
					case -55:
						break;
					case 55:
						{

	newline();
	yybegin(STRINGLINES);
}
					case -56:
						break;
					case 56:
						{
    con_str+=transMeaning(yytext());
}
					case -57:
						break;
					case 57:
						{

	newline();
}
					case -58:
						break;
					case 58:
						{yybegin(STRING);}
					case -59:
						break;
					case 59:
						{}
					case -60:
						break;
					case 60:
						{
	if ((--comm_depth)==0)
		{
			yybegin(YYINITIAL);
			return commTok(comm_left,sym.COMM);
		}
}
					case -61:
						break;
					case 61:
						{
	comm_depth++;
}
					case -62:
						break;
					case 62:
						{}
					case -63:
						break;
					case 63:
						{}
					case -64:
						break;
					case 64:
						{newline();}
					case -65:
						break;
					case 65:
						{
    err("error ["+yytext()+"]");
	return errorTok(sym.error, yytext());
}
					case -66:
						break;
					case 66:
						{return idTok(sym.ID, yytext());}
					case -67:
						break;
					case 68:
						{	
		con_str+=yytext();
}
					case -68:
						break;
					case 69:
						{
	err("illegal escape sequence [" + yytext() + "]");
}
					case -69:
						break;
					case 70:
						{

	newline();
	yybegin(STRINGLINES);
}
					case -70:
						break;
					case 71:
						{

	newline();
}
					case -71:
						break;
					case 72:
						{}
					case -72:
						break;
					case 73:
						{newline();}
					case -73:
						break;
					case 74:
						{return idTok(sym.ID, yytext());}
					case -74:
						break;
					case 76:
						{
	err("illegal escape sequence [" + yytext() + "]");
}
					case -75:
						break;
					case 77:
						{}
					case -76:
						break;
					case 78:
						{return idTok(sym.ID, yytext());}
					case -77:
						break;
					case 80:
						{
	err("illegal escape sequence [" + yytext() + "]");
}
					case -78:
						break;
					case 81:
						{return idTok(sym.ID, yytext());}
					case -79:
						break;
					case 83:
						{return idTok(sym.ID, yytext());}
					case -80:
						break;
					case 85:
						{return idTok(sym.ID, yytext());}
					case -81:
						break;
					case 87:
						{return idTok(sym.ID, yytext());}
					case -82:
						break;
					case 89:
						{return idTok(sym.ID, yytext());}
					case -83:
						break;
					case 91:
						{return idTok(sym.ID, yytext());}
					case -84:
						break;
					case 92:
						{return idTok(sym.ID, yytext());}
					case -85:
						break;
					case 93:
						{return idTok(sym.ID, yytext());}
					case -86:
						break;
					case 94:
						{return idTok(sym.ID, yytext());}
					case -87:
						break;
					case 95:
						{return idTok(sym.ID, yytext());}
					case -88:
						break;
					case 96:
						{return idTok(sym.ID, yytext());}
					case -89:
						break;
					case 97:
						{return idTok(sym.ID, yytext());}
					case -90:
						break;
					case 98:
						{return idTok(sym.ID, yytext());}
					case -91:
						break;
					case 99:
						{return idTok(sym.ID, yytext());}
					case -92:
						break;
					case 100:
						{return idTok(sym.ID, yytext());}
					case -93:
						break;
					case 101:
						{return idTok(sym.ID, yytext());}
					case -94:
						break;
					case 102:
						{return idTok(sym.ID, yytext());}
					case -95:
						break;
					case 103:
						{return idTok(sym.ID, yytext());}
					case -96:
						break;
					case 104:
						{return idTok(sym.ID, yytext());}
					case -97:
						break;
					case 105:
						{return idTok(sym.ID, yytext());}
					case -98:
						break;
					case 106:
						{return idTok(sym.ID, yytext());}
					case -99:
						break;
					case 107:
						{return idTok(sym.ID, yytext());}
					case -100:
						break;
					case 108:
						{return idTok(sym.ID, yytext());}
					case -101:
						break;
					case 109:
						{return idTok(sym.ID, yytext());}
					case -102:
						break;
					case 110:
						{return idTok(sym.ID, yytext());}
					case -103:
						break;
					case 111:
						{return idTok(sym.ID, yytext());}
					case -104:
						break;
					case 112:
						{return idTok(sym.ID, yytext());}
					case -105:
						break;
					case 113:
						{return idTok(sym.ID, yytext());}
					case -106:
						break;
					case 114:
						{return idTok(sym.ID, yytext());}
					case -107:
						break;
					case 115:
						{return idTok(sym.ID, yytext());}
					case -108:
						break;
					case 116:
						{return idTok(sym.ID, yytext());}
					case -109:
						break;
					case 117:
						{return idTok(sym.ID, yytext());}
					case -110:
						break;
					case 118:
						{return idTok(sym.ID, yytext());}
					case -111:
						break;
					case 119:
						{return idTok(sym.ID, yytext());}
					case -112:
						break;
					case 120:
						{return idTok(sym.ID, yytext());}
					case -113:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
