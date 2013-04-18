package ast;
import util.Position;

public class UnaryExpression extends CastExpression {
	
	public static enum PrefixOp{
		INC, DEC, SIZEOF
	}
	
	public PrefixOp Prefixop;
	public Expression uexpr;
	
	public UnaryExpression(Position p, PrefixOp o, Expression u) {
		super(p, null, null);
		Prefixop = o;
		uexpr = u;
	}
	
}
