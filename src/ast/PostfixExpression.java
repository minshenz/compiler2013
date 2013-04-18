package ast;
import util.Position;

public class PostfixExpression extends UnaryExpression {

	public Expression pexpr;
	public Postfix postfix;
	
	public PostfixExpression(Position po, Expression pe, Postfix p) {
		super(po, null, null);
		pexpr = pe;
		postfix = p;
	}
	
}
