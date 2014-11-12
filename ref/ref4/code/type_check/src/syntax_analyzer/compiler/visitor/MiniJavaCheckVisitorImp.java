package syntax_analyzer.compiler.visitor;

import java.util.*;

import org.eclipse.swt.widgets.Text;

import syntax_analyzer.compiler.MiniJavaParser;
import syntax_analyzer.compiler.MiniJavaTypeCheck;
import syntax_analyzer.compiler.visitor.*;
import code.*;
import syntax_analyzer.compiler.model.ASTAndExpression;
import syntax_analyzer.compiler.model.ASTArrayAssignmentStatement;
import syntax_analyzer.compiler.model.ASTArrayLength;
import syntax_analyzer.compiler.model.ASTArrayLookUp;
import syntax_analyzer.compiler.model.ASTArrayType;
import syntax_analyzer.compiler.model.ASTAssignmentStatement;
import syntax_analyzer.compiler.model.ASTBlock;
import syntax_analyzer.compiler.model.ASTBooleanType;
import syntax_analyzer.compiler.model.ASTClassDeclaration;
import syntax_analyzer.compiler.model.ASTClassExtendsDeclaration;
import syntax_analyzer.compiler.model.ASTCompareExpression;
import syntax_analyzer.compiler.model.ASTExpression;
import syntax_analyzer.compiler.model.ASTFalseLiteral;
import syntax_analyzer.compiler.model.ASTFormalParameter;
import syntax_analyzer.compiler.model.ASTIdentifier;
import syntax_analyzer.compiler.model.ASTIfStatement;
import syntax_analyzer.compiler.model.ASTIntegerLiteral;
import syntax_analyzer.compiler.model.ASTIntegerType;
import syntax_analyzer.compiler.model.ASTMainClass;
import syntax_analyzer.compiler.model.ASTMainMethodDeclaration;
import syntax_analyzer.compiler.model.ASTMessageSend;
import syntax_analyzer.compiler.model.ASTMethodDeclaration;
import syntax_analyzer.compiler.model.ASTMinusExpression;
import syntax_analyzer.compiler.model.ASTNotExpression;
import syntax_analyzer.compiler.model.ASTPlusExpression;
import syntax_analyzer.compiler.model.ASTPrintStatement;
import syntax_analyzer.compiler.model.ASTRoot;
import syntax_analyzer.compiler.model.ASTStatement;
import syntax_analyzer.compiler.model.ASTTimesExpression;
import syntax_analyzer.compiler.model.ASTTrueLiteral;
import syntax_analyzer.compiler.model.ASTType;
import syntax_analyzer.compiler.model.ASTVariableDeclaration;
import syntax_analyzer.compiler.model.ASTWhileStatement;
import syntax_analyzer.compiler.model.Node;
import syntax_analyzer.compiler.model.SimpleNode;

public class MiniJavaCheckVisitorImp implements MiniJavaCheckVisitor {
	public static HashMap<String, String> lTable = new HashMap<String, String>(8);
	public boolean inLocal=false;
	public boolean checkLocal = false;
	private static Text errorText;
	public MiniJavaCheckVisitorImp(Text errorText){
		this.errorText = errorText;
	}

	/**
	 * show error message in GUI pane
	 * @param s		error message
	 * @param n		node that cause this error, contains the position information in source file
	 */
	public static void showErrorMessage(String s, Node n){
		errorText.append(s+"\n");
		SimpleNode sn = (SimpleNode)n;
//		errorText.append("\t"+n.getFileContent()+"\n");
		errorText.append("At line "+sn.getBeginLine()+", column "+sn.getBeginColumn()+"\n"+"\n");
	}
	
	/**
	 * get type of global identifier
	 * @param id 	identifier to be found
	 * @param thisClass		the class that may contain this identifier
	 * @return		the type of this identifier and "" if identifier not found
	 */
	public static String getIdType(String id, Symbol thisClass){
		String idType = lTable.get(id);
		if(idType==null)
			idType = thisClass.vTable.get(id);
		if(idType==null){
			idType = thisClass.uTable.get(id);
		}
	
		return idType;
	}
	
	/**
	 * judge if class sub is a subclass of class sup
	 * @param sub	class that may be a subclass
	 * @param sup	class that may be a superclass
	 * @return	true if clsss sub is a subclass of class sup
	 */
	public static boolean isKindOf(String sub, String sup){
		String s;
		while(true){
			s = MiniJavaTypeCheck.extendTable.get(sub);
			if(s!=null){
				if(s.equals(sup)){
					return true;
				}else{
					sub = s;
				}
			}else{
				return false;
			}
		}
	}
	
	/**
	 * visit node and do type checking
	 */
	public String visit(SimpleNode node, Symbol thisClass, int times) {

		for (int i = 0; i < node.children.length; i++) {
			node.children[i].acceptTypeCheck(this, thisClass, times);
		}
		return null;
	}

	public String visit(ASTRoot node, Symbol thisClass, int times) {

		if(times==2)
			checkLocal=true;
		else if(times==1)
			checkLocal=false;
		if (times == 0 || times == 1 || times==2) {
			for (int i = 0; i < node.children.length; i++) {
				node.children[i].acceptTypeCheck(this, null,times);
			}
		}
		return "";
	}

	public String visit(ASTMainClass node, Symbol thisClass, int times) {
		inLocal=false;
		String type = node.children[0].getFileContent();
		if (times == 0) {
			Symbol symble = new Symbol(false, type);
			if(MiniJavaTypeCheck.cTable.put(type, "class")==null)
				MiniJavaTypeCheck.cSymbolTable.put(type, symble);
			else{
				showErrorMessage("a class with same name already exists", node);
				return null;
			}
			return type;
		} 
		if(times==2)
			node.children[1].acceptTypeCheck(this, MiniJavaTypeCheck.cSymbolTable.get(type), times);
		else if(times==1){
			node.children[1].acceptTypeCheck(this, MiniJavaTypeCheck.cSymbolTable.get(type), times);
		}
		return null;
	}

	public String visit(ASTMainMethodDeclaration node, Symbol thisClass,
			int times) {
		inLocal=true;
		if (times == 0) {
			return "";
		} else if (times == 2) {
			//main方法
			FormalList fl = new FormalList();
			fl.add("String []");
			thisClass.fTable.put("Main", fl);
			//参数
			lTable.clear();
			lTable.put(node.children[0].getFileContent(),"String []");
			for (int i = 1; i < node.children.length; i++) {
				node.children[i].acceptTypeCheck(this, thisClass, times);
			}
		}else if (times == 1) {
			//main方法
			FormalList fl = new FormalList();
			fl.add("String []");
			thisClass.fTable.put("Main", fl);
			//参数
			lTable.clear();
			lTable.put(node.children[0].getFileContent(),"String []");
		}
		return null;
	}

	public String visit(ASTClassDeclaration node, Symbol thisClass, int times) {
		inLocal=false;
		String type = node.children[0].getFileContent();
		if (times == 0) {
			Symbol symble = new Symbol(false, type);
			if(MiniJavaTypeCheck.cTable.put(type, "class")==null){
				MiniJavaTypeCheck.cSymbolTable.put(type, symble);
			}
			else{
				showErrorMessage("a class with same name already exists", node);
				return null;
			}
			return type;
		} else if (times == 2) {
			for (int i = 1; i < node.children.length; i++) {
				node.children[i].acceptTypeCheck(this, MiniJavaTypeCheck.cSymbolTable
						.get(type), times);
			}
		}else if (times==1){
			for (int i = 1; i < node.children.length; i++) {
				node.children[i].acceptTypeCheck(this, MiniJavaTypeCheck.cSymbolTable
						.get(type), times);
			}
		}
		return null;
	}

	public String visit(ASTClassExtendsDeclaration node, Symbol thisClass,
			int times) {
		inLocal=false;
		String subType = node.children[0].getFileContent();
		String supType = node.children[1].getFileContent();
		if (times == 0) {
			Symbol symble = new Symbol(false, subType);
			if(MiniJavaTypeCheck.cTable.put(subType, "class")==null){
				MiniJavaTypeCheck.cSymbolTable.put(subType, symble);
				MiniJavaTypeCheck.extendTable.put(subType, supType);
			}
			else{
				showErrorMessage("a class with same name already exists", node);
				return null;
			}
			return subType;
		} else if (times == 2) {
			for (int i = 2; i < node.children.length; i++) {
				node.children[i].acceptTypeCheck(this, MiniJavaTypeCheck.cSymbolTable
						.get(subType), times);
			}
		}
		else if (times == 1) {
			for (int i = 2; i < node.children.length; i++) {
				node.children[i].acceptTypeCheck(this, MiniJavaTypeCheck.cSymbolTable
						.get(subType), times);
			}
		}
		return null;
	}

	public String visit(ASTVariableDeclaration node, Symbol thisClass, int times) {
		if(times==1 || times==2){
			String type = node.children[0].acceptTypeCheck(this, thisClass, times);
			String name = node.children[1].getFileContent();

			//check variables in class methods
			if(inLocal&&checkLocal){
				if(lTable.get(name)!=null){
					showErrorMessage("variable with the same name already exists", node);
					return null;
				}else
					lTable.put(name, type);
			}
			//class member variable check
			else if(!inLocal && !checkLocal){
				if(thisClass.vTable.get(name)!=null){
					showErrorMessage("variable with the same name already exists", node);
					return null;
				}else
					thisClass.vTable.put(name, type);
			}
			return "";
		}
		return "";
	}

	public String visit(ASTMethodDeclaration node, Symbol thisClass, int times) {
		inLocal=true;
		if(times==1){
			FormalList fl = new FormalList();
			fl.add(node.children[0].acceptTypeCheck(this, thisClass, times));
			String paraType=null;
			for(int i=2;i<node.children.length;i++){
				if(node.children[i] instanceof ASTFormalParameter){
					paraType = node.children[i].acceptTypeCheck(this, thisClass, times);
					fl.add(paraType);
				}
			}
			String functionName = node.children[1].getFileContent();
			if(thisClass.fTable.get(functionName)==null)
				thisClass.fTable.put(functionName, fl);
			else
				showErrorMessage("a function with same name already exists", node);
		}else if(times==2){
			lTable.clear();
			int length = node.children.length;
			for(int i=2;i<length-1;i++){
				if(node.children[i] instanceof ASTFormalParameter){
					node.children[i].acceptTypeCheck(this, thisClass, times);
				}
				node.children[i].acceptTypeCheck(this, thisClass, times);
			}
			String sigType = node.children[0].acceptTypeCheck(this, thisClass, times);
			String returnType = node.children[length-1].acceptTypeCheck(this, thisClass, times);
			if(!(returnType.equals(sigType) || isKindOf(returnType, sigType)) ){
				showErrorMessage("incompatiable return type",node);
				return "";
			}else{
				return returnType;
			}
		}
		return null;
	}

	public String visit(ASTFormalParameter node, Symbol thisClass, int times) {
		if(times==1){
			String type = node.children[0].acceptTypeCheck(this, thisClass, times);
			return type;
		}else if(times==2){
			String type = node.children[0].acceptTypeCheck(this, thisClass, times);
			String name = node.children[1].getFileContent();
			lTable.put(name, type);
		}
		return "";
	}

	public String visit(ASTType node, Symbol thisClass, int times) {
		String s = node.children[0].getFileContent();
		if(s.equals("int[]"))
			s="int []";
		return s;
	}

	public String visit(ASTArrayType node, Symbol thisClass, int times) {
		return "int []";
	}

	public String visit(ASTBooleanType node, Symbol thisClass, int times) {
		return "boolean";
	}

	public String visit(ASTIntegerType node, Symbol thisClass, int times) {
		return "int";
	}

	public String visit(ASTIdentifier node, Symbol thisClass, int times) {
		String idName = node.getFileContent();
		String s = getIdType(idName, thisClass);
		if(s==null){
			String sup = MiniJavaTypeCheck.extendTable.get(thisClass.name);
			while(sup!=null){
				s = MiniJavaTypeCheck.cSymbolTable.get(sup).vTable.get(idName);
				if(s!=null)
					break;
				sup = MiniJavaTypeCheck.extendTable.get(sup);
			}
		}
		if(s==null){
			showErrorMessage("undeclared variavble", node);
			return "";
		}
		else
			return s;
	}

	public String visit(ASTStatement node, Symbol thisClass, int times) {
		node.children[0].acceptTypeCheck(this, thisClass, times);
		return "";
	}

	public String visit(ASTBlock node, Symbol thisClass, int times) {
		return this.visit((SimpleNode) node, thisClass, times);
	}

	public String visit(ASTAssignmentStatement node, Symbol thisClass, int times) {
			String idType = node.children[0].acceptTypeCheck(this, thisClass, times);
			String expType = node.children[1].acceptTypeCheck(this, thisClass, times);

			if(idType.equals(expType))
				return "";
			if(MiniJavaTypeCheck.extendTable.get(idType)!=null){
				if(isKindOf(idType, expType)){
					return "";
				}
			}
			
			showErrorMessage("incompatiable type in assignment", node);
				
			return "";
			
	}

	public String visit(ASTArrayAssignmentStatement node, Symbol thisClass,
			int times) {
			String type = node.children[0].acceptTypeCheck(this, thisClass, times);

			if(!type.equals("int []")){
				showErrorMessage("int []  required as left value", node);
				return null;
			}
			
			type = node.children[1].acceptTypeCheck(this, thisClass, times);
			if(!type.equals("int")){
				showErrorMessage("int required as index", node.children[1]);
				return null;
			}
			
			type = node.children[2].acceptTypeCheck(this, thisClass, times);
			if(!type.equals("int")){
				showErrorMessage("int required as right value", node.children[1]);
				return null;
			}
			return null;			
	}

	public String visit(ASTIfStatement node, Symbol thisClass, int times) {
		String type = node.children[0].acceptTypeCheck(this, thisClass, times);
		if(!type.equals("boolean")){
			showErrorMessage("boolean required for if loop", node);
			return "";	
		}
		node.children[1].acceptTypeCheck(this, thisClass, times);
		node.children[2].acceptTypeCheck(this, thisClass, times);
		return "";
	}

	public String visit(ASTWhileStatement node, Symbol thisClass, int times) {
		String type = node.children[0].acceptTypeCheck(this, thisClass, times);
		if(!type.equals("boolean")){
			showErrorMessage("boolean required for while loop", node.children[0]);
			return "";	
		}
		node.children[1].acceptTypeCheck(this, thisClass, times);
		return "";
	}

	public String visit(ASTPrintStatement node, Symbol thisClass, int times) {
		if(!(node.children[0] instanceof ASTExpression)){
			showErrorMessage(" can only print int", node);
		}
		String type = node.children[0].acceptTypeCheck(this, thisClass, times);
		if(!type.equals("int")){
			showErrorMessage(" can only print int", node);
		}
		return "";
	}

	public String visit(ASTExpression node, Symbol thisClass, int times) {
		if(times==2){
			if(node.children.length==0){
				if(!node.getFileContent().equals("this")){
					showErrorMessage("unknown expression", node);
					return "";
				}
				return thisClass.name;
			}
			// call methods
			//this.expression  or  new class().expression  or  id.expression
			if(node.children[0] instanceof ASTMessageSend){
				return node.children[0].acceptTypeCheck(this, thisClass, times);
			}
			
			if(node.getFileContent().startsWith("new ")){
				//new Class
				if(node.children[0] instanceof ASTIdentifier){
					String newClass = MiniJavaTypeCheck.cTable.get(node.children[0].getFileContent());
					if( newClass.equals("class")){
						return node.children[0].getFileContent();
					}else{
						showErrorMessage("undeclared class type in new expression", node);
						return "";
					}
				//new int[]
				}else if(node.children[0] instanceof ASTExpression){
					String type = node.children[0].acceptTypeCheck(this, thisClass, times);
					return "int []";
				}
				return "";
			}
			
			return node.children[0].acceptTypeCheck(this, thisClass, times);

			
		}
		return "";
		
	}

	public String visit(ASTAndExpression node, Symbol thisClass, int times) {
		if(times==2){
			String pLeft = node.children[0].acceptTypeCheck(this, thisClass, times);
			String pRight = node.children[1].acceptTypeCheck(this, thisClass, times);
	
			if(!pLeft.equals("boolean")  ){
				showErrorMessage("boolean expected  ",node.children[0]);
				return "";
			}
			
			if(!pRight.equals("boolean")  ){
				showErrorMessage("boolean expected ",node.children[1]);
				return "";
			}
		}
		return "boolean";
	}

	public String visit(ASTCompareExpression node, Symbol thisClass, int times) {
		String leftType = node.children[0].acceptTypeCheck(this, thisClass, times);
		String rightType = node.children[1].acceptTypeCheck(this, thisClass, times);

		if(! (leftType.equals("int")&&rightType.equals("int")) ){
			showErrorMessage("need both int for compare expression", node);
			return "";
		}
		return "boolean";
	}

	public String visit(ASTPlusExpression node, Symbol thisClass, int times) {
		String leftType = node.children[0].acceptTypeCheck(this, thisClass, times);
		String rightType = node.children[1].acceptTypeCheck(this, thisClass, times);
	
		if(! (leftType.equals("int")&&rightType.equals("int")) ){
			showErrorMessage("need both int ", node);
			return null;
		}
		return "int";
	}

	public String visit(ASTMinusExpression node, Symbol thisClass, int times) {
		String leftType = node.children[0].acceptTypeCheck(this, thisClass, times);
		String rightType = node.children[1].acceptTypeCheck(this, thisClass, times);
	
		if(! (leftType.equals("int")&&rightType.equals("int")) ){
			showErrorMessage("need both int ", node);
			return "";
		}
		return "int";
	}

	public String visit(ASTTimesExpression node, Symbol thisClass, int times) {
		String leftType = node.children[0].acceptTypeCheck(this, thisClass, times);
		String rightType = node.children[1].acceptTypeCheck(this, thisClass, times);
	
		if(! (leftType.equals("int")&&rightType.equals("int")) ){
			showErrorMessage("need both int ", node);
			return "";
		}
		return "int";
	}

	public String visit(ASTArrayLookUp node, Symbol thisClass, int times) {
		if(times==2){

			String idType = getIdType(node.children[0].getFileContent(), thisClass);

			String indexType = node.children[1].acceptTypeCheck(this, thisClass, times);
		
			if(!(indexType.equals("int"))){
				showErrorMessage("index expected as int", node.children[1]);
				return "";
			}
		
			if(idType.equals("int[]") || idType.equals("int []")){
				return "int";
			}else{
				showErrorMessage("int[] expected", node);
				return "";
			}
		}
		return "";
	}

	public String visit(ASTArrayLength node, Symbol thisClass, int times) {
		if(times==2){
			String idType = node.children[0].acceptTypeCheck(this, thisClass, times);
			if(idType.equals("")){
				showErrorMessage("unknown identifier", node.children[0]);
				return "";
			}
			if(idType.equals("int[]") || idType.equals("int []")){
				return "int";
			}else{
				showErrorMessage("int[] expected", node);
				return "";
			}
		}
		return "";
	}

	public String visit(ASTMessageSend node, Symbol thisClass, int times) {
		if(times==2){
			FormalList paras=null;//参数
			int paraBeginIndex=0;//参数起始索引

			String function = null;	//函数名
			String message = node.getFileContent();//消息
			Symbol targetClass=null;	//符号表
			//this.expression
			if(message.startsWith("this.")){
				targetClass = thisClass;
				function = node.children[0].getFileContent();
				paraBeginIndex=1;
			}else if(message.startsWith("new ")){
				//new class().expression
				targetClass = MiniJavaTypeCheck.cSymbolTable.get(node.children[0].getFileContent());
				if(targetClass==null){
					showErrorMessage("undeclared class", node.children[0]);
					return "";
				}
				function = node.children[1].getFileContent();
				paraBeginIndex = 2;
			}else{
				//id.expression
				String className = node.children[0].acceptTypeCheck(this, thisClass, times);
				if(className.equals("")){
					showErrorMessage("undeclared class type", node.children[0]);
					return "";
				}
				targetClass = MiniJavaTypeCheck.cSymbolTable.get(className);
				function = node.children[1].getFileContent();
				paraBeginIndex = 2;
			}
			
			paras = targetClass.fTable.get(function);
			if(paras==null){
				String sup = MiniJavaTypeCheck.extendTable.get(thisClass.name);
				while(sup!=null){
					paras = MiniJavaTypeCheck.cSymbolTable.get(sup).fTable.get(function);
					if(paras!=null)
						break;
					sup = MiniJavaTypeCheck.extendTable.get(sup);
				}
			}
			if(paras==null){//没有函数
				showErrorMessage("undeclared method in class",node.children[1]);
				return "";
			}
			String paraType=null;
			if(node.children.length-paraBeginIndex!=paras.types.size()-1){
				showErrorMessage("not the same number of parameters", node);
				return "";
			}
			for(int i=paraBeginIndex;i<node.children.length;i++){
				paraType=node.children[i].acceptTypeCheck(this, thisClass, times);

				if(!paraType.equals(paras.types.get(i-paraBeginIndex+1))){//不匹配参数
					if(!isKindOf(paraType,paras.types.get(i-paraBeginIndex+1))){
						showErrorMessage("incompatiable parameter",node.children[i]);
						return "";			
					}
				}
			}
			return paras.types.get(0);
			
		}
		return "";
	}

	public String visit(ASTIntegerLiteral node, Symbol thisClass, int times) {
		return "int";
	}

	public String visit(ASTTrueLiteral node, Symbol thisClass, int times) {
		return "boolean";
	}

	public String visit(ASTFalseLiteral node, Symbol thisClass, int times) {
		return "boolean";
	}

	public String visit(ASTNotExpression node, Symbol thisClass, int times) {
	
		String rightType = node.children[0].acceptTypeCheck(this, thisClass, times);
	
		if(!rightType.equals("boolean") ){
			showErrorMessage("need boolean in NotExpression ", node);
			return "";
		}
		return "boolean";
	}

}
