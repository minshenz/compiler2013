package ast;
import util.Position;

public class BasicInitializer extends Initializer {
	
	public Expression expr;
	
	public BasicInitializer(Position p, Expression e) {
		pos = p;
		expr = e;
	}

}
