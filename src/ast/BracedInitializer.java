package ast;
import util.Position;

public class BracedInitializer extends Initializer {
	
	public InitializerList il;
	
	public BracedInitializer(Position p, InitializerList i) {
		pos = p;
		il = i;
	}

}
