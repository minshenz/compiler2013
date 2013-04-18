package ast;
import util.Position;

public final class Number extends Constant {

	public String val;
	
	public Number(Position p, String s) {
		super(p);
		val = s;
	}
	
	public int toInteger() {
		return new Integer(val);
	}
	
}
