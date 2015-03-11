package build1;

/**
 * This datatype will not like strimal strings that look like a byte of binary! 
 * Pass a leading zero in such cases as 1000000000000000 i.e. 01000000000000000
 * Also - don't pass floats, doubles, longs etc as that could go wrong
 * @author Tim
 *
 */
public class Poly {
	
	int n;
	String str, bin;
	
	final static int BIT_LIMIT = 16;
	final static int HALF_DEC_RANGE = 32768; //Probably sort this out to being automatic... 

	public Poly(String strNumber) {
		if(strNumber.length() == BIT_LIMIT && strNumber.matches("[01]+")){
			bin = strNumber;
			n = Integer.parseInt(strNumber, 2) - HALF_DEC_RANGE;
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
		String s = Integer.toBinaryString(Integer.parseInt(number) + HALF_DEC_RANGE);
	    if (s.length() == BIT_LIMIT) return s;
	    else return String.format("%0" + (BIT_LIMIT-s.length()) + "d%s", 0, s);
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
