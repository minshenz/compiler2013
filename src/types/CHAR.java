package types;

public final class CHAR extends Type {
	public boolean equalTo(Type t) {
		return t.actual() instanceof CHAR || t.actual() instanceof INT;
	}

}
