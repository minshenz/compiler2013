package ast;
import util.Position;

public class CastExpression extends Op {
	
	public Typename casttype;
	public Expression expr;
	
	public CastExpression(Position p, Typename t, Expression e) {
		super(p, null, null, null);
		casttype = t;
		expr = e;
	}

}
