package code;
import java.util.*;

/*
 * this class describes the parameter number and types of a method
 */
public class FormalList {
	public int size;
	public Vector<String> types;
	public FormalList(){
		size=0;
		types = new Vector<String>();
	}
	public void add(String s){
		size++;
		types.add(s);
	}
	public int size(){

		return size;
	}
}


