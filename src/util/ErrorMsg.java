package util;

public class ErrorMsg {
	static public ErrorMsg instance = new ErrorMsg();

	private ErrorMsg() {
		// singleton
	}

	public void errChk(boolean cond, String msg) {
		if (!cond)
			err(msg);
	}

	public void err(String msg) {
		System.err.println("error : " + msg);
		System.exit(1);
	}
}
