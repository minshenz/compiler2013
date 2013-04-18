package ast;

public class CompoundStatement extends Statement {
	
	public DeclarationList decl;
	public StatementList stmtl;
	
	public CompoundStatement(DeclarationList d, StatementList s) {
		decl = d;
		stmtl = s;
	}
	
}
