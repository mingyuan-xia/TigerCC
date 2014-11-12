package BackEnd;

public abstract class CrtInstr {
	public int op;
	public Access opr0, opr1, opr2, opr3; 
	public CrtInstr(int op, Access opr0, Access opr1, Access opr2, Access opr3)
	{
		this.op = op;
		this.opr0 = opr0;
		this.opr1 = opr1;
		this.opr2 = opr2;
		this.opr3 = opr3;
	}
	public CrtInstr(int op, Access opr0, Access opr1, Access opr2)
	{
		this.op = op;
		this.opr0 = opr0;
		this.opr1 = opr1;
		this.opr2 = opr2;
	}
	public CrtInstr(int op, Access opr0, Access opr1)
	{
		this.op = op;
		this.opr0 = opr0;
		this.opr1 = opr1;
	}
	public CrtInstr(int op, Access opr0)
	{
		this.op = op;
		this.opr0 = opr0;
	}
	public CrtInstr(int op)
	{
		this.op = op;
	}	
	@Override
	abstract public String toString();
}
