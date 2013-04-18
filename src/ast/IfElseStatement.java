package ast;
import util.Position;

public final class IfElseStatement extends SelectionStatement {
	
	public Expression cond;
	public Statement body, elsebody;
	
	public IfElseStatement(Position p, Expression e, Statement s1, Statement s2) {
		pos = p;
		cond = e;
		body = s1;
		elsebody = s2;
	}

}
