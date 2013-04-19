package types;

public final class Ellipsis extends Function {
	
	private Type binding = null;

	public Ellipsis(Type t) {
		super(null, null);
		argumentType = this;
		returnType = this;
		binding = t;
	}
	
	public Type actual() {
		return binding.actual();
	}
	
	public boolean equalTo(Type t) {
		return true;
	}
	
}
