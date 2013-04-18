package ast;
import util.Position;

public final class PlainDeclarator extends Declarator {
	
	public Symbol symbol;
	public int number_of_stars;
	public Position pos = null;
	
	public PlainDeclarator(Position p, Symbol s) {
		pos = p;
		number_of_stars = 0;
		symbol = s;
	}
	
	public PlainDeclarator(Position p, int n, Symbol s) {
		pos = p;
		number_of_stars = n;
		symbol = s;
	}
	
	public int stars() {
		return number_of_stars;
	}
	
	public Symbol getSymbol() {
		return symbol;
	}

}
