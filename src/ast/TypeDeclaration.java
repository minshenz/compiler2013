package ast;
import util.Position;

public final class TypeDeclaration extends Declaration {
	
	public TypeSpecifier typespecifier;
	public DeclaratorList decl;
	
	public TypeDeclaration(Position p, TypeSpecifier t, DeclaratorList d) {
		super(p);
		typespecifier = t;
		decl = d;
	}

}
