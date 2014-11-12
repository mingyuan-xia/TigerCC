package GUI;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.xml.sax.SAXException;

import Parse.sym;


public class Config {

public static HashMap<Integer,KeyValue> keys =new HashMap<Integer,KeyValue>();
public static HashMap<String,IdValue> ids=new HashMap<String,IdValue>();
public static ArrayList<String> types =new ArrayList<String>();

public static Color key_color;
public static Color string_color;
public static Color normal_color;
public static Color comm_color;

public static String font;
public static int fontsize;
public static String error_img;
public static String warning_img;
public static boolean initialed=false;

public static HashMap<String,Integer> symnames1 = new HashMap<String,Integer>();
static {

	symnames1.put("function", new Integer(sym.FUNCTION));
	symnames1.put("else", new Integer(sym.ELSE));
	symnames1.put("nil", new Integer(sym.NIL));
	symnames1.put("do", new Integer(sym.DO));
	symnames1.put("of", new Integer(sym.OF));
	symnames1.put("array", new Integer(sym.ARRAY));
	symnames1.put("type", new Integer(sym.TYPE));
	symnames1.put("for", new Integer(sym.FOR));
	symnames1.put("to", new Integer(sym.TO));
	symnames1.put("in", new Integer(sym.IN));
	symnames1.put("end", new Integer(sym.END));
	symnames1.put("if", new Integer(sym.IF));
	symnames1.put("then", new Integer(sym.THEN));
	symnames1.put("while", new Integer(sym.WHILE));
	symnames1.put("var", new Integer(sym.VAR));
	symnames1.put("break;", new Integer(sym.BREAK));
	symnames1.put("while", new Integer(sym.WHILE));
	symnames1.put("let", new Integer(sym.LET));
	symnames1.put("comm", new Integer(sym.COMM));
	
	symnames1.put("eof", new Integer(sym.EOF));
	symnames1.put("int", new Integer(sym.INT));
	symnames1.put(">", new Integer(sym.GT));
	symnames1.put("/", new Integer(sym.DIVIDE));
	symnames1.put(":", new Integer(sym.COLON));
	symnames1.put(">=", new Integer(sym.GE));
	symnames1.put("error", new Integer(sym.error));
	symnames1.put("<", new Integer(sym.LT));
	symnames1.put("-", new Integer(sym.MINUS));
	symnames1.put("&", new Integer(sym.AND));
	symnames1.put("|", new Integer(sym.OR));
	symnames1.put("*", new Integer(sym.TIMES));
	symnames1.put(",", new Integer(sym.COMMA));
	symnames1.put("<=", new Integer(sym.LE));
	symnames1.put(":=", new Integer(sym.ASSIGN));
	symnames1.put("string", new Integer(sym.STRING));
	symnames1.put("String", new Integer(sym.STRING));
	symnames1.put(".", new Integer(sym.DOT));
	symnames1.put("(", new Integer(sym.LPAREN));
	symnames1.put(")", new Integer(sym.RPAREN));
	symnames1.put(";", new Integer(sym.SEMICOLON));
	symnames1.put("id", new Integer(sym.ID));
	symnames1.put("[", new Integer(sym.LBRACK));
	symnames1.put("]", new Integer(sym.RBRACK));
	symnames1.put("<>", new Integer(sym.NEQ));
	symnames1.put("+", new Integer(sym.PLUS));
	symnames1.put("{", new Integer(sym.LBRACE));
	symnames1.put("}", new Integer(sym.RBRACE));
	symnames1.put("=", new Integer(sym.EQ));
	
}

public static HashMap<Integer,String> symnames2 = new HashMap<Integer,String>();
static {

	symnames2.put(sym.FUNCTION,"FUNCTION");
	symnames2.put(sym.EOF,"EOF");
	symnames2.put(sym.INT,"INT");
	symnames2.put(sym.GT,"GT");
	symnames2.put(sym.DIVIDE,"DIVIDE");
	symnames2.put(sym.COLON,"COLON");
	symnames2.put(sym.ELSE,"ELSE");
	symnames2.put(sym.OR,"OR");
	symnames2.put(sym.NIL,"NIL");
	symnames2.put(sym.DO,"DO");
	symnames2.put(sym.GE,"GE");
	symnames2.put(sym.error,"error");
	symnames2.put(sym.LT,"LT");
	symnames2.put(sym.OF,"OF");
	symnames2.put(sym.MINUS,"MINUS");
	symnames2.put(sym.ARRAY,"ARRAY");
	symnames2.put(sym.TYPE,"TYPE");
	symnames2.put(sym.FOR,"FOR");
	symnames2.put(sym.TO,"TO");
	symnames2.put(sym.TIMES,"TIMES");
	symnames2.put(sym.COMMA,"COMMA");
	symnames2.put(sym.LE,"LE");
	symnames2.put(sym.IN,"IN");
	symnames2.put(sym.END,"END");
	symnames2.put(sym.ASSIGN,"ASSIGN");
	symnames2.put(sym.STRING,"STRING");
	symnames2.put(sym.DOT,"DOT");
	symnames2.put(sym.LPAREN,"LPAREN");
	symnames2.put(sym.RPAREN,"RPAREN");
	symnames2.put(sym.IF,"IF");
	symnames2.put(sym.SEMICOLON,"SEMICOLON");
	symnames2.put(sym.ID,"ID");
	symnames2.put(sym.WHILE,"WHILE");
	symnames2.put(sym.LBRACK,"LBRACK");
	symnames2.put(sym.RBRACK,"RBRACK");
	symnames2.put(sym.NEQ,"NEQ");
	symnames2.put(sym.VAR,"VAR");
	symnames2.put(sym.BREAK,"BREAK");
	symnames2.put(sym.AND,"AND");
	symnames2.put(sym.PLUS,"PLUS");
	symnames2.put(sym.LBRACE,"LBRACE");
	symnames2.put(sym.RBRACE,"RBRACE");
	symnames2.put(sym.LET,"LET");
	symnames2.put(sym.THEN,"THEN");
	symnames2.put(sym.EQ,"EQ");
	symnames2.put(sym.COMM,"COMM");
}

public static HashMap<String,Integer>  keyMap= new HashMap<String,Integer>();
static {

	keyMap.put("function", new Integer(sym.FUNCTION));
	keyMap.put("else", new Integer(sym.ELSE));
	keyMap.put("nil", new Integer(sym.NIL));
	keyMap.put("do", new Integer(sym.DO));
	keyMap.put("of", new Integer(sym.OF));
	keyMap.put("array", new Integer(sym.ARRAY));
	keyMap.put("type", new Integer(sym.TYPE));
	keyMap.put("for", new Integer(sym.FOR));
	keyMap.put("to", new Integer(sym.TO));
	keyMap.put("in", new Integer(sym.IN));
	keyMap.put("end", new Integer(sym.END));
	keyMap.put("if", new Integer(sym.IF));
	keyMap.put("then", new Integer(sym.THEN));
	keyMap.put("while", new Integer(sym.WHILE));
	keyMap.put("var", new Integer(sym.VAR));
	keyMap.put("break;", new Integer(sym.BREAK));
	keyMap.put("while", new Integer(sym.WHILE));
	keyMap.put("let", new Integer(sym.LET));
	keyMap.put("string", new Integer(sym.STRING));
	keyMap.put("String", new Integer(sym.STRING));
	keyMap.put("comm", new Integer(sym.COMM));
	keyMap.put("id", new Integer(sym.ID));
}
/*
 * 配置文件路径
 */
public  Config()
{
}

public static  int getLine(int pos,String text)
 {
    if(pos<=0)return 1;
    int line=1;
    for(int i=0;i<text.length();i++)
    {
 	   if(i>=pos)break;
 	   if(text.charAt(i)=='\n')
 		   line++;
    }
    return line;
 }

public static void initialized(boolean i)
{
	initialed=i;
}
  /*
   * 读取下载页面配置
   */
public static void readConfig()
{
	initialed=true;
	HashMap<String,String> tmp=new HashMap<String,String>();
		try { 
			DocumentBuilderFactory factory = DocumentBuilderFactory 
			.newInstance(); 
			DocumentBuilder builder;		
			builder = factory.newDocumentBuilder();		
			Document document;
		
			document = builder.parse(new File(Constants.configPath));	
			Element rootElement = document.getDocumentElement(); 

			for(int i=1;i<rootElement.getChildNodes().getLength();){
			Node node = rootElement.getChildNodes().item(i); 
			tmp.put(node.getNodeName(),node.getChildNodes().item(0).getNodeValue()); 
			//System.out.println(node.getNodeName() + "="+node.getChildNodes().item(0).getNodeValue()); 
			i+=2;
			}
		 } catch (SAXException e) {
			// TODO 自动生成 catch 块
			 System.out.println("exception:" + e.getMessage());
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			System.out.println("exception:" + e.getMessage());
		} 	catch (ParserConfigurationException e) {
			// TODO 自动生成 catch 块
			System.out.println("exception:" + e.getMessage());
		} 
		ids.clear();
		keys.clear();
		String value="";
		if(tmp.containsKey(Constants.sys_keys))
		  value=tmp.get(Constants.sys_keys);
		else value=Constants.keys;
			ArrayList<String>  list=parseTocken(value,",");
			for(int i=0;i<list.size();i++)
			{
				keys.put(keyMap.get(list.get(i)), queryKeyValue(list.get(i)));
			}

			if(tmp.containsKey(Constants.sys_ids))
				  value=tmp.get(Constants.sys_ids);
			else value=Constants.ids;
			list=parseTocken(value,",");
			for(int i=0;i<list.size();i++)
			{
				ids.put(list.get(i), queryIdValue(list.get(i)));
			}	
			
			if(tmp.containsKey(Constants.sys_key_color))
			{
				key_color=parseColor(tmp.get(Constants.sys_key_color),",");
				if(key_color==null)comm_color=Constants.key_color;
			}else key_color=Constants.key_color;
			
			if(tmp.containsKey(Constants.sys_string_color))
			{
				string_color=parseColor(tmp.get(Constants.sys_string_color),",");
				if(string_color==null)string_color=Constants.string_color;
			}else string_color=Constants.string_color;
			
			if(tmp.containsKey(Constants.sys_normal_color))
			{
				normal_color=parseColor(tmp.get(Constants.sys_normal_color),",");
				if(normal_color==null)normal_color=Constants.normal_color;
			}else normal_color=Constants.normal_color;
			
			if(tmp.containsKey(Constants.sys_comm_color))
			{
				comm_color=parseColor(tmp.get(Constants.sys_comm_color),",");
				if(comm_color==null)comm_color=Constants.comm_color;
			}else comm_color=Constants.comm_color; 
			
			
			if(tmp.containsKey(Constants.sys_font))
			{
				font=tmp.get(Constants.sys_font);
			}else font=Constants.font;
			
			if(tmp.containsKey(Constants.sys_fontsize))
			{
				fontsize=Integer.parseInt(tmp.get(Constants.sys_fontsize));
				if(fontsize<=0)fontsize=Constants.fontsize;
			}else fontsize=Constants.fontsize;
			
			if(tmp.containsKey(Constants.sys_error_img))
			{
				error_img=tmp.get(Constants.sys_error_img);
			}else error_img=Constants.error_img;
			
			if(tmp.containsKey(Constants.sys_warning_img))
			{
				warning_img=tmp.get(Constants.sys_warning_img);
			}else warning_img=Constants.warning_img;
			
			if(tmp.containsKey(Constants.initial_folder))
			{
				Constants.folder=tmp.get(Constants.initial_folder);
			}
			
			if(tmp.containsKey(Constants.initial_type))
			{
				types=parseTocken(tmp.get(Constants.initial_type),",");
			}else types=parseTocken(Constants.initial_types,",");
}
public static Color parseColor(String str,String tok)
{
	String r="",g="",b="";
	StringTokenizer tn = new StringTokenizer(str,tok,false);
	r=tn.nextToken();
	g=tn.nextToken();
	b=tn.nextToken();
	
	int ir=0,ig=0,ib=0;
	ir=Integer.parseInt(r);
	ig=Integer.parseInt(g);
	ib=Integer.parseInt(b);
	
	return new Color(ir,ig,ib);
}
public static ArrayList<String> parseTocken(String str,String tok)
{
	ArrayList<String> list=new ArrayList<String>();
	StringTokenizer tn = new StringTokenizer(str,tok,false);
	while(tn.hasMoreTokens())
	{
		list.add(tn.nextToken());
	}
	return list;
}
public static KeyValue queryKeyValue(String key)
{
	return new KeyValue(key);
}
public static IdValue queryIdValue(String id)
{
	return new IdValue(id);
}
}
class KeyValue
{
	String value="";
	
	KeyValue()
	{
		value="";
	}
	KeyValue(String value)
	{
		this.value=value;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value=value;
	}
	public String toString()
	{
		return value;
	}
}

class IdValue
{
	String value="";
	
	IdValue()
	{
		value="";
	}
	IdValue(String value)
	{
		this.value=value;
	}
	
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value=value;
	}
	public String toString()
	{
		return value;
	}
}