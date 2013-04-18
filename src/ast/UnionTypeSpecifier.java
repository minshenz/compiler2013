package ast;
import util.Position;

public final class UnionTypeSpecifier extends TypeSpecifier {
	
	public Symbol name;
	public PlainDeclarationList pdl;
	
	public UnionTypeSpecifier(Position p, Symbol s) {
		super(p, TYPE.UNION);
		name = s;
		pdl = null;
	}
	
	public UnionTypeSpecifier(Position po, PlainDeclarationList p) {
		super(po, TYPE.UNION);
		name = null;
		pdl = p;
	}
	
	public UnionTypeSpecifier(Position po, Symbol s, PlainDeclarationList p) {
		super(po, TYPE.UNION);
		name = s;
		pdl = p;
	}

}
