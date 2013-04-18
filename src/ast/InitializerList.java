package ast;

public class InitializerList {
	
	public Initializer head;
	public InitializerList tail;
	
	public InitializerList(Initializer h) {
		head = h;
		tail = null;
	}
	
	public InitializerList(Initializer h, InitializerList t) {
		head = h;
		tail = t;
	}

}
