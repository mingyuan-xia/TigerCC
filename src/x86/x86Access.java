package x86;

import util.ErrorMsg;
import BackEnd.*;

public class x86Access extends Access {
	public x86Access(String label)
	{
		super(Access.AC_ABSOLUTE, label);
	}
	public x86Access(int v)
	{
		super(Access.AC_ABSOLUTE, new Integer(v));
	}
	public x86Access(int dummy, int addr)
	{
		super(Access.AC_INDIRECT_ADDR, new Integer(addr));
	}
	public x86Access(Register r)
	{
		super(Access.AC_REGISTER, r);
	}
	public x86Access(String s, Register r)
	{
		// TODO : check
		super(Access.AC_INDIRECT_REG, r);
	}
	public x86Access(String s, int addr)
	{
		super(Access.AC_INDIRECT_ADDR, new Integer(addr));
	}
	public x86Access(Register r, int v)
	{
		// TODO : check
		super(Access.AC_BASE_REG, r, new Integer(v));
	}
	public x86Access(Register base, Register r, int v)
	{
		// TODO
		super(Access.AC_ABSOLUTE, null);
		ErrorMsg.instance.err("tobe implemented!");
	}
	@Override
	public String toString() {
		switch (this.type)
		{
		case Access.AC_ABSOLUTE:
			if (this.o1 instanceof Integer)
				return "0"+Integer.toHexString(((Integer)this.o1).intValue())+"h";
			else
				return this.o1.toString();
		case Access.AC_BASE_REG:
			int v = ((Integer)this.o2).intValue();
			if (v > 0)
				return "["+this.o1.toString()+"+0"+Integer.toHexString(v)+"h]";
			else
				return "["+this.o1.toString()+"-0"+Integer.toHexString(-v)+"h]";
		case Access.AC_DIRECT:
			return "["+Integer.toHexString(((Integer)this.o1).intValue())+"]";
		case Access.AC_INDIRECT_REG:
			return "["+((Register)this.o1).toString()+"]";
		case Access.AC_REGISTER:
			return ((Register)this.o1).toString();
		default:
			return null;
		}
	}
	public boolean isMemory()
	{
		return (this.type != Access.AC_REGISTER && this.type != Access.AC_ABSOLUTE);
	}
	public boolean isRegister()
	{
		return this.type == Access.AC_REGISTER;
	}
	public boolean isAbsolute()
	{
		return this.type == Access.AC_ABSOLUTE;
	}
}
