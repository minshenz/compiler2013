package ast;
import util.Position;

public class PlainDeclaration extends Declaration {
	
	public TypeSpecifier typespecifier;
	public Declarator dec;
	
	public PlainDeclaration(Position p, TypeSpecifier t, Declarator d) {
		super(p);
		typespecifier = t;
		dec = d;
	}

}
