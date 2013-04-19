package ast;
import util.Position;

public final class Number extends Constant {

	public String val;
	
	public Number(Position p, String s) {
		super(p);
		val = s;
	}
	
	public int toInteger() {
		if (val.charAt(0) == '0') {
			if (val.length() == 1) return 0;
			if (val.charAt(1) == 'x') return HexInteger(val.substring(2, val.length()));
			else return OctInteger(val.substring(1, val.length()));
		}
		return DecInteger(val);
	}
	
	private int digit(char c) {
		int ans = 0;
		switch (c) {
		case 'F': 
		case 'f': ans++;
		case 'E':
		case 'e': ans++;
		case 'D':
		case 'd': ans++;
		case 'C':
		case 'c': ans++;
		case 'B':
		case 'b': ans++;
		case 'A':
		case 'a': ans++;
		case '9': ans++;
		case '8': ans++;
		case '7': ans++;
		case '6': ans++;
		case '5': ans++;
		case '4': ans++;
		case '3': ans++;
		case '2': ans++;
		case '1': ans++;
		case '0': break;
		}
		return ans;
	}
	
	private int HexInteger(String s) {
		int ans = 0;
		for (int i = 0; i < s.length(); ++i) ans = ans*16 + digit(s.charAt(i));
		return ans;
	}
	
	private int DecInteger(String s) {
		int ans = 0;
		for (int i = 0; i < s.length(); ++i) ans = ans*10 + digit(s.charAt(i));
		return ans;
	}
	
	private int OctInteger(String s) {
		int ans = 0;
		for (int i = 0; i < s.length(); ++i) ans = ans*8 + digit(s.charAt(i));
		return ans;
	}
	
}
