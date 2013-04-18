package types;

public final class VOID extends Type {
	public boolean equalTo(Type t) {
		return t.actual() instanceof VOID;
	}

}
