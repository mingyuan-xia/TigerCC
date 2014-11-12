/* JFlex script for tiger by Kenmark
*  2009-12-9
*/
package Parser;
import java_cup.runtime.*;
import Parser.sym;
import util.ErrorMsg;


public class Lexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 65536;
	private final int YY_EOF = 65537;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public Lexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public Lexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Lexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int NESTED_COMMENT = 2;
	private final int STRING_LITERAL = 1;
	private final int yy_state_dtrans[] = {
		0,
		48,
		73
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
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
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
		/* 48 */ YY_NOT_ACCEPT,
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
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
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
		/* 106 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,65538,
"47:9,46,44,47,46,44,47:18,46,47,23,47:3,42,47,28,29,37,35,25,36,34,38,24:10" +
",26,27,40,39,41,47:2,21:26,30,45,31,47,22,47,1,4,14,7,5,12,21,17,16,21,6,9," +
"21,11,8,18,21,2,10,15,13,19,20,21,3,21,32,43,33,47:65410,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,107,
"0,1,2,1:2,3,1,4,1:10,5,6,1,7,8,1:2,9:5,1:6,9:12,10,11,1:11,12,13,14,15,16,1" +
"7,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,4" +
"2,43,44,45,46,47,48,49,50,51,52,53,54,9,55,56")[0];

	private int yy_nxt[][] = unpackFromString(57,48,
"1,2,104:2,105,85,104,62,65,86,104,87,88,104:2,68,70,104:2,89,106,104,3,4,5," +
"6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,61,3,61,3,-1:49,104,97" +
",104:20,-1,104,-1:47,5,-1:62,30,-1:46,31,-1:46,32,-1:49,33,-1,34,-1:45,35,-" +
"1:9,104:22,-1,104,-1:23,1,49:22,50,49:20,-1,63,49:2,-1,49:22,-1,49:20,-1:2," +
"49:2,-1:44,61,-1,61,-1:2,104:7,25,104:14,-1,104,-1:25,51,-1:8,52,-1:3,53,-1" +
":7,54,66,-1:19,69,55,69,-1:39,59,-1:10,104:11,26,104:10,-1,104,-1:47,71,-1:" +
"60,60,-1:11,104:2,91,104:4,27,104:8,92,104:5,-1,104,-1:67,69,56,69,-1:2,104" +
":10,28,29,104:10,-1,104,-1:47,57,-1:24,104:6,36,104:15,-1,104,-1:23,1,58:36" +
",64,67,58:9,-1,104:14,37,104:7,-1,104,-1:24,104:8,38,104:13,-1,104,-1:24,10" +
"4,39,104:20,-1,104,-1:24,104,40,104:20,-1,104,-1:24,104:4,41,104:17,-1,104," +
"-1:24,104:4,42,104:17,-1,104,-1:24,104:10,43,104:11,-1,104,-1:24,104:2,44,1" +
"04:19,-1,104,-1:24,104:5,45,104:16,-1,104,-1:24,104:4,46,104:17,-1,104,-1:2" +
"4,104:10,47,104:11,-1,104,-1:24,104:8,90,104,72,104:11,-1,104,-1:24,104:4,7" +
"4,104:17,-1,104,-1:24,104:15,75,104:6,-1,104,-1:24,104:7,76,104:4,99,104:9," +
"-1,104,-1:24,77,104:21,-1,104,-1:24,104:9,78,104:12,-1,104,-1:24,104:17,79," +
"104:4,-1,104,-1:24,104:4,80,104:17,-1,104,-1:24,81,104:21,-1,104,-1:24,82,1" +
"04:21,-1,104,-1:24,104:8,83,104:13,-1,104,-1:24,104:7,84,104:14,-1,104,-1:2" +
"4,104,93,104:20,-1,104,-1:24,104:4,94,104:17,-1,104,-1:24,104:10,101,104:11" +
",-1,104,-1:24,104:15,95,104:6,-1,104,-1:24,104:13,102,104:8,-1,104,-1:24,10" +
"4:14,103,104:7,-1,104,-1:24,104:15,96,104:6,-1,104,-1:24,104,98,104:20,-1,1" +
"04,-1:24,104:16,100,104:5,-1,104,-1:23");

	public java_cup.runtime.Symbol next_token ()
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
				return null;
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
						{ /* ignore */ }
					case -2:
						break;
					case 1:
						
					case -3:
						break;
					case 2:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -4:
						break;
					case 3:
						{ throw new Error("Illegal character <"+ yytext()+">"); }
					case -5:
						break;
					case 4:
						{ str_literal.setLength(0); yybegin(STRING_LITERAL);    }
					case -6:
						break;
					case 5:
						{ return symbol(sym.INTEGER, yytext()); }
					case -7:
						break;
					case 6:
						{ return symbol(sym.COMMA);       }
					case -8:
						break;
					case 7:
						{ return symbol(sym.COLON);       }
					case -9:
						break;
					case 8:
						{ return symbol(sym.SEMICOLON);   }
					case -10:
						break;
					case 9:
						{ return symbol(sym.LPARENTHESE); }
					case -11:
						break;
					case 10:
						{ return symbol(sym.RPARENTHESE); }
					case -12:
						break;
					case 11:
						{ return symbol(sym.LBRACKET);    }
					case -13:
						break;
					case 12:
						{ return symbol(sym.RBRACKET);    }
					case -14:
						break;
					case 13:
						{ return symbol(sym.LBRACE);      }
					case -15:
						break;
					case 14:
						{ return symbol(sym.RBRACE);      }
					case -16:
						break;
					case 15:
						{ return symbol(sym.DOT);         }
					case -17:
						break;
					case 16:
						{ return symbol(sym.ADD);         }
					case -18:
						break;
					case 17:
						{ return symbol(sym.SUB);         }
					case -19:
						break;
					case 18:
						{ return symbol(sym.MUL);         }
					case -20:
						break;
					case 19:
						{ return symbol(sym.DIV);         }
					case -21:
						break;
					case 20:
						{ return symbol(sym.EQ);          }
					case -22:
						break;
					case 21:
						{ return symbol(sym.LESS);        }
					case -23:
						break;
					case 22:
						{ return symbol(sym.GREATER);     }
					case -24:
						break;
					case 23:
						{ return symbol(sym.AND);         }
					case -25:
						break;
					case 24:
						{ return symbol(sym.OR);          }
					case -26:
						break;
					case 25:
						{ return symbol(sym.DO);       }
					case -27:
						break;
					case 26:
						{ return symbol(sym.OF);       }
					case -28:
						break;
					case 27:
						{ return symbol(sym.TO);       }
					case -29:
						break;
					case 28:
						{ return symbol(sym.IN);       }
					case -30:
						break;
					case 29:
						{ return symbol(sym.IF);       }
					case -31:
						break;
					case 30:
						{ return symbol(sym.ASSIGN);      }
					case -32:
						break;
					case 31:
						{ lexerPanic("Unmatched comment!"); }
					case -33:
						break;
					case 32:
						{ comment_layer = 1; yybegin(NESTED_COMMENT); }
					case -34:
						break;
					case 33:
						{ return symbol(sym.LESSEQ);      }
					case -35:
						break;
					case 34:
						{ return symbol(sym.NEQ);         }
					case -36:
						break;
					case 35:
						{ return symbol(sym.GREATEREQ);   }
					case -37:
						break;
					case 36:
						{ return symbol(sym.END);      }
					case -38:
						break;
					case 37:
						{ return symbol(sym.LET);      }
					case -39:
						break;
					case 38:
						{ return symbol(sym.NIL);      }
					case -40:
						break;
					case 39:
						{ return symbol(sym.FOR);      }
					case -41:
						break;
					case 40:
						{ return symbol(sym.VAR);      }
					case -42:
						break;
					case 41:
						{ return symbol(sym.ELSE);     }
					case -43:
						break;
					case 42:
						{ return symbol(sym.TYPE);     }
					case -44:
						break;
					case 43:
						{ return symbol(sym.THEN);     }
					case -45:
						break;
					case 44:
						{ return symbol(sym.ARRAY);    }
					case -46:
						break;
					case 45:
						{ return symbol(sym.BREAK);    }
					case -47:
						break;
					case 46:
						{ return symbol(sym.WHILE);    }
					case -48:
						break;
					case 47:
						{ return symbol(sym.FUNCTION); }
					case -49:
						break;
					case 49:
						{ str_literal.append( yytext() ); }
					case -50:
						break;
					case 50:
						{ yybegin(YYINITIAL); return symbol(sym.STRING, str_literal.toString()); }
					case -51:
						break;
					case 51:
						{ str_literal.append('\r'); }
					case -52:
						break;
					case 52:
						{ str_literal.append('\n'); }
					case -53:
						break;
					case 53:
						{ str_literal.append('\t'); }
					case -54:
						break;
					case 54:
						{ str_literal.append('\"'); }
					case -55:
						break;
					case 55:
						{ str_literal.append('\\'); }
					case -56:
						break;
					case 56:
						{ /* ignore */ }
					case -57:
						break;
					case 57:
						{ int tmp = Integer.parseInt(yytext().substring(1, 4));
                                    if(tmp>255) lexerPanic("exceed \\ddd"); else str_literal.append((char)tmp);}
					case -58:
						break;
					case 58:
						{ /* ignore */ }
					case -59:
						break;
					case 59:
						{ if(--comment_layer == 0) yybegin(YYINITIAL); }
					case -60:
						break;
					case 60:
						{ ++comment_layer; }
					case -61:
						break;
					case 61:
						{ /* ignore */ }
					case -62:
						break;
					case 62:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -63:
						break;
					case 64:
						{ /* ignore */ }
					case -64:
						break;
					case 65:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -65:
						break;
					case 67:
						{ /* ignore */ }
					case -66:
						break;
					case 68:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -67:
						break;
					case 70:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -68:
						break;
					case 72:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -69:
						break;
					case 74:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -70:
						break;
					case 75:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -71:
						break;
					case 76:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -72:
						break;
					case 77:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -73:
						break;
					case 78:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -74:
						break;
					case 79:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -75:
						break;
					case 80:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -76:
						break;
					case 81:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -77:
						break;
					case 82:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -78:
						break;
					case 83:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -79:
						break;
					case 84:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -80:
						break;
					case 85:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -81:
						break;
					case 86:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -82:
						break;
					case 87:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -83:
						break;
					case 88:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -84:
						break;
					case 89:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -85:
						break;
					case 90:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -86:
						break;
					case 91:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -87:
						break;
					case 92:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -88:
						break;
					case 93:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -89:
						break;
					case 94:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -90:
						break;
					case 95:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -91:
						break;
					case 96:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -92:
						break;
					case 97:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -93:
						break;
					case 98:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -94:
						break;
					case 99:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -95:
						break;
					case 100:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -96:
						break;
					case 101:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -97:
						break;
					case 102:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -98:
						break;
					case 103:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -99:
						break;
					case 104:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -100:
						break;
					case 105:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -101:
						break;
					case 106:
						{ return symbol(sym.IDENTIFIER, yytext()); }
					case -102:
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
