package java_ASE;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Other {
	
	String dato;
	
	public String generateDato(){
		Calendar d = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat("00");
		dato = d.get(Calendar.YEAR) + "-"
		+ df.format(d.get(Calendar.MONTH) + 1) + "-"
		+ df.format(d.get(Calendar.DATE)) + " "
		+ df.format(d.get(Calendar.HOUR_OF_DAY)) + ":"
		+ df.format(d.get(Calendar.MINUTE)) + ":"
		+ df.format(d.get(Calendar.SECOND));
		return dato;
	}

}
