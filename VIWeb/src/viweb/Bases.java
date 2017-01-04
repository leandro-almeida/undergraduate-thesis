package viweb;


public class Bases {
	public static final int CARROS = 0;
	public static final String BASE_CARROS = "D:\\carros85.txt";
	
	public static String getCaminhoBase(int base){
		String b = "";
		switch (base) {
			case CARROS:
				b = BASE_CARROS;
				break;
		}
		return b;
	}
}
