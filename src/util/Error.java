package util;

public final class Error {
	
	private String message = null;
	
	public Error(Position pos, String str) {
		this(pos, str, false);
	}
	
	public Error(Position pos, String str, boolean silent) {
		message = "Error in line " + (1 + pos.line) + ", column " + (1 + pos.column) + ": " + str;		
		if (!silent) {
			System.out.println(this);
			System.out.flush();
		}
	}
	
	public String toString() {
		return message;
	}

}
