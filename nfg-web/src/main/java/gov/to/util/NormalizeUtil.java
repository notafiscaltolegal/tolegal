package gov.to.util;

import java.text.Normalizer;

public class NormalizeUtil {

	public static String convert(String input){
		
		return Normalizer
		           .normalize(input, Normalizer.Form.NFD)
		           .replaceAll("[^\\p{ASCII}]", "");
	}
}
