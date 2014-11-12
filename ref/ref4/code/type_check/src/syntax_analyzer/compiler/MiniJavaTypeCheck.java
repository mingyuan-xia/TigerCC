package syntax_analyzer.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.widgets.Text;

import syntax_analyzer.compiler.model.ASTRoot;
import syntax_analyzer.compiler.visitor.MiniJavaCheckVisitor;
import syntax_analyzer.compiler.visitor.MiniJavaCheckVisitorImp;

import code.Symbol;

/*
 * this class mainly do the type checking
 */
public class MiniJavaTypeCheck {

	public ASTRoot root;	//AST根节点
	Text errorText;	//显示出错信息
	String fileName;
	public static Vector<String> fileContent = new Vector<String>();	//source code to be parsed and checked
	public static HashMap<String, String> cTable = new HashMap<String, String>(8);	//basic type and class symbol table 
	public static HashMap<String, Symbol> cSymbolTable = new HashMap<String, Symbol>(8);	//class symbol table 
	public static HashMap<String, String> extendTable = new HashMap<String, String>(8);		//describes the extends relations between class
	public static void initTable(){
		cTable.put("boolean","basic");
		cTable.put("int","basic");
		cTable.put("any","basic");
		cTable.put("int []","basic");
	}
	/**
	 * 
	 * @param fileName	source code filename
	 * @param root	AST root
	 * @param errorText	show error message
	 */
	public MiniJavaTypeCheck(String fileName,ASTRoot root,Text errorText){
		this.root=root;
		this.errorText=errorText;
		this.fileName=fileName;
	}
	private void reset(){
		fileContent.clear();
		cTable.clear();
		cSymbolTable.clear();
		extendTable.clear();
		initTable();

		String src = "\t";
		String des = "        ";
		String noTable=null;
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			String  temp = br.readLine();
			while(temp!=null){
				noTable = temp.replaceAll(src, des);
				//将\t替换为空格
		//		noTable = temp;
				fileContent.add(noTable);
				temp = br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	public void doTypeChecking(){
		System.out.println("type checking");
		reset();
		MiniJavaCheckVisitor v2 = new MiniJavaCheckVisitorImp(errorText);
		/*
		 * visit ast three times
		 * 第一次构建所有的类的声明，存储在符号表中。
		 * 第二次检查并构建类中的变量和方法声明，包括方法的参数声明。
		 * 第三次检查并构建类中的每个方法，对方法中参数的数目、类型，返回值以及方法中的每个expression和statement进行类型检查。

		 */
		root.acceptTypeCheck(v2,null,0);
		root.acceptTypeCheck(v2,null,1);
		root.acceptTypeCheck(v2,null,2);
	}

}
