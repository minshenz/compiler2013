package ast;

public class DeclarationList {
	
	public Declaration head;
	public DeclarationList tail;
	
	public DeclarationList(Declaration h) {
		head = h;
		tail = null;
	}
	
	public DeclarationList(Declaration h, DeclarationList t) {
		head = h;
		tail = t;
	}

}
