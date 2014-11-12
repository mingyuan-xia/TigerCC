package Parse;
import ErrorMsg.ErrorMsg;


class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

  private int comment_count = 0;
	private StringBuffer str = new StringBuffer();

private void newline() {
  errorMsg.newline(yychar);
}
private void err(int pos, String s) {
  errorMsg.error(pos,s);
}
private void err(String s) {
  err(yychar,s);
}
private java_cup.runtime.Symbol tok(int kind, Object value) {
		return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}
private ErrorMsg errorMsg;
Yylex(java.io.InputStream s,ErrorMsg e) {
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
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int UNDERSTR = 3;
	private final int yy_state_dtrans[] = {
		0,
		83,
		55,
		91
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
		/* 0 */ YY_NOT_ACCEPT,
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
		/* 27 */ YY_NO_ANCHOR,
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
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
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
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"44:9,43,40,44:2,41,44:18,42,44,45,44:3,18,44,4,5,13,11,1,12,10,14,47:10,2,3" +
",16,15,17,44,50,49:26,6,46,7,48,51,44,20,23,33,26,24,31,52,36,35,52,25,28,5" +
"3,30,27,37,52,21,29,34,32,38,39,52,22,52,8,19,9,44:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,124,
"0,1:2,2,1:10,3,4,1,5,6,1:2,7,8,1:4,9,1:6,10:10,11,10:7,1:3,12,1:2,13,1:8,11" +
",14,15,16,17,18,19,20,1,21,22,23,8,24,25,15,26,27,28,29,30,31,32,33,34,35,3" +
"6,37,38,39,40,41,1,42,43,44,45,46,47,48,49,50,51,52,53,54,55,10,56,57,58,59" +
",60,61,10,62,63")[0];

	private int yy_nxt[][] = unpackFromString(64,54,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,121:2,122,101,121,68," +
"74,102,121,103,104,121:2,78,81,121:2,105,123,22,69,23,24,25,26,25,27,25,121" +
",25,70,121:2,-1:69,28,-1:52,29,-1:52,30,-1:55,31,-1,32,-1:51,33,-1:58,121,1" +
"13,121:18,-1:7,114,-1,121,-1,114,121:2,-1:41,75,-1:22,67,-1:36,27,-1:26,121" +
":20,-1:7,114,-1,121,-1,114,121:2,-1:47,44,-1:6,1,72:39,56,72:2,-1,72,57,58," +
"72:7,-1:30,59,-1:3,60,-1:10,61,62,85,87,-1:25,121:7,34,121:12,-1:7,114,-1,1" +
"21,-1,114,121:2,-1:40,75,-1:66,73,-1:14,53,-1:40,72:39,-1,72:2,-1,72,-1:2,7" +
"2:7,-1:20,77,-1:53,121:11,35,121:8,-1:7,114,-1,121,-1,114,121:2,-1:13,54,-1" +
":75,80,-1:38,121:2,107,121:4,36,121:8,108,121:3,-1:7,114,-1,121,-1,114,121:" +
"2,-1:30,99,-1:43,121:10,37,38,121:8,-1:7,114,-1,121,-1,114,121:2,1,52:12,71" +
",76,52:25,79,82,52:12,-1:20,121:6,39,121:13,-1:7,114,-1,121,-1,114,121:2,-1" +
":47,89,-1:26,121:14,40,121:5,-1:7,114,-1,121,-1,114,121:2,-1:6,63:2,-1:38,6" +
"3,-1,63:4,-1:22,121:8,41,121:11,-1:7,114,-1,121,-1,114,121:2,-1:47,64,-1:26" +
",121,42,121:18,-1:7,114,-1,121,-1,114,121:2,1,65:39,79,82,65,66,65:10,-1:20" +
",121,43,121:18,-1:7,114,-1,121,-1,114,121:2,-1:20,121:4,45,121:15,-1:7,114," +
"-1,121,-1,114,121:2,-1:20,121:4,46,121:15,-1:7,114,-1,121,-1,114,121:2,-1:2" +
"0,121:10,47,121:9,-1:7,114,-1,121,-1,114,121:2,-1:20,121:2,48,121:17,-1:7,1" +
"14,-1,121,-1,114,121:2,-1:20,121:5,49,121:14,-1:7,114,-1,121,-1,114,121:2,-" +
"1:20,121:4,50,121:15,-1:7,114,-1,121,-1,114,121:2,-1:20,121:10,51,121:9,-1:" +
"7,114,-1,121,-1,114,121:2,-1:20,121:8,106,121,84,121:9,-1:7,114,-1,121,-1,1" +
"14,121:2,-1:20,121:4,86,121:15,-1:7,114,-1,121,-1,114,121:2,-1:20,121:15,88" +
",121:4,-1:7,114,-1,121,-1,114,121:2,-1:20,121:7,90,121:4,116,121:7,-1:7,114" +
",-1,121,-1,114,121:2,-1:20,92,121:19,-1:7,114,-1,121,-1,114,121:2,-1:20,121" +
":9,93,121:10,-1:7,114,-1,121,-1,114,121:2,-1:20,121:17,94,121:2,-1:7,114,-1" +
",121,-1,114,121:2,-1:20,121:4,95,121:15,-1:7,114,-1,121,-1,114,121:2,-1:20," +
"96,121:19,-1:7,114,-1,121,-1,114,121:2,-1:20,97,121:19,-1:7,114,-1,121,-1,1" +
"14,121:2,-1:20,121:8,98,121:11,-1:7,114,-1,121,-1,114,121:2,-1:20,121:7,100" +
",121:12,-1:7,114,-1,121,-1,114,121:2,-1:20,121,109,121:18,-1:7,114,-1,121,-" +
"1,114,121:2,-1:20,121:4,110,121:15,-1:7,114,-1,121,-1,114,121:2,-1:20,121:1" +
"0,118,121:9,-1:7,114,-1,121,-1,114,121:2,-1:20,121:15,111,121:4,-1:7,114,-1" +
",121,-1,114,121:2,-1:20,121:13,119,121:6,-1:7,114,-1,121,-1,114,121:2,-1:20" +
",121:14,120,121:5,-1:7,114,-1,121,-1,114,121:2,-1:20,121:15,112,121:4,-1:7," +
"114,-1,121,-1,114,121:2,-1:20,121,115,121:18,-1:7,114,-1,121,-1,114,121:2,-" +
"1:20,121:16,117,121:3,-1:7,114,-1,121,-1,114,121:2");

	public java_cup.runtime.Symbol nextToken ()
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
	  if(yy_lexical_state == COMMENT) err("Comment unclosure error!");
    if(yy_lexical_state == STRING) err("String unclosure error!");
    if(yy_lexical_state == UNDERSTR) err("String unclosure error!");
	 	return tok(sym.EOF,null);
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
					case 1:
						
					case -2:
						break;
					case 2:
						{ return tok(sym.COMMA,null); }
					case -3:
						break;
					case 3:
						{ return tok(sym.COLON,null); }
					case -4:
						break;
					case 4:
						{ return tok(sym.SEMICOLON,null); }
					case -5:
						break;
					case 5:
						{ return tok(sym.LPAREN,null); }
					case -6:
						break;
					case 6:
						{ return tok(sym.RPAREN,null); }
					case -7:
						break;
					case 7:
						{ return tok(sym.LBRACK,null); }
					case -8:
						break;
					case 8:
						{ return tok(sym.RBRACK,null); }
					case -9:
						break;
					case 9:
						{ return tok(sym.LBRACE,null); }
					case -10:
						break;
					case 10:
						{ return tok(sym.RBRACE,null); }
					case -11:
						break;
					case 11:
						{ return tok(sym.DOT,null); }
					case -12:
						break;
					case 12:
						{ return tok(sym.PLUS,null); }
					case -13:
						break;
					case 13:
						{ return tok(sym.MINUS,null); }
					case -14:
						break;
					case 14:
						{ return tok(sym.TIMES,null); }
					case -15:
						break;
					case 15:
						{ return tok(sym.DIVIDE,null); }
					case -16:
						break;
					case 16:
						{ return tok(sym.EQ,null); }
					case -17:
						break;
					case 17:
						{ return tok(sym.LT,null); }
					case -18:
						break;
					case 18:
						{ return tok(sym.GT,null); }
					case -19:
						break;
					case 19:
						{ return tok(sym.AND,null); }
					case -20:
						break;
					case 20:
						{ return tok(sym.OR,null); }
					case -21:
						break;
					case 21:
						{
														return tok(sym.ID,yytext());
															}
					case -22:
						break;
					case 22:
						{newline();}
					case -23:
						break;
					case 23:
						{}
					case -24:
						break;
					case 24:
						{}
					case -25:
						break;
					case 25:
						{ err("Illegal character: <" + yytext() + ">"); }
					case -26:
						break;
					case 26:
						{ yybegin(STRING);
									str.setLength(0);
							}
					case -27:
						break;
					case 27:
						{ 
		 return tok(sym.INT,new Integer(yytext()));
						}
					case -28:
						break;
					case 28:
						{ return tok(sym.ASSIGN,null); }
					case -29:
						break;
					case 29:
						{err("Comment error!");}
					case -30:
						break;
					case 30:
						{ yybegin(COMMENT); comment_count = comment_count + 1; }
					case -31:
						break;
					case 31:
						{ return tok(sym.LE,null); }
					case -32:
						break;
					case 32:
						{ return tok(sym.NEQ,null); }
					case -33:
						break;
					case 33:
						{ return tok(sym.GE,null); }
					case -34:
						break;
					case 34:
						{ return tok(sym.DO,null); }
					case -35:
						break;
					case 35:
						{ return tok(sym.OF,null); }
					case -36:
						break;
					case 36:
						{ return tok(sym.TO,null); }
					case -37:
						break;
					case 37:
						{ return tok(sym.IN,null); }
					case -38:
						break;
					case 38:
						{ return tok(sym.IF,null); }
					case -39:
						break;
					case 39:
						{ return tok(sym.END,null); }
					case -40:
						break;
					case 40:
						{ return tok(sym.LET,null); }
					case -41:
						break;
					case 41:
						{ return tok(sym.NIL,null); }
					case -42:
						break;
					case 42:
						{ return tok(sym.FOR,null); }
					case -43:
						break;
					case 43:
						{ return tok(sym.VAR,null); }
					case -44:
						break;
					case 44:
						{err("NOT INTEGER");}
					case -45:
						break;
					case 45:
						{ return tok(sym.ELSE,null); }
					case -46:
						break;
					case 46:
						{ return tok(sym.TYPE,null); }
					case -47:
						break;
					case 47:
						{ return tok(sym.THEN,null); }
					case -48:
						break;
					case 48:
						{ return tok(sym.ARRAY,null); }
					case -49:
						break;
					case 49:
						{ return tok(sym.BREAK,null); }
					case -50:
						break;
					case 50:
						{ return tok(sym.WHILE,null); }
					case -51:
						break;
					case 51:
						{ return tok(sym.FUNCTION,null); }
					case -52:
						break;
					case 52:
						{}
					case -53:
						break;
					case 53:
						{ 
	comment_count = comment_count - 1; 
	if (comment_count == 0) {
    		yybegin(YYINITIAL);
	}
}
					case -54:
						break;
					case 54:
						{ comment_count = comment_count + 1; }
					case -55:
						break;
					case 55:
						{str=str.append(yytext());}
					case -56:
						break;
					case 56:
						{yybegin(YYINITIAL);
							newline();
							err("unclosed String");
							return  tok(sym.STRING,str.toString());
						}
					case -57:
						break;
					case 57:
						{yybegin(YYINITIAL);
						return  tok(sym.STRING,str.toString());
						}
					case -58:
						break;
					case 58:
						{yybegin(UNDERSTR);}
					case -59:
						break;
					case 59:
						{str.append('\n');}
					case -60:
						break;
					case 60:
						{str.append('\t');}
					case -61:
						break;
					case 61:
						{str.append('\"');}
					case -62:
						break;
					case 62:
						{str.append('\\');}
					case -63:
						break;
					case 63:
						{char a=(char)(((int)(yytext().charAt(2))-64));str=str.append(a);}
					case -64:
						break;
					case 64:
						{
                String tmp=yytext().substring(1,4);
								int asc=Integer.parseInt(tmp);
								if(asc>255) err("Wrong ASCII");
								else
								   str=str.append((char)asc);
								}
					case -65:
						break;
					case 65:
						{err("Illegal character: <" + yytext() + ">");}
					case -66:
						break;
					case 66:
						{}
					case -67:
						break;
					case 68:
						{
														return tok(sym.ID,yytext());
															}
					case -68:
						break;
					case 69:
						{newline();}
					case -69:
						break;
					case 70:
						{ err("Illegal character: <" + yytext() + ">"); }
					case -70:
						break;
					case 71:
						{}
					case -71:
						break;
					case 72:
						{str=str.append(yytext());}
					case -72:
						break;
					case 74:
						{
														return tok(sym.ID,yytext());
															}
					case -73:
						break;
					case 75:
						{newline();}
					case -74:
						break;
					case 76:
						{}
					case -75:
						break;
					case 78:
						{
														return tok(sym.ID,yytext());
															}
					case -76:
						break;
					case 79:
						{newline();}
					case -77:
						break;
					case 81:
						{
														return tok(sym.ID,yytext());
															}
					case -78:
						break;
					case 82:
						{newline();}
					case -79:
						break;
					case 84:
						{
														return tok(sym.ID,yytext());
															}
					case -80:
						break;
					case 86:
						{
														return tok(sym.ID,yytext());
															}
					case -81:
						break;
					case 88:
						{
														return tok(sym.ID,yytext());
															}
					case -82:
						break;
					case 90:
						{
														return tok(sym.ID,yytext());
															}
					case -83:
						break;
					case 92:
						{
														return tok(sym.ID,yytext());
															}
					case -84:
						break;
					case 93:
						{
														return tok(sym.ID,yytext());
															}
					case -85:
						break;
					case 94:
						{
														return tok(sym.ID,yytext());
															}
					case -86:
						break;
					case 95:
						{
														return tok(sym.ID,yytext());
															}
					case -87:
						break;
					case 96:
						{
														return tok(sym.ID,yytext());
															}
					case -88:
						break;
					case 97:
						{
														return tok(sym.ID,yytext());
															}
					case -89:
						break;
					case 98:
						{
														return tok(sym.ID,yytext());
															}
					case -90:
						break;
					case 99:
						{
														return tok(sym.ID,yytext());
															}
					case -91:
						break;
					case 100:
						{
														return tok(sym.ID,yytext());
															}
					case -92:
						break;
					case 101:
						{
														return tok(sym.ID,yytext());
															}
					case -93:
						break;
					case 102:
						{
														return tok(sym.ID,yytext());
															}
					case -94:
						break;
					case 103:
						{
														return tok(sym.ID,yytext());
															}
					case -95:
						break;
					case 104:
						{
														return tok(sym.ID,yytext());
															}
					case -96:
						break;
					case 105:
						{
														return tok(sym.ID,yytext());
															}
					case -97:
						break;
					case 106:
						{
														return tok(sym.ID,yytext());
															}
					case -98:
						break;
					case 107:
						{
														return tok(sym.ID,yytext());
															}
					case -99:
						break;
					case 108:
						{
														return tok(sym.ID,yytext());
															}
					case -100:
						break;
					case 109:
						{
														return tok(sym.ID,yytext());
															}
					case -101:
						break;
					case 110:
						{
														return tok(sym.ID,yytext());
															}
					case -102:
						break;
					case 111:
						{
														return tok(sym.ID,yytext());
															}
					case -103:
						break;
					case 112:
						{
														return tok(sym.ID,yytext());
															}
					case -104:
						break;
					case 113:
						{
														return tok(sym.ID,yytext());
															}
					case -105:
						break;
					case 114:
						{
														return tok(sym.ID,yytext());
															}
					case -106:
						break;
					case 115:
						{
														return tok(sym.ID,yytext());
															}
					case -107:
						break;
					case 116:
						{
														return tok(sym.ID,yytext());
															}
					case -108:
						break;
					case 117:
						{
														return tok(sym.ID,yytext());
															}
					case -109:
						break;
					case 118:
						{
														return tok(sym.ID,yytext());
															}
					case -110:
						break;
					case 119:
						{
														return tok(sym.ID,yytext());
															}
					case -111:
						break;
					case 120:
						{
														return tok(sym.ID,yytext());
															}
					case -112:
						break;
					case 121:
						{
														return tok(sym.ID,yytext());
															}
					case -113:
						break;
					case 122:
						{
														return tok(sym.ID,yytext());
															}
					case -114:
						break;
					case 123:
						{
														return tok(sym.ID,yytext());
															}
					case -115:
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
