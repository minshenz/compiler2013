package ast;

public class PlainDeclarationList {
	
	public TypeSpecifier typespecifier;
	public DeclaratorList decl;
	public PlainDeclarationList tail;

	public PlainDeclarationList(TypeSpecifier h1, DeclaratorList h2) {
		typespecifier = h1;
		decl = h2;
		tail  = null;
	}
	
	public PlainDeclarationList(TypeSpecifier h1, DeclaratorList h2, PlainDeclarationList t) {
		typespecifier = h1;
		decl = h2;
		tail = t;
	}

}
