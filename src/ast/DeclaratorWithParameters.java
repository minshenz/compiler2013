package ast;
import util.Position;

public class DeclaratorWithParameters extends Declarator {

	public Parameters parameters;
	
	public DeclaratorWithParameters(Position po, PlainDeclarator p) {
		pos = po;
		plaindeclarator = p;
		parameters = null;
	}
	
	public DeclaratorWithParameters(Position po, PlainDeclarator p, Parameters para) {
		pos = po;
		plaindeclarator = p;
		parameters = para;
	}
	
	public Symbol getSymbol() {
		return plaindeclarator.getSymbol();
	}
	
}
