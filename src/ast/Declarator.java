package ast;
import util.Position;

public abstract class Declarator {
	
	public Position pos = null;
	public PlainDeclarator plaindeclarator;
	
	public abstract Symbol getSymbol();
	
}
