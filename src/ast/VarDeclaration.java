package ast;
import util.Position;

public final class VarDeclaration extends Declaration {
	
	public TypeSpecifier typespecifier;
	public InitDeclaratorList idecl;
	
	public VarDeclaration(Position p, TypeSpecifier t, InitDeclaratorList i) {
		super(p);
		typespecifier = t;
		idecl = i;
	}
	
}
