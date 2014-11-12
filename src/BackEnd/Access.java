package BackEnd;

public abstract class Access {
	public int type;
	public static final int AC_ABSOLUTE = 0;     // 1000
	public static final int AC_REGISTER = 1;     // EAX
	public static final int AC_DIRECT = 2;       // [1000]
	public static final int AC_INDIRECT_REG = 3; // [EAX]
	public static final int AC_INDIRECT_ADDR = 4;// [[1000]]
	public static final int AC_RELATIVE = 5;     // PC+V
	public static final int AC_BASE_REG = 6;     // [EBX+1000]
	public static final int AC_RELATIVE_REG = 7; // [ESI+1000]
	public static final int AC_COMPOUND = 8;     // [EBX+ESI*4+1000]
	public Object o1, o2, o3, o4;
	public Access(int type, Object o1)
	{
		this.type = type;
		this.o1 = o1;
	}
	public Access(int type, Object o1, Object o2)
	{
		this.type = type;
		this.o1 = o1;
		this.o2 = o2;

	}
	public Access(int type, Object o1, Object o2, Object o3)
	{
		this.type = type;
		this.o1 = o1;
		this.o2 = o2;
		this.o3 = o3;
	}
	public Access(int type, Object o1, Object o2, Object o3, Object o4)
	{
		this.type = type;
		this.o1 = o1;
		this.o2 = o2;
		this.o3 = o3;
		this.o4 = o4;
	}
	@Override
	public abstract String toString();
}
