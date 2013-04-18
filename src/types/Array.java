package types;

public final class Array extends Pointer {
	
	public int capacity;
	
	public Array(Type t, int cap) {
		super(t);
		capacity = cap;
	}

}
