package ast;
import util.Position;

public final class Var extends PrimaryExpression {
	
	public Symbol symbol;
	
	public Var(Position p, Symbol s) {
		super(p);
		symbol = s;
	}
	
}
