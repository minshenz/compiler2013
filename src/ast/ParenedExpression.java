package ast;
import util.Position;

public final class ParenedExpression extends PrimaryExpression {
	
	public Expression expression;
	
	public ParenedExpression(Position p, Expression e) {
		super(p);
		expression = e;
	}

}
