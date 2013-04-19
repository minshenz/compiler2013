package semantics;
import types.Function;

public class FunEntry extends VarEntry {
	
	public boolean implemented = false;
	
	public FunEntry(Function t) {
		super(t);
	}
	
	public void Implement() {
		implemented = true;
	}

}
