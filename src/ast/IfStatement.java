package ast;
import util.Position;

public final class IfStatement extends SelectionStatement{
	
	public Expression cond;
	public Statement body;
	
	public IfStatement(Position p, Expression e, Statement s) {
		pos = p;
		cond = e;
		body = s;
	}
	
}
