package GUI;

public class Error {
private int line;
private String error;
public Error()
{
	line=0;
	error="";
}
public Error(int l,String e)
{
	line=l;
	error=e;
}
public String getErr()
{
	return error;
}
public int getLine()
{
	return line;
}
public void setErr(String e)
{
	error=e;
}
public void setLine(int l)
{
	line=l;
}
public String toString()
{
	return (error);
}
}
