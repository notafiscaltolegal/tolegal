package gov.goias.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lucas-mp on 09/01/15.
 */
public class Test {

    public static void main(String[] args) {
//        String cpf="76355497153";
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Object[] param = {new Date(), 123,"123",234.22,'C'};
//        String s="";
//        for(Object o:param){
//            s+= " "+o.toString()+" ";
//        }
//        System.out.printf(s);
//        try {
//            String encrypted = Encrypter.encryptSHA512(cpf + sdf.parse("22/06/2015").getTime());
//            System.out.println(sdf.parse("24/06/2015").getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        String senha = String.format("%-10s", "".toUpperCase());
        String senha2 =  "0078900".toUpperCase();

        senha = senha.substring(0,10);

        System.out.println(senha);
        System.out.println(senha2);
    }
}
