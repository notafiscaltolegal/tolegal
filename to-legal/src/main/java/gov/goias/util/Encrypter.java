package gov.goias.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by lucas-mp on 09/01/15.
 */
public class Encrypter {

    public static String encryptSHA512(String target) {
        return DigestUtils.sha512Hex(target);
    }

    public static String encryptMD5(String target) {
        return DigestUtils.md5Hex(target);
    }

    public static String encryptMD5(InputStream inputStream) throws IOException {
        return DigestUtils.md5Hex(inputStream);
    }
}
