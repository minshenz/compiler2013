package ast;
import util.Position;

public final class StringConstant extends PrimaryExpression {

	public String val;
	
	public int length() {
		return val.length();
	}
	
	public StringConstant(Position p, String s) {
		super(p);
		val = s;
	}
	
}
