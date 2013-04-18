package ast;

public class Parameters {
	
	public PlainDeclaration head;
	public Parameters tail;
	
	public Parameters(PlainDeclaration h) {
		head = h;
		tail = null;
	}
	
	public Parameters(PlainDeclaration h, Parameters t) {
		head = h;
		tail = t;
	}

}
