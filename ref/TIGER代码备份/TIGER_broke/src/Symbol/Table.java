package Symbol;

class Binder< T > {
	T value;			//stored value
	Symbol prevtop;			//previous symbol(key) in the stack
	Binder tail;			//next binder in the hash table binder list
	Binder( T v, Symbol p, Binder t) {
		value = v; prevtop = p; tail = t;
	}
}

/**
 * The Table class is similar to java.util.Dictionary, except that
 * each key must be a Symbol and there is a scope mechanism.
 */
public class Table< T > {
	private java.util.Dictionary < Symbol,Binder<T> >dict = new java.util.Hashtable<Symbol,Binder<T> >();
	private Symbol top;
	private Binder<T> marks;			//stack to reserve top of previous scope 

	public Table(){}

 /**
  * Gets the object associated with the specified symbol in the Table.
  */
	public T get( Symbol key ) {
		Binder<T> e =  dict.get (key);
		if (e == null) return null;
		else return e.value;
	}	

 /**
  * Puts the specified value into the Table, bound to the specified Symbol.
  */
	public void put(Symbol key, T value) {
		dict.put(key, new Binder<T>(value, top, dict.get(key)));
		top = key;
	}

 /**
  * Remembers the current state of the Table.
  */
	public void beginScope() { marks = new Binder<T>(null,top,marks); top=null; }

 /** 
  * Restores the table to what it was at the most recent beginScope
  *	that has not already been ended.
  */
	public void endScope() {
		while ( top != null ) {
			Binder e = (Binder)dict.get(top);
			if (e.tail!=null) dict.put(top,e.tail);
			else dict.remove(top);
			top = e.prevtop;
		}
		top = marks.prevtop;		//resume top of previous scope
		marks = marks.tail;			//pop mark of previous scope from stack 
	}
  /**
   * Returns an enumeration of the Table's symbols.
   */
	public java.util.Enumeration<Symbol> keys() {return dict.keys();}
}

