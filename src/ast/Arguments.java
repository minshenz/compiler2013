package ast;

public class Arguments {

	public Expression head;
	public Arguments tail;
	
	public Arguments(Expression h) {
		head = h;
		tail = null;
	}
	
	public Arguments(Expression h, Arguments t) {
		head = h;
		tail = t;
	}
	
}
