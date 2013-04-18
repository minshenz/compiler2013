package ast;

public class DeclaratorList {
	
	public Declarator head;
	public DeclaratorList tail;
	
	public DeclaratorList(Declarator h) {
		head = h;
		tail = null;
	}
	
	public DeclaratorList(Declarator h, DeclaratorList t) {
		head = h;
		tail = t;
	}
	
}
