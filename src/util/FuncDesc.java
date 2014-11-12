package util;

public class FuncDesc {
	public TypeDesc retType;
	public String[] paraNames;
	public TypeDesc[] paraTypes;

	public FuncDesc(TypeDesc retType, String[] paraNames, TypeDesc[] paraTypes) {
		this.retType = retType;
		this.paraNames = paraNames;
		this.paraTypes = paraTypes;
	}

	public static final FuncDesc std_print = new FuncDesc(
			TypeDesc.PRIMITIVE_VOID, new String[] { "s" },
			new TypeDesc[] { TypeDesc.PRIMITIVE_STRING });
	public static final FuncDesc std_printi = new FuncDesc(
			TypeDesc.PRIMITIVE_VOID, new String[] { "s" },
			new TypeDesc[] { TypeDesc.PRIMITIVE_INTEGER });
	public static final FuncDesc std_getchar = new FuncDesc(
			TypeDesc.PRIMITIVE_STRING, new String[] {}, new TypeDesc[] {});
	public static final FuncDesc std_ord = new FuncDesc(
			TypeDesc.PRIMITIVE_INTEGER, new String[] { "s" },
			new TypeDesc[] { TypeDesc.PRIMITIVE_STRING });
	public static final FuncDesc std_chr = new FuncDesc(
			TypeDesc.PRIMITIVE_STRING, new String[] { "c" },
			new TypeDesc[] { TypeDesc.PRIMITIVE_INTEGER });
}
