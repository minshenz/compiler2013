package ast;
import util.Position;

public class ConstantExpression {
	
	public Expression op;
	public Position pos = null; 
	
	public ConstantExpression(Position p, Expression o) {
		pos = p;
		op = o;
	}
	
	public boolean isConstant() {
		return op.isConstant;
	}
}
