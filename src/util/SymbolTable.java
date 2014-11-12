package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable<VTYPE> {
	private int curScope = 0;
	private ArrayList<Map<String, VTYPE>> vlist;

	public SymbolTable() {
		vlist = new ArrayList<Map<String, VTYPE>>();
		vlist.add(new HashMap<String, VTYPE>());
	}

	public void enterScope() {
		vlist.add(new HashMap<String, VTYPE>());
		++this.curScope;
	}

	public Map<String, VTYPE> exitScope() {
		return vlist.remove(curScope--);
	}

	public VTYPE add(String name, VTYPE desc) {
		vlist.get(curScope).put(name, desc);
		return desc;
	}

	public VTYPE find(String name) {
		VTYPE desc;
		for (int i = curScope; i >= 0; --i)
			if ((desc = vlist.get(i).get(name)) != null)
				return desc;
		return null;
	}

	public VTYPE wipe(String name) {
		VTYPE desc;
		for (int i = curScope; i >= 0; --i)
			if ((desc = vlist.get(i).get(name)) != null) {
				vlist.get(i).put(name, null);
				return desc;
			}
		return null;

	}

}
