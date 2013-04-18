package ast;
import util.Position;

public class Program {
	
	public Program head;
	public Program tail;
	public Position pos = null;
	
	public Program(Program h, Program t) {
		head = h;
		tail = t;
	}
	
}
