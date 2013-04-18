package ast;
import util.Position;

public class FunctionDefinition extends Program {
	
	public TypeSpecifier typespecifier;
	public PlainDeclarator plaindeclarator;
	public Parameters parameters;
	public Statement compoundstatement;
	
	public FunctionDefinition(Position po, TypeSpecifier t, PlainDeclarator p, Parameters para, Statement s) {
		super(null, null);
		pos = po;
		typespecifier = t;
		plaindeclarator = p;
		parameters = para;
		compoundstatement = s;
	}

}
