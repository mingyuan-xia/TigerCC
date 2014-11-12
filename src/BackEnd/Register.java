package BackEnd;

import IR.ScopeItem;

public class Register {
	public boolean locked, synced; 
	private String name;
	public ScopeItem data;
	public void bind(ScopeItem u)
	{
		if (!locked)
			this.data = u;
	}
	public void free()
	{
		if (!locked)
		{
			this.data = null;
			this.synced = false;
		}
	}
	public Register(String name)
	{
		this.name = name;
	}
	public boolean isAvailable()
	{
		return this.data == null && !locked;
	}
	@Override
	public String toString()
	{
		return this.name;
	}
}
