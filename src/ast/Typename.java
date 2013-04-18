package ast;
import util.Position;

public class Typename {
	
	public TypeSpecifier typespecifier;
	public int number_of_stars;
	public Position pos = null;
	
	public Typename(Position p, TypeSpecifier t) {
		pos = p;
		typespecifier = t;
		number_of_stars = 0;
	}
	
	public Typename(Position p, TypeSpecifier t, int n) {
		pos = p;
		typespecifier = t;
		number_of_stars = n;
	}
	
	public int stars() {
		return number_of_stars;
	}
	
}
