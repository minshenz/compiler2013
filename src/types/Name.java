package types;
import ast.Symbol;

public final class Name extends Type {
	
	public Symbol name;
	private Type binding;
	
	public Name(Symbol n) {
		name = n;
	}
	
	public Type actual() {
		return binding.actual();
	}
	
	public boolean equalTo(Type t) {
		return this.actual().equalTo(t);
	}
	
	public boolean isLoop() {
		Type b = binding;
		binding = null;
		boolean isloop;
		if (b == null) isloop = true;
		else if (b instanceof Name) isloop = ((Name) b).isLoop();
		else isloop = false;
		binding = b;
		return isloop;
	}
	
	public void bind(Type t) {
		binding = t;
	}

}
