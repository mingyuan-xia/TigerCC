package IR;

public class Constant extends ScopeItem {
	public Object data;
	public Constant(int v) {
		super(null);
		this.data = new Integer(v);
	}

	public Constant(String s) {
		super(null);
		this.data = s;
	}

	@Override
	public String toString() {
		if (data instanceof Integer)
			return "#"+data.toString();
		else
			return "#";
	}

}
