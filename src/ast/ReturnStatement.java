package ast;
import util.Position;

public final class ReturnStatement extends JumpStatement {
	
	public Expression expression;
	
	public ReturnStatement(Position p) {
		pos = p;
		expression = null;
	}
	
	public ReturnStatement(Position p, Expression e) {
		pos = p;
		expression = e;
	}
	
}
