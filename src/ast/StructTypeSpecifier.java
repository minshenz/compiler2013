package ast;
import util.Position;

public final class StructTypeSpecifier extends TypeSpecifier {
	
	public Symbol name;
	public PlainDeclarationList pdl;
	
	public StructTypeSpecifier(Position p, Symbol s) {
		super(p, TYPE.STRUCT);
		name = s;
		pdl = null;
	}
	
	public StructTypeSpecifier(Position po, PlainDeclarationList p) {
		super(po, TYPE.STRUCT);
		name = null;
		pdl = p;
	}
	
	public StructTypeSpecifier(Position po, Symbol s, PlainDeclarationList p) {
		super(po, TYPE.STRUCT);
		name = s;
		pdl = p;
	}

}
