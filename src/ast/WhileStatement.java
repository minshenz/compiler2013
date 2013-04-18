package ast;
import util.Position;

public final class WhileStatement extends IterationStatement {
	
	public Expression cond;
	public Statement body;
	
	public WhileStatement(Position p, Expression e, Statement s) {
		pos = p;
		cond = e;
		body = s;
	}

}
