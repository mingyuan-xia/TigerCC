package GUI;

import java.util.Stack;

import java_cup.runtime.Symbol;

public interface  resPrinter {
public abstract void pushRe(Symbol s);
public abstract void popRe(int i);
public abstract void popRe();
public abstract void reduce(int i,Symbol s);
public abstract void clear();
public abstract Object getRoot();
public abstract Stack getStack();
}
