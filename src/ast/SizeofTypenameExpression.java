package ast;
import util.Position;

public class SizeofTypenameExpression extends UnaryExpression {
	
	public Typename typename;
	
	public SizeofTypenameExpression(Position p, Typename t) {
		super(p, null, null);
		typename = t;
	}

}
