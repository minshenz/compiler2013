package ast;

public class InitDeclaratorList {
	
	public InitDeclarator head;
	public InitDeclaratorList tail;
	
	public InitDeclaratorList(InitDeclarator h) {
		head = h;
		tail = null;
	}
	
	public InitDeclaratorList(InitDeclarator h, InitDeclaratorList t) {
		head = h;
		tail = t;
	}

}
