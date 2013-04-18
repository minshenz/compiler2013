package ast;
import util.Position;

public final class CharConstant extends Constant {
	
	public String val;
	
	public CharConstant(Position p, String s) {
		super(p);
		val = s;
	}
	
	public int toInteger() {
		return 0;
	}

}
