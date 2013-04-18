package ast;
import util.Position;

public class UnaryCastExpression extends UnaryExpression {
	
	public enum UnaryOperator {
		DIGIT_AND, TIMES, PLUS, MINUS, DIGIT_NOT, NOT
	}
	
	public UnaryOperator Unaryoperator;
	public CastExpression castexpression;
	
	public UnaryCastExpression(Position p, UnaryOperator o, CastExpression e) {
		super(p, null, null);
		Unaryoperator = o;
		castexpression = e;
	}

}
