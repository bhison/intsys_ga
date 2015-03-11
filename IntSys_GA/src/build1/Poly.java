package build1;

/**
 * This datatype will not like strimal strings that look like a byte of binary! 
 * Pass a leading zero in such cases as 10000000 i.e. 010000000
 * Also - don't pass floats, doubles, longs etc as that could go wrong
 * @author Tim
 *
 */
public class Poly {
	
	int n;
	String str, bin;

	public Poly(String strNumber) {
		if(strNumber.length() == 8 && strNumber.matches("[01]+")){
			bin = strNumber;
			n = Integer.parseInt(strNumber, 2);
			str = Integer.toString(n);
		} else {
			str = strNumber;
			bin = encode(str);
			n = Integer.parseInt(strNumber);
		}
	}
	
	public Poly(int integer){
		n = integer;
		str = Integer.toString(n);
		bin = encode(str);
	}
	
	public String encode(String number){
		int i = Integer.parseInt(number);
		String s = Integer.toBinaryString(i);
		s = "00000000"+s;
		return s.substring(s.length()-8);
	}
	
	public String s(){
		return str;
	}
	
	public String b(){
		return bin;
	}
	
	public int i(){
		return n;
	}
}
