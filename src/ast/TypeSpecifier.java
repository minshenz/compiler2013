package ast;
import util.Position;

public class TypeSpecifier {
	
	public enum TYPE{
		VOID, INT, CHAR, DEFINED, STRUCT, UNION
	} 
	
	public TYPE type;
	public Position pos = null;

	public TypeSpecifier(Position p, TYPE t) {
		pos = p;
		type = t;
	}

}
