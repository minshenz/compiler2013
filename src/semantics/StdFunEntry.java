package semantics;
import types.Function;

public class StdFunEntry extends FunEntry {
	
	public StdFunEntry(Function t) {
		super(t);
		implemented = true;
	}

}
