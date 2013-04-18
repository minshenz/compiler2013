package ast;
import util.Position;

public class InitDeclarator {

	public Declarator declarator;
	public Initializer initializer;
	public Position pos = null;
	
	public InitDeclarator(Position p, Declarator d) {
		pos = p;
		declarator = d;
		initializer = null;
	}
	
	public InitDeclarator(Position p, Declarator d, Initializer i) {
		pos = p;
		declarator = d;
		initializer = i;
	}
	
}
