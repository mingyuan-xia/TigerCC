package GUI;

import java.awt.Color;

public class Constants {
	//System configs 
	public final static String  sys_key_color="sys_keycolor";
	public final static String  sys_string_color="sys_stringcolor";
	public final static String  sys_normal_color="sys_normalcolor";
	public final static String  sys_comm_color="sys_commcolor";
	
	public final static String  sys_error_img="sys_errorimg";
	public final static String  sys_warning_img="sys_warningimg";
	public final static String  sys_font="sys_font";
	public final static String  sys_fontsize="sys_fontsize";
	public final static String  sys_keys="sys_keys";
	public final static String  sys_ids="sys_ids";
	
	public final static String  initial_type="initial_type";
	
	//System default value
	public final static Color  key_color=new Color(127, 0, 85);
	public final static Color  comm_color=new Color(63, 127, 95);
	public final static Color  string_color=new Color(40, 0, 255);
	public final static Color  normal_color=Color.BLACK;
	
	public final static String  error_img="imgs/error.gif";
	public final static String  warning_img="imgs/warning.gif";
	public final static String  font="Courier New";
	public final static int  fontsize=13;
	public final static String  keys="comm,string,String,let,in,if,else,then,while,break,of,to,type,array,do,end,var,nil,function";
	public final static String  ids="int,String,Char,public,protected,private,catch,try,void,class,implements,extends,interface,throw,throws,import,package,return,byte,double,float,boolean,abstract,final,static,struct";
	public final static String  initial_types="int,string";

	
	public static final String  configPath="Sys/sys.xml";
	
	public static final String  initial_folder="initial_folder";
	public static String  folder="./";
}
