package ast;
import util.Position;

public abstract class Declaration extends Program {

	public Declaration(Position p) {
		super(null, null);
		pos = p;
	}
	
}
