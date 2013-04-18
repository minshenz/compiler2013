package ast;
import util.Position;

public final class ForStatement extends IterationStatement {
	
	public Expression expr1, expr2, expr3;
	public Statement body;
	
	public ForStatement(Position p, Expression e1, Expression e2, Expression e3, Statement s) {
		pos = p;
		expr1 = e1;
		expr2 = e2;
		expr3 = e3;
		body = s;
	}

}
