package gov.goias.persistencia.util;

import java.text.Normalizer;

/**
 * Created by lucas-mp on 20/10/14.
 */
public class TratamentoDeDados {

    public static String removeAcentos(String str) {
        if(str == null)
            return "";
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }
}
