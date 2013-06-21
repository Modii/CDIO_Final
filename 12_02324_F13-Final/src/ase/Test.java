package ase;

import java.text.DecimalFormat;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("#.##");
		double d = 1.5525252525252525;
		String trimmed = (df.format(d));
		String trimmed1 = trimmed.replace(',' , '.');
		double hh = Double.parseDouble(trimmed1);
		System.out.println(hh);
		double f = 1.10;
		System.out.println(hh-f);

	}

}
