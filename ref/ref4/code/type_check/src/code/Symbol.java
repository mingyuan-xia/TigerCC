package code;

import java.util.HashMap;

/*
 * This class describes the symbol table of a class
 */
public class Symbol{
	public String name;
	public HashMap<String, String> vTable;	//variable table
	public HashMap<String, FormalList> fTable;	//function parameter list table
	public HashMap<String, String> uTable;	//function table
	public Symbol(boolean isEmpty, String s){
		if(isEmpty){
			vTable=null;
			fTable=null;
			uTable=null;
			name = s;
		}else{
			name = s;
			vTable=new HashMap<String, String>(8);
			fTable=new HashMap<String, FormalList>(8);
			uTable=new HashMap<String, String>(8);
		}
	}
}
