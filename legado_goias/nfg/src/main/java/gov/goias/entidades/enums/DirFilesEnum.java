package gov.goias.entidades.enums;

/**
 * @author leticia-abb
 * @since 29/06/16.
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public enum DirFilesEnum {

    DIR_RECEBE_ARQUIVOS_MFD("/mnt/nfg/mfd/recebidos/"),
    DIR_ARQUIVOS_PRONTOS_MFD("/mnt/nfg/mfd/ok/");

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String value;

    DirFilesEnum(String value) {
        this.value = value;
    }

}