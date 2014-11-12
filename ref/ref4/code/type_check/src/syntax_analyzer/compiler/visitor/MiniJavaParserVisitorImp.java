package syntax_analyzer.compiler.visitor;

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
import syntax_analyzer.compiler.model.SimpleNode;

public class MiniJavaParserVisitorImp implements MiniJavaParserVisitor {
	public Object visit(SimpleNode node, Object data) {
		System.out.println(node.getName());

		node.dump(" ");

		return data;
	}

	public Object visit(ASTRoot node, Object data) {

		node.dump(" ");
		return data;
	}

	public Object visit(ASTMainClass node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTMainMethodDeclaration node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTClassDeclaration node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTClassExtendsDeclaration node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTVariableDeclaration node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTMethodDeclaration node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTFormalParameter node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTType node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTArrayType node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTBooleanType node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTIntegerType node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTIdentifier node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTStatement node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTBlock node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTAssignmentStatement node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTArrayAssignmentStatement node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTIfStatement node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTWhileStatement node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTPrintStatement node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTExpression node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTAndExpression node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTCompareExpression node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTPlusExpression node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTMinusExpression node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTTimesExpression node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTArrayLookUp node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTArrayLength node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTMessageSend node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTIntegerLiteral node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTTrueLiteral node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTFalseLiteral node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

	public Object visit(ASTNotExpression node, Object data) {
		node.dump(" ");
		System.out.println(node.getName());
		return data;
	}

}
