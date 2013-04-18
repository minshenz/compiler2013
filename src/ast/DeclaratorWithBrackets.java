package ast;
import util.Position;

public class DeclaratorWithBrackets extends Declarator {
	
	public ConstantExpressionPackage constantexpressionpackage;
	
	public DeclaratorWithBrackets(Position po, PlainDeclarator p, ConstantExpressionPackage cep) {
		pos = po;
		plaindeclarator = p;
		constantexpressionpackage = cep;
	}
	
	public Symbol getSymbol() {
		return plaindeclarator.getSymbol();
	}

}
