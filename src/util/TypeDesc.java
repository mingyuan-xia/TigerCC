package util;

import java.util.ArrayList;
import AbsSytree.SynTypeFieldList;

public class TypeDesc {
	private class RecordField {
		public String name;
		public TypeDesc ty;

		public RecordField(String name, TypeDesc ty) {
			this.name = name;
			this.ty = ty;
		}
	}

	private Object tyinfo;
	private static final int ty_unresolved = 0;
	private static final int ty_base = 1;
	private static final int ty_array = 2;
	private static final int ty_record = 3;
	private static final int ty_alias = 4;
	private int tyclass;

	public static final TypeDesc PRIMITIVE_STRING = new TypeDesc("string");
	public static final TypeDesc PRIMITIVE_INTEGER = new TypeDesc("int");
	public static final TypeDesc PRIMITIVE_NIL = new TypeDesc("nil");
	public static final TypeDesc PRIMITIVE_VOID = new TypeDesc("void");

	public TypeDesc() {
		this.tyclass = TypeDesc.ty_unresolved;
		this.tyinfo = null;
	}

	public TypeDesc(TypeDesc alias) {
		this.tyclass = TypeDesc.ty_alias;
		this.tyinfo = alias;
	}

	public TypeDesc(String base) {
		this.tyclass = TypeDesc.ty_base;
		this.tyinfo = base;
	}

	public TypeDesc(TypeDesc arrayeeType, int t) {
		this.tyclass = TypeDesc.ty_array;
		this.tyinfo = arrayeeType;
	}

	public TypeDesc(SynTypeFieldList tflst) {
		this.tyclass = TypeDesc.ty_record;
		this.tyinfo = new ArrayList<RecordField>();
		parseTypeFieldList(tflst);
	}

	private void parseTypeFieldList(SynTypeFieldList tflst) {
		ArrayList<RecordField> members = (ArrayList<RecordField>) this.tyinfo;
		for (int i = 0; i < tflst.tyfields.size(); ++i)
			members.add(new RecordField(tflst.tyfields.get(i).name,
					tflst.tyfields.get(i).exprType));
	}

	@Override
	public String toString() {
		switch (this.tyclass) {
		case TypeDesc.ty_alias:
			return this.tyinfo.toString();
		case TypeDesc.ty_unresolved:
			return null;
		case TypeDesc.ty_base:
			return (String) this.tyinfo;
		case TypeDesc.ty_array:
			return "[]of" + this.tyinfo;
		case TypeDesc.ty_record:
			String retVal = "record{";
			ArrayList<RecordField> members = (ArrayList<RecordField>) this.tyinfo;
			for (int i = 0; i < members.size(); ++i)
				retVal += members.get(i).name + ", ";
			return retVal + "}";
		default:
			return null;
		}
	}

	private TypeDesc getRealType() {
		TypeDesc ty = this;
		while (ty.tyclass == TypeDesc.ty_alias)
			ty = (TypeDesc) ty.tyinfo;
		if (this.tyclass == TypeDesc.ty_alias)
			this.tyinfo = ty;
		return ty;
	}

	public boolean isNil() {
		return (getRealType() == TypeDesc.PRIMITIVE_NIL);
	}

	public boolean isArray() {
		return (getRealType().tyclass == TypeDesc.ty_array);
	}

	public boolean isRecord() {
		return (getRealType().tyclass == TypeDesc.ty_record);
	}

	public boolean isResolved() {
		TypeDesc ty = getRealType();
		return (ty.tyclass != TypeDesc.ty_unresolved);
	}

	public boolean equals(TypeDesc that) {
		TypeDesc l = this.getRealType(), r = that.getRealType();
		if (l == r)
			return true;
		if (l == TypeDesc.PRIMITIVE_NIL)
			return r.isRecord();
		if (r == TypeDesc.PRIMITIVE_NIL)
			return l.isRecord();
		return false;
	}

	public TypeDesc getArrayeeType() {
		TypeDesc ty = getRealType();
		return (TypeDesc) (ty.tyclass == TypeDesc.ty_array ? ty.tyinfo : null);
	}

	public TypeDesc getRecordField(String name) {
		TypeDesc ty = getRealType();
		int idx = ty.getRecordFieldLoc(name);
		if (idx == -1)
			return null;
		else
			return ((ArrayList<RecordField>) ty.tyinfo).get(idx).ty;
	}

	public int getRecordFieldLoc(String name) {
		TypeDesc ty = getRealType();
		if (ty.tyclass != TypeDesc.ty_record)
			return -1;
		ArrayList<RecordField> members = (ArrayList<RecordField>) ty.tyinfo;
		for (int i = 0; i < members.size(); ++i)
			if (members.get(i).name.equals(name))
				return i;
		return -1;
	}

	public boolean setAsAlias(TypeDesc ty) {
		if (this.tyclass != TypeDesc.ty_unresolved)
			ErrorMsg.instance.err("TypeDesc redefined!");
		ty = ty.getRealType();
		if (ty == this)
			return false;
		this.tyclass = TypeDesc.ty_alias;
		this.tyinfo = ty;
		return true;
	}

	public int getRecordFieldCount() {
		TypeDesc ty = getRealType();
		if (ty.tyclass != TypeDesc.ty_record)
			return -1;
		return ((ArrayList<RecordField>) ty.tyinfo).size();
	}
}
