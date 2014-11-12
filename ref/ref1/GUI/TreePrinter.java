package GUI;

import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

import java_cup.runtime.Symbol;
import Parse.sym;

public class TreePrinter implements resPrinter {
	Stack<Result> result = new Stack<Result>();

	Stack<DefaultMutableTreeNode> nodes = new Stack<DefaultMutableTreeNode>();

	// 
	public void popRe(int i) {
		// TODO Auto-generated method stub
		for (int j = 0; j < i && j < result.size(); j++)
			result.pop();
	}

	public void pushRe(Symbol s) {
		// TODO Auto-generated method stub
		if(s.getValue()==null && s.sym==sym.EOF)
			return;
		nodes.add(new DefaultMutableTreeNode(new Result(s)));
	}

	public void reduce(int i, Symbol s) {
		// TODO Auto-generated method stub
		if (i<=0)
			return;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new Result(s));
		int j=nodes.size()-i;
		if(j<0)j=0;
		for (; j < nodes.size(); j++) {
			node.add(nodes.get(j));
		}
		for (j=0; j<i&& nodes.size()>0; j++) {
			nodes.pop();
		}
		if (node == null)
			System.out.println("root null");
		if (node != null) {
			nodes.push(node);

		}
	}

	public void popRe() {
		// TODO Auto-generated method stub
		nodes.pop();
	}

	public void clear() {
		// TODO Auto-generated method stub
		nodes.clear();
	}

	public Object getRoot() {
		// TODO Auto-generated method stub
		return nodes.pop();
	}

	public Stack getStack() {
		// TODO Auto-generated method stub
		return nodes;
	}

}
