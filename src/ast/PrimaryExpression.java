package ast;
import util.Position;

public abstract class PrimaryExpression extends PostfixExpression {
	
	public PrimaryExpression(Position p) {
		super(p, null, null);
	}

}
