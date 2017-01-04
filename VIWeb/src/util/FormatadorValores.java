package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelo.Coluna;

public final class FormatadorValores {
	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
	private static DecimalFormat decimalFormatScientific = new DecimalFormat("#.##E0");
	DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public static SimpleDateFormat dataHoraBrasil = new SimpleDateFormat("dd/MM/yyyy - kk:mm:ss");
	
	public FormatadorValores() {
    	symbols.setDecimalSeparator(',');
    	symbols.setGroupingSeparator('.');
    	decimalFormat.setDecimalFormatSymbols(symbols);
	}
	
	private static String strValor = "";
	public static String formata(double valor){
		decimalFormat.setMaximumFractionDigits(2);
		decimalFormat.setMinimumFractionDigits(0);
		try {
			strValor = decimalFormat.format(valor); 				
		} catch (NumberFormatException e) {
			strValor = "";
		}
		return strValor;
	}

	public static String formata(double valor, int casas){
		decimalFormat.setMaximumFractionDigits(casas);
		decimalFormat.setMinimumFractionDigits(0);		
		try {
			strValor = decimalFormat.format(valor); 				
		} catch (NumberFormatException e) {
			strValor = "";
		}
		return strValor;
	}
	
	public static String formataCientifico(double valor){
		try {
			if (valor != 0)
				strValor = decimalFormatScientific.format(valor);
		} catch (NumberFormatException e) {
			strValor = "";
		}
		return strValor;
	}
	
	// Tenta formatar os campos passados como par�metro pela seguinte ordem:
	// Integer, Double, String
	public static String formataValorColuna(int tipo, Object valor){
		if (tipo == Coluna.DOUBLE)
			return formata(Double.parseDouble(((Double)valor).toString()));
		else if (tipo == Coluna.DATA){
			return dateFormatter.format(valor);
		}
		
		return valor.toString();			
	}
	
	public static double getValorDouble(byte tipoColuna, Object valorColuna){
		if (tipoColuna == Coluna.INTEGER){
			return ((Integer) valorColuna).intValue();
		}
		else if (tipoColuna == Coluna.DOUBLE){
			return ((Double) valorColuna).doubleValue();		
		}
		else if (tipoColuna == Coluna.DATA){
			return ((Date) valorColuna).getTime();
		}
		return 0;
	}
	
	public static String getDataHoraSistema(){
		return dataHoraBrasil.format(dataHoraBrasil.getCalendar().getTime());
		
	}
	
	public static String formataComPorcentagem(double valor){
		NumberFormat numberFormat = NumberFormat.getPercentInstance();
		numberFormat.setMinimumFractionDigits(2);
		numberFormat.setMaximumFractionDigits(2);		
		return numberFormat.format(valor);		
	}
}
