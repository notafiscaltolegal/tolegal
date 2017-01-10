package gov.goias.util;

import javax.swing.text.MaskFormatter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

/**
 * Created by lucas-mp on 24/03/15.
 */
public class TextUtils {
    public static String format(String pattern, Object value) {
        MaskFormatter mask;
        try {
            mask = new MaskFormatter(pattern);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeURIComponent(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public List<Map> transformaArrayStringEmMap(ArrayList<String> array){
        List<Map> listaMaps = new ArrayList<Map>();
        Map<String,String> mapa = new HashMap<String,String>();

        Iterator<String> it = array.iterator();
        while(it.hasNext()){
            String s = it.next();
            mapa = new HashMap<String,String>();
            mapa.put("mensagem", s);
            listaMaps.add(mapa);
        }
        return listaMaps;
    }

}
