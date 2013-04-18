package ast;
import util.Position;

public final class TypedefTypeSpecifier extends TypeSpecifier {
	
	public Symbol typedefname;
	
	public TypedefTypeSpecifier(Position p, Symbol s) {
		super(p, TYPE.DEFINED);
		typedefname = s;
	}

}
